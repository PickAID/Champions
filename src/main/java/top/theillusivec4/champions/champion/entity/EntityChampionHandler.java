package top.theillusivec4.champions.champion.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.attachment.Attachments;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.AffixEffectComponents;
import top.theillusivec4.champions.champion.affix.LatestDamage;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.affix.effect.ConditionalEffect;
import top.theillusivec4.champions.champion.affix.effect.DamageImmunity;
import top.theillusivec4.champions.champion.affix.event.ChampionEventHooks;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.champion.reference.ChampionLevel;
import top.theillusivec4.champions.component.DataComponents;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;
import top.theillusivec4.champions.world.loot.parameters.LootContextParamSets;

import java.util.Optional;
import java.util.function.Consumer;

public class EntityChampionHandler implements ChampionHandlerEntity {
  private static final Logger LOGGER = LogManager.getLogger();
  private final Entity entity;

  public EntityChampionHandler(Entity entity) {
    this.entity = entity;
  }

  @Override
  public void runInitializeEffects(ServerLevel serverLevel, int level, Entity victim, Vec3 origin) {
    this.runIteration(affix -> affix.value().runInitialize(serverLevel, level, victim, origin));
  }

  @Override
  public void stopInitializeEffects(ServerLevel serverLevel, int level, Entity victim, Vec3 origin) {
    this.runIteration(affix -> affix.value().stopInitialized(serverLevel, level, victim, origin));
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
  public float getDamageProtection(ServerLevel serverLevel, DamageSource source) {
    MutableFloat mutableFloat = new MutableFloat(0.0f);
    this.runIteration(affix -> affix.value().modifyDamageProtection(serverLevel, this.getLevel(), this.entity, source, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyKnockback(ServerLevel serverLevel, DamageSource source, float knockback) {
    MutableFloat mutableFloat = new MutableFloat(knockback);
    this.runIteration(affix -> affix.value().modifyKnockback(serverLevel, this.getLevel(), this.entity, source, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyDamage(ServerLevel level, Entity victim, DamageSource damageSource, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    this.runIteration(affix -> affix.value().modifyDamage(level, this.getLevel(), this.entity, damageSource, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public float modifyHeal(ServerLevel level, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    this.runIteration(affix -> affix.value().modifyHeal(level, this.getLevel(), this.entity, this.getLatestDamage(), mutableFloat));
    return mutableFloat.floatValue();
  }

  @Override
  public void doPostAttackEffects(ServerLevel serverLevel, AffixTarget target, Entity victim, DamageSource source) {
    this.runIteration(affix -> affix.value().doPostAttack(serverLevel, this.getLevel(), target, victim, source, this.getLatestDamage()));
  }

  @Override
  public void tickEffects(ServerLevel level) {
    this.runIteration(affix -> affix.value().tick(level, this.getLevel(), this.entity, this.getLatestDamage()));
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
      this.stopInitializeEffects(serverLevel, this.getLevel(), this.entity, this.entity.position());
      this.entity.setData(Attachments.AFFIXES, newAffixes);
      ChampionEventHooks.onUpdateAffixesPost(this.entity, serverLevel, this);
      this.runInitializeEffects(serverLevel, this.getLevel(), this.entity, this.entity.position());
      // BossBar
      this.getEvent().ifPresent(bossEvent -> bossEvent.setAffixes(newAffixes.getAffixes()));
    }
  }

  @Override
  public void copyFrom(IAttachmentHolder holder) {
    if (this.entity != holder && this.entity.level() instanceof ServerLevel serverLevel) {
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
        this.setPrefixName(holder.getData(Attachments.PREFIX_NAME).copy());
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
        this.stopInitializeEffects(serverLevel, this.getLevel(), this.entity, this.entity.position());
        this.entity.setData(Attachments.AFFIXES, holder.getOrDefault(DataComponents.AFFIXES, Affixes.EMPTY).copy());
        ChampionEventHooks.onUpdateAffixesPost(this.entity, serverLevel, this);
        this.runInitializeEffects(serverLevel, this.getLevel(), this.entity, this.entity.position());
      }

      if (holder.has(DataComponents.RANK)) {
        this.setRank(holder.getOrDefault(DataComponents.RANK, serverLevel.registryAccess().getOrThrow(Ranks.COMMON)));
      }

      if (holder.has(DataComponents.PREFIX_NAME)) {
        this.setPrefixName(holder.getOrDefault(DataComponents.PREFIX_NAME, Component.empty()));
      }

      if (holder.has(DataComponents.LEVEL)) {
        this.setLevel(holder.getOrDefault(DataComponents.LEVEL, ChampionLevel.MIN_LEVEL));
      }

      if (holder.has(DataComponents.COLOR)) {
        this.setColor(holder.getOrDefault(DataComponents.COLOR, -1));
      }

      if (holder.has(DataComponents.BOSS)) {
        this.setBoss(holder.getOrDefault(DataComponents.BOSS, false));
      }
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
  public boolean isDisplay() {
    return true;
  }

  @Override
  public void setDisplay(boolean display) {

  }

  public Affixes getAllAffixes() {
    return this.entity.getData(Attachments.AFFIXES);
  }

  @Override
  public int getLevel() {
    if (this.entity.hasData(Attachments.LEVEL)) {
      return this.entity.getData(Attachments.LEVEL);
    } else {
      Holder<Rank> rank = this.getRank();
      return rank.value().level();
    }
  }

  @Override
  public void setLevel(int level) {
    this.entity.setData(Attachments.LEVEL, Math.clamp(level, ChampionLevel.MIN_LEVEL, ChampionLevel.MAX_LEVEL));
  }

  @Override
  public int getColor() {
    if (this.entity.hasData(Attachments.COLOR)) {
      return this.entity.getData(Attachments.COLOR);
    }

    Holder<Rank> rank = this.getRank();
    return rank.value().color();

  }

  @Override
  public void setColor(int color) {
    this.entity.setData(Attachments.COLOR, ARGB.opaque(color));
  }

  @Override
  public Holder<Rank> getRank() {
    return this.entity.getData(Attachments.RANK).orElse(this.entity.registryAccess().getOrThrow(Ranks.COMMON));
  }

  @Override
  public void setRank(Holder<Rank> rank) {
    this.entity.setData(Attachments.RANK, Optional.of(rank));
  }

  @Override
  public Component getPrefixName() {
    if (this.entity.hasData(Attachments.PREFIX_NAME)) {
      return this.entity.getData(Attachments.PREFIX_NAME);
    }

    Holder<Rank> rank = this.getRank();
    return rank.value().description();
  }

  @Override
  public void setPrefixName(Component name) {
    this.entity.setData(Attachments.PREFIX_NAME, name);
  }

  @Override
  public boolean isBoss() {
    return this.entity.getExistingData(Attachments.BOSS).orElse(false);
  }

  @Override
  public void setBoss(boolean boss) {
    // On Server 客户端禁止写入该数据
    if (!this.entity.level().isClientSide()) {
      if (boss) {
        this.setEvent(new ServerChampionBossEvent(Mth.createInsecureUUID(this.entity.getRandom()), this.getName(), this.getHealth() / this.getMaxHealth(), this.getLevel(), this.getColor(), this.getAllAffixes().getAffixes()));
        this.entity.setData(Attachments.BOSS, true);
      } else {
        this.setEvent(null);
        this.entity.setData(Attachments.BOSS, false);
      }
    } else {
      LOGGER.warn("当前认为客户端不应该使用 SetBoss");
    }
  }

  @Override
  public Component getName() {
    return this.getPrefixName().copy().append(CommonComponents.space().append(entity.getDisplayName()));
  }

  @Override
  public Optional<ServerChampionBossEvent> getEvent() {
    if (!this.entity.level().isClientSide()) {
      return this.entity.getExistingData(Attachments.SERVER_CHAMPION_BOSS_EVENT).orElse(Optional.empty());
    }

    return Optional.empty();
  }

  @Override
  public void setEvent(@Nullable ServerChampionBossEvent event) {
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
  public Entity getEntity() {
    return this.entity;
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
