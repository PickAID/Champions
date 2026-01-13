package top.theillusivec4.champions.champion.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.attachment.Attachments;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.AffixEffectComponents;
import top.theillusivec4.champions.champion.affix.Damage;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.affix.effect.ConditionalEffect;
import top.theillusivec4.champions.champion.affix.effect.DamageImmunity;
import top.theillusivec4.champions.champion.event.ChampionEventHooks;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.server.champion.config.ChampionDefaultConfigs;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;
import top.theillusivec4.champions.world.loot.parameters.LootContextParamSets;

import java.util.Optional;
import java.util.function.Consumer;

public record ChampionHandlerEntityImpl(Entity entity) implements ChampionHandlerEntity {

  @Override
  public void runInitializeEffects(ServerLevel serverLevel, Entity entity, Vec3 origin) {
    this.runIteration(affix -> affix.value().runInitialize(serverLevel, this.getLevelOrDefault(), entity, origin));
  }

  @Override
  public void stopInitializeEffects(ServerLevel serverLevel, Entity entity, Vec3 origin) {
    this.runIteration(affix -> affix.value().stopInitialized(serverLevel, this.getLevelOrDefault(), entity, origin));
  }

  @Override
  public boolean isImmuneToDamage(ServerLevel serverLevel, DamageSource damageSource) {
    LootContext context = LootContextParamSets.damageImmunity(serverLevel, this.entity, this.getLevelOrDefault(), damageSource, this.getLatestDamage(), damageSource.getDirectEntity(), damageSource.getEntity());
    for (Holder<Affix> affix : this.getAffixesOrDefault().getAffixes()) {
      for (ConditionalEffect<DamageImmunity> effect : affix.value().getEffects(AffixEffectComponents.DAMAGE_IMMUNITY)) {
        if (effect.matches(context)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public float getDamageProtection(ServerLevel serverLevel, DamageSource damageSource) {
    MutableFloat mutableFloat = new MutableFloat(0.0f);
    this.runIteration(affix -> affix.value().modifyDamageProtection(serverLevel, this.getLevelOrDefault(), this.entity, damageSource, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyKnockback(ServerLevel serverLevel, DamageSource damageSource, float knockback) {
    MutableFloat mutableFloat = new MutableFloat(knockback);
    this.runIteration(affix -> affix.value().modifyKnockback(serverLevel, this.getLevelOrDefault(), this.entity, damageSource, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyDamage(ServerLevel serverLevel, Entity target, DamageSource damageSource, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    this.runIteration(affix -> affix.value().modifyDamage(serverLevel, this.getLevelOrDefault(), this.entity, damageSource, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyHeal(ServerLevel serverLevel, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    this.runIteration(affix -> affix.value().modifyHeal(serverLevel, this.getLevelOrDefault(), this.entity, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public void doPostAttackEffects(ServerLevel serverLevel, AffixTarget targetType, Entity victim, DamageSource damageSource) {
    this.runIteration(affix -> affix.value().doPostAttack(serverLevel, this.getLevelOrDefault(), targetType, victim, damageSource, this.getLatestDamage()));
  }

  @Override
  public void tickEffects(ServerLevel serverLevel) {
    this.runIteration(affix -> affix.value().tick(serverLevel, this.getLevelOrDefault(), this.entity, this.getLatestDamage()));
  }

  @Override
  public void runIteration(Consumer<Holder<Affix>> consumer) {
    for (Holder<Affix> affix : this.getAffixesOrDefault().getAffixes()) {
      consumer.accept(affix);
    }
  }

  @Override
  public void updateAffixes(Consumer<Affixes.Mutable> consumer) {
    if (this.entity.level() instanceof ServerLevel serverLevel) {
      Affixes oldAffixes = entity.getData(Attachments.AFFIXES);
      Affixes.Mutable mutable = oldAffixes.toMutable();
      consumer.accept(mutable);
      Affixes newAffixes = mutable.toImmutable();
      ChampionEventHooks.onUpdateAffixesPre(this.entity, serverLevel, this);
      this.stopInitializeEffects(serverLevel, this.entity, this.entity.position());
      this.entity.setData(Attachments.AFFIXES, newAffixes);
      ChampionEventHooks.onUpdateAffixesPost(this.entity, serverLevel, this);
      this.runInitializeEffects(serverLevel, this.entity, this.entity.position());
      // BossBar
      this.getBossEvent().ifPresent(bossEvent -> bossEvent.setAffixes(newAffixes.getAffixes()));
    }
  }

  @Override
  public Optional<Affixes> getAffixes() {
    return this.entity.getExistingData(Attachments.AFFIXES);
  }

  public Affixes getAffixesOrDefault() {
    return this.getAffixes().orElse(Affixes.EMPTY);
  }

  @Override
  public Optional<Integer> getLevel() {
    if (this.entity.hasData(Attachments.LEVEL)) {
      return Optional.of(this.entity.getData(Attachments.LEVEL));
    } else {
      return this.getRank().map(rank -> rank.value().level());
    }
  }

  @Override
  public void setLevel(int level) {
    if (level <= ChampionDefaultConfigs.DEFAULT_LEVEL) {
      this.entity.removeData(Attachments.LEVEL);
    } else {
      this.entity.setData(Attachments.LEVEL, Math.clamp(level, ChampionDefaultConfigs.MIN_LEVEL, ChampionDefaultConfigs.MAX_LEVEL));
    }
  }

  @Override
  public int getLevelOrDefault() {
    return this.getLevel().orElse(ChampionDefaultConfigs.DEFAULT_LEVEL);
  }

  @Override
  public Optional<Boolean> isBoss() {
    return this.entity.getExistingData(Attachments.BOSS);
  }

  @Override
  public boolean isBossOrDefault() {
    return this.isBoss().orElse(false);
  }

  @Override
  public void setBoss(boolean boss) {
    // 对客户端来说，该数据是只读的
    if (!this.entity.level().isClientSide()) {
      if (boss) {
        Component name = this.getPrefixName().map(component -> (Component) component.copy()
          .append(CommonComponents.space())
          .append(this.entity.getDisplayName())
        ).orElse(this.entity.getDisplayName());
        this.setBossEvent(new ServerChampionBossEvent(Mth.createInsecureUUID(this.entity.getRandom()), name, this.getHealth() / this.getMaxHealth(), this.getLevelOrDefault(), this.getColorOrDefault(), this.getAffixesOrDefault().getAffixes()));
        this.entity.setData(Attachments.BOSS, true);
      } else {
        this.setBossEvent(null);
        this.entity.setData(Attachments.BOSS, false);
      }
    }
  }

  @Override
  public Optional<Integer> getColor() {
    if (this.entity.hasData(Attachments.COLOR)) {
      return Optional.of(this.entity.getData(Attachments.COLOR));
    }

    return this.getRank().map(rank -> rank.value().color());
  }

  @Override
  public void setColor(int color) {
    if (color == ChampionDefaultConfigs.DEFAULT_COLOR) {
      this.entity.removeData(Attachments.COLOR);
    } else {
      this.entity.setData(Attachments.COLOR, ARGB.opaque(color));
    }
  }

  @Override
  public Optional<Holder<Rank>> getRank() {
    return this.entity.getData(Attachments.RANK);
  }

  @Override
  public void setRank(Holder<Rank> rank) {
    this.entity.setData(Attachments.RANK, Optional.of(rank));
  }

  @Override
  public Optional<Component> getPrefixName() {
    if (this.entity.hasData(Attachments.PREFIX_NAME)) {
      return this.entity.getData(Attachments.PREFIX_NAME);
    }

    return this.getRank().map(rank -> rank.value().description());
  }

  @Override
  public void setPrefixName(@Nullable Component name) {
    if (name == null) {
      this.entity.removeData(Attachments.PREFIX_NAME);
    } else {
      this.entity.setData(Attachments.PREFIX_NAME, Optional.of(name));
    }
  }

  /**
   * 获取该冠军实体的刷怪蛋，只有在该实体的冠军配置不为空时才该返回具有数据的刷怪蛋
   *
   * @return 刷怪蛋
   */
  @Override
  public ItemStack getSpawnEgg() {
    EntityType<?> entityType = this.entity.getType();
    Identifier id = EntityType.getKey(entityType).withSuffix("_spawn_egg");
    Item item = BuiltInRegistries.ITEM.getValue(id);
    //noinspection ConstantValue
    if (item != null && item != Items.AIR) {
      ItemStack itemStack = new ItemStack(item);

      ChampionUtil.getHandler(itemStack).ifPresent(handlerItem -> handlerItem.applyConfig(this.deriveConfig()));

      return itemStack;
    }

    return ItemStack.EMPTY;
  }

  @Override
  public float getHealth() {
    if (this.entity instanceof LivingEntity livingEntity) {
      return livingEntity.getHealth();
    }

    return 1.0f;
  }

  @Override
  public float getMaxHealth() {
    if (this.entity instanceof LivingEntity livingEntity) {
      return livingEntity.getMaxHealth();
    }
    return 1.0f;
  }
}
