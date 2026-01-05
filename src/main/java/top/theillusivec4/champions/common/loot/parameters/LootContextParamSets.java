package top.theillusivec4.champions.common.loot.parameters;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.api.affix.LatestDamage;
import top.theillusivec4.champions.common.mixin.LootContextParamSetsAccessor;
import top.theillusivec4.champions.common.util.Utils;

import java.util.Optional;

public final class LootContextParamSets {

  public static final ContextKeySet ATTRIBUTES = register("attribute",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL)
  );
  public static final ContextKeySet KNOCKBACK = register("knockback",
    builder().required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .required(top.theillusivec4.champions.common.loot.parameters.LootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final ContextKeySet POST_ATTACK = register("post_attack",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .required(top.theillusivec4.champions.common.loot.parameters.LootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final ContextKeySet DAMAGE_PROTECTION = register("damage_protection",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .required(top.theillusivec4.champions.common.loot.parameters.LootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final ContextKeySet TICK = register("tick",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .required(top.theillusivec4.champions.common.loot.parameters.LootContextParams.LATEST_DAMAGE)
  );

  public static final ContextKeySet SPAWN = register("spawn",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL)
  );

  public static LootContext attributes(ServerLevel serverLevel, Entity entity, int level) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .create(ATTRIBUTES);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext knockback(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, LatestDamage latestDamage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withParameter(top.theillusivec4.champions.common.loot.parameters.LootContextParams.LATEST_DAMAGE, latestDamage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(KNOCKBACK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext postAttack(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, LatestDamage latestDamage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withParameter(top.theillusivec4.champions.common.loot.parameters.LootContextParams.LATEST_DAMAGE, latestDamage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(POST_ATTACK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext damageProtection(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, LatestDamage latestDamage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withParameter(top.theillusivec4.champions.common.loot.parameters.LootContextParams.LATEST_DAMAGE, latestDamage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(DAMAGE_PROTECTION);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext tick(ServerLevel serverLevel, Entity entity, int level, LatestDamage latestDamage) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withParameter(top.theillusivec4.champions.common.loot.parameters.LootContextParams.LATEST_DAMAGE, latestDamage)
      .create(TICK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext spawn(ServerLevel serverLevel, Entity entity, int level) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(top.theillusivec4.champions.common.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .create(SPAWN);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  private static ContextKeySet register(String name, ContextKeySet.Builder builder) {
    ContextKeySet contextKeySet = builder.build();
    LootContextParamSetsAccessor.getRegistry().put(Utils.id(name), contextKeySet);
    return contextKeySet;
  }

  private static ContextKeySet.Builder builder() {
    return new ContextKeySet.Builder();
  }

  private LootContextParamSets() {
  }

}
