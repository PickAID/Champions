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
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.damage.Damage;
import top.theillusivec4.champions.world.loot.ChampionsLootContextParamSets;
import top.theillusivec4.champions.world.loot.ChampionsLootContextParams;

import java.util.Optional;

public final class ChampionsUtil {
  private ChampionsUtil() {
  }

  public static ResourceLocation id(String name) {
    return ResourceLocation.fromNamespaceAndPath(ChampionsMod.MOD_ID, name);
  }

  public static String makeDescriptionId(String prefix, ResourceLocation id) {
    return makeDescriptionId(prefix, id, null);
  }

  public static String makeDescriptionId(String prefix, ResourceLocation id, @Nullable String suffix) {
    if (suffix != null) {
      return id.toLanguageKey(prefix, suffix);
    } else {
      return id.toLanguageKey(prefix);
    }
  }

  public static LootContext createAttributesContext(ServerLevel serverLevel, Entity entity, int level) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(ChampionsLootContextParams.CHAMPION_LEVEL, level)
      .create(ChampionsLootContextParamSets.ATTRIBUTES);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext createKnockbackContext(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(ChampionsLootContextParams.LATEST_DAMAGE, damage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(ChampionsLootContextParamSets.KNOCKBACK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext createDamageContext(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(ChampionsLootContextParams.LATEST_DAMAGE, damage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(ChampionsLootContextParamSets.DAMAGE);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext createHealContext(ServerLevel serverLevel, Entity entity, int level, @Nullable Damage damage) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(ChampionsLootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(ChampionsLootContextParams.LATEST_DAMAGE, damage)
      .create(ChampionsLootContextParamSets.HEAL);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext createPostAttackContext(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(ChampionsLootContextParams.LATEST_DAMAGE, damage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(ChampionsLootContextParamSets.POST_ATTACK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext createDamageImmunityContext(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(ChampionsLootContextParams.LATEST_DAMAGE, damage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(ChampionsLootContextParamSets.DAMAGE_IMMUNITY);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext createDamageProtectionContext(ServerLevel serverLevel, Entity entity, int level, DamageSource damageSource, @Nullable Damage damage, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(ChampionsLootContextParams.LATEST_DAMAGE, damage)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity)
      .create(ChampionsLootContextParamSets.DAMAGE_PROTECTION);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext createTickContext(ServerLevel serverLevel, Entity entity, int level, @Nullable Damage damage) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(ChampionsLootContextParams.CHAMPION_LEVEL, level)
      .withOptionalParameter(ChampionsLootContextParams.LATEST_DAMAGE, damage)
      .create(ChampionsLootContextParamSets.TICK);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext createLocationContext(ServerLevel serverLevel, Entity entity, int level, Vec3 origin) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, origin)
      .withParameter(ChampionsLootContextParams.CHAMPION_LEVEL, level)
      .create(ChampionsLootContextParamSets.LOCATION);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static LootContext createSpawnContext(ServerLevel serverLevel, Entity entity, @Nullable MobSpawnType spawnReason, Optional<ResourceLocation> randomSequenceKey) {
    LootParams params = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .create(ChampionsLootContextParamSets.SPAWN);
    return new LootContext.Builder(params).create(randomSequenceKey);
  }
}
