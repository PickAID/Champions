package top.theillusivec4.champions.champion.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
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
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.attachment.Attachments;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.ChampionDefaultProperties;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.AffixEffectComponents;
import top.theillusivec4.champions.champion.affix.LatestDamage;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.affix.effect.ConditionalEffect;
import top.theillusivec4.champions.champion.affix.effect.DamageImmunity;
import top.theillusivec4.champions.champion.affix.event.ChampionEventHooks;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.component.DataComponents;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;
import top.theillusivec4.champions.world.loot.parameters.LootContextParamSets;

import java.util.Optional;
import java.util.function.Consumer;

public class ChampionHandlerEntityImpl implements ChampionHandlerEntity {
  private static final Logger LOGGER = LogManager.getLogger();
  private final Entity entity;

  public ChampionHandlerEntityImpl(Entity entity) {
    this.entity = entity;
  }

  @Override
  public void runInitializeEffects(ServerLevel serverLevel, Entity entity, Vec3 origin) {
    this.runIteration(affix -> affix.value().runInitialize(serverLevel, this.getLevel(), entity, origin));
  }

  @Override
  public void stopInitializeEffects(ServerLevel serverLevel, Entity entity, Vec3 origin) {
    this.runIteration(affix -> affix.value().stopInitialized(serverLevel, this.getLevel(), entity, origin));
  }

