package top.theillusivec4.champions.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.extralootparam.ExtraLootParamHelper;
import top.theillusivec4.champions.world.loot.parameters.ChampionsLootContextParamSets;
import top.theillusivec4.champions.world.loot.parameters.ChampionsLootContextParams;

import java.util.Optional;

public final class LootContextFactory {
  private LootContextFactory() {
  }

  public static LootContext attributes(ServerLevel serverLevel, Entity entity, int affixLevel) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel);
    ExtraLootParamHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.ATTRIBUTES);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext knockback(ServerLevel serverLevel, Entity entity, DamageSource damageSource, int affixLevel, @Nullable Entity direct, @Nullable Entity attacking) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, direct)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attacking);
    ExtraLootParamHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.KNOCKBACK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext damage(ServerLevel serverLevel, Entity entity, DamageSource damageSource, int affixLevel, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity);
    ExtraLootParamHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.DAMAGE);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext heal(ServerLevel serverLevel, Entity entity, int affixLevel) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel);
    ExtraLootParamHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.HEAL);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext postAttack(ServerLevel serverLevel, Entity entity, DamageSource damageSource, int affixLevel, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity);
    ExtraLootParamHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.POST_ATTACK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext damageImmunity(ServerLevel serverLevel, Entity entity, DamageSource damageSource, int affixLevel, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity);
    ExtraLootParamHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.DAMAGE_IMMUNITY);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext damageProtection(ServerLevel serverLevel, Entity entity, int affixLevel, DamageSource damageSource, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity);
    ExtraLootParamHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.DAMAGE_PROTECTION);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext tick(ServerLevel serverLevel, Entity entity, int affixLevel) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel);
    ExtraLootParamHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.TICK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext location(ServerLevel serverLevel, Entity entity, Vec3 origin, int affixLevel) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, origin)
      .withParameter(ChampionsLootContextParams.CHAMPION_TIER, affixLevel);
    ExtraLootParamHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.LOCATION);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static LootContext spawn(ServerLevel serverLevel, Entity entity, int affixLevel, @Nullable MobSpawnType spawnReason, Optional<ResourceLocation> randomSequenceKey) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(ChampionsLootContextParams.CHAMPION_TIER, affixLevel)
      .create(ChampionsLootContextParamSets.SPAWN);
    return new LootContext.Builder(params).create(randomSequenceKey);
  }
}
