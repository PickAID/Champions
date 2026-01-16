package top.theillusivec4.champions.world.loot.parameters;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.champion.affix.Damage;
import top.theillusivec4.champions.mixin.LootContextParamSetsAccessor;
import top.theillusivec4.champions.util.Util;

import java.util.Optional;

public final class LootContextParamSets {
  /**
   * 用于实体生成选择配置
   */
  public static final ContextKeySet SPAWN = register(
    "spawn",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
  );
  public static final ContextKeySet LOCATION = register(
    "location",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL)
  );
  public static final ContextKeySet ATTRIBUTES = register("attribute",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL)
  );
  public static final ContextKeySet KNOCKBACK = register("knockback",
    builder().required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .optional(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final ContextKeySet DAMAGE = register("damage",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .optional(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final ContextKeySet HEAL = register("heal",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .optional(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE)
  );
  public static final ContextKeySet POST_ATTACK = register("post_attack",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .optional(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final ContextKeySet DAMAGE_IMMUNITY = register(
    "damage_immunity",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .optional(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final ContextKeySet DAMAGE_PROTECTION = register("damage_protection",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .optional(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final ContextKeySet TICK = register("tick",
    builder()
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL)
      .optional(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE)
  );

  public static LootContext attributes(ServerLevel serverLevel, Entity entity, int level) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .create(ATTRIBUTES);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext knockback(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE, damage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(KNOCKBACK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext damage(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE, damage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(DAMAGE);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext heal(ServerLevel serverLevel, Entity entity, int level,@Nullable Damage damage) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE, damage)
      .create(HEAL);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext postAttack(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE, damage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(POST_ATTACK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext damageImmunity(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE, damage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(DAMAGE_IMMUNITY);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext damageProtection(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE, damage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(DAMAGE_PROTECTION);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext tick(ServerLevel serverLevel, Entity entity, int level,@Nullable Damage damage) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.LATEST_DAMAGE, damage)
      .create(TICK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext location(ServerLevel serverLevel, Entity entity, int level, Vec3 origin) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, origin)
      .withParameter(top.theillusivec4.champions.world.loot.parameters.LootContextParams.CHAMPION_LEVEL, level)
      .create(LOCATION);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static LootContext spawn(ServerLevel serverLevel, Entity entity, @Nullable EntitySpawnReason spawnReason, Optional<Identifier> randomSequenceKey) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .create(SPAWN);
    return new LootContext.Builder(params).create(randomSequenceKey);
  }

  private static ContextKeySet register(String name, ContextKeySet.Builder builder) {
    ContextKeySet contextKeySet = builder.build();
    LootContextParamSetsAccessor.getRegistry().put(Util.id(name), contextKeySet);
    return contextKeySet;
  }

  private static ContextKeySet.Builder builder() {
    return new ContextKeySet.Builder();
  }

  private LootContextParamSets() {
  }

}