  @Override
  public boolean isImmuneToDamage(ServerLevel serverLevel, DamageSource damageSource) {
    LootContext context = LootContextParamSets.damageImmunity(serverLevel, this.entity, this.getLevel(), damageSource, this.getLatestDamage(), damageSource.getDirectEntity(), damageSource.getEntity());
    for (Holder<Affix> affix : this.getAllAffixes().getAffixes()) {
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
    this.runIteration(affix -> affix.value().modifyDamageProtection(serverLevel, this.getLevel(), this.entity, damageSource, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyKnockback(ServerLevel serverLevel, DamageSource damageSource, float knockback) {
    MutableFloat mutableFloat = new MutableFloat(knockback);
    this.runIteration(affix -> affix.value().modifyKnockback(serverLevel, this.getLevel(), this.entity, damageSource, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyDamage(ServerLevel serverLevel, Entity target, DamageSource damageSource, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    this.runIteration(affix -> affix.value().modifyDamage(serverLevel, this.getLevel(), this.entity, damageSource, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyHeal(ServerLevel serverLevel, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    this.runIteration(affix -> affix.value().modifyHeal(serverLevel, this.getLevel(), this.entity, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public void doPostAttackEffects(ServerLevel serverLevel, AffixTarget targetType, Entity victim, DamageSource damageSource) {
    this.runIteration(affix -> affix.value().doPostAttack(serverLevel, this.getLevel(), targetType, victim, damageSource, this.getLatestDamage()));
  }

  @Override
  public void tickEffects(ServerLevel serverLevel) {
    this.runIteration(affix -> affix.value().tick(serverLevel, this.getLevel(), this.entity, this.getLatestDamage()));
  }

  @Override
  public void runIteration(Consumer<Holder<Affix>> consumer) {
    for (Holder<Affix> affix : this.getAllAffixes().getAffixes()) {
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
  public void copyFrom(IAttachmentHolder holder) {
    if (this.entity != holder && !this.entity.level().isClientSide()) {
      if (holder.hasData(Attachments.AFFIXES)) {
        this.updateAffixes(mutable -> {
          for (Holder<Affix> affix : holder.getData(Attachments.AFFIXES).getAffixes()) {
            mutable.add(affix);
          }
        });
      }

      if (holder.hasData(Attachments.RANK)) {
        holder.getData(Attachments.RANK).ifPresent(this::setRank);
      }

      if (holder.hasData(Attachments.PREFIX_NAME)) {
        holder.getData(Attachments.PREFIX_NAME).ifPresent(this::setPrefixName);
      }

      if (holder.hasData(Attachments.LEVEL)) {
        this.setLevel(holder.getData(Attachments.LEVEL));
      }

      if (holder.hasData(Attachments.COLOR)) {
        this.setColor(holder.getData(Attachments.COLOR));
      }

      if (holder.hasData(Attachments.BOSS)) {
        this.setBoss(holder.getData(Attachments.BOSS));
      }
    }
  }

  @Override
  public void copyFrom(DataComponentHolder holder) {
    if (this.entity.level() instanceof ServerLevel serverLevel) {
      if (holder.has(DataComponents.AFFIXES)) {
        ChampionEventHooks.onUpdateAffixesPre(this.entity, serverLevel, this);
        this.stopInitializeEffects(serverLevel, this.entity, this.entity.position());
        this.entity.setData(Attachments.AFFIXES, holder.getOrDefault(DataComponents.AFFIXES, Affixes.EMPTY).copy());
        ChampionEventHooks.onUpdateAffixesPost(this.entity, serverLevel, this);
        this.runInitializeEffects(serverLevel, this.entity, this.entity.position());
      }

      if (holder.has(DataComponents.RANK)) {
        this.setRank(holder.getOrDefault(DataComponents.RANK, serverLevel.registryAccess().getOrThrow(Ranks.EMPTY)));
      }

      if (holder.has(DataComponents.PREFIX_NAME)) {
        this.setPrefixName(holder.getOrDefault(DataComponents.PREFIX_NAME, Component.empty()));
      }

      if (holder.has(DataComponents.LEVEL)) {
        this.setLevel(holder.getOrDefault(DataComponents.LEVEL, ChampionDefaultProperties.MIN_LEVEL));
      }

      if (holder.has(DataComponents.COLOR)) {
        this.setColor(holder.getOrDefault(DataComponents.COLOR, -1));
      }

      if (holder.has(DataComponents.BOSS)) {
        this.setBoss(holder.getOrDefault(DataComponents.BOSS, false));
      }
    }
  }

  public Affixes getAllAffixes() {
    return this.entity.getData(Attachments.AFFIXES);
  }

  @Override
  public int getLevel() {
    if (this.entity.hasData(Attachments.LEVEL)) {
      return this.entity.getData(Attachments.LEVEL);
    } else {
      return this.getRank().map(rank -> rank.value().level()).orElse(ChampionDefaultProperties.DEFAULT_LEVEL);
    }
  }

  @Override
  public void setLevel(int level) {
    this.entity.setData(Attachments.LEVEL, Math.clamp(level, ChampionDefaultProperties.MIN_LEVEL, ChampionDefaultProperties.MAX_LEVEL));
  }

  @Override
  public int getColor() {
    if (this.entity.hasData(Attachments.COLOR)) {
      return this.entity.getData(Attachments.COLOR);
    }

    return this.getRank().map(rank -> rank.value().color()).orElse(ChampionDefaultProperties.DEFAULT_COLOR);
  }

  @Override
  public void setColor(int color) {
    this.entity.setData(Attachments.COLOR, ARGB.opaque(color));
  }

  @Override
  public Optional<Holder<Rank>> getRank() {
    return this.entity.getData(Attachments.RANK);
  }

  @Override
  public void setRank(Holder<Rank> rank) {
    if (!rank.is(Ranks.EMPTY)) {
      this.entity.setData(Attachments.RANK, Optional.of(rank));
    }
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

  @Override
  public void updateLatestDamage(Consumer<LatestDamage.Mutable> consumer) {
    if (!this.entity.level().isClientSide()) {
      LatestDamage latestDamage = this.entity.getData(Attachments.LATEST_DAMAGE);
      LatestDamage.Mutable mutable = latestDamage.toMutable();
      consumer.accept(mutable);
      this.entity.setData(Attachments.LATEST_DAMAGE, mutable.toImmutable());
    }
  }

  @Override
  public Optional<ServerChampionBossEvent> getBossEvent() {
    if (!this.entity.level().isClientSide()) {
      return this.entity.getExistingData(Attachments.SERVER_CHAMPION_BOSS_EVENT).orElse(Optional.empty());
    }

    return Optional.empty();
  }

  private void setBossEvent(@Nullable ServerChampionBossEvent event) {
    if (event == null) {
      Optional<ServerChampionBossEvent> optional = this.entity.removeData(Attachments.SERVER_CHAMPION_BOSS_EVENT);
      if (optional != null && optional.isPresent()) {
        ServerChampionBossEvent event1 = optional.get();
        event1.removeAllPlayers();
      }
    } else {
      this.entity.setData(Attachments.SERVER_CHAMPION_BOSS_EVENT, Optional.of(event));
    }
  }

  @Override
  public boolean isBoss() {
    return this.entity.getExistingData(Attachments.BOSS).orElse(false);
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
        this.setBossEvent(new ServerChampionBossEvent(Mth.createInsecureUUID(this.entity.getRandom()), name, this.getHealth() / this.getMaxHealth(), this.getLevel(), this.getColor(), this.getAllAffixes().getAffixes()));
        this.entity.setData(Attachments.BOSS, true);
      } else {
        this.setBossEvent(null);
        this.entity.setData(Attachments.BOSS, false);
      }
    } else {
      LOGGER.warn("当前认为客户端不应该使用 SetBoss");
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

      ChampionUtil.getHandler(itemStack).ifPresent(handlerItem -> handlerItem.copyFrom(this.entity));

      return itemStack;
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean isSpawned() {
    return this.entity.getExistingData(Attachments.SPAWNED).orElse(false);
  }

  @Override
  public void setSpawned(boolean spawned) {
    if (!spawned) {
      this.entity.removeData(Attachments.SPAWNED);
    }

    this.entity.setData(Attachments.SPAWNED, spawned);
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

  private LatestDamage getLatestDamage() {
    return this.entity.getData(Attachments.LATEST_DAMAGE);
  }
}
