package top.theillusivec4.champions.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.world.level.storage.loot.ExtraLootParamsHelper;
import top.theillusivec4.champions.world.level.storage.loot.parameters.ChampionsLootContextParamSets;
import top.theillusivec4.champions.world.level.storage.loot.parameters.ChampionsLootContextParams;

import java.util.Optional;

public final class LootContextFactory {
  private LootContextFactory() {
  }

  public static LootContext affixedDamage(ServerLevel serverLevel, Entity entity, DamageSource damageSource, int affixLevel, @Nullable Entity directAttackingEntity, @Nullable Entity attackingEntity) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel)
      .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, directAttackingEntity)
      .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, attackingEntity);
    ExtraLootParamsHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.AFFIXED_DAMAGE);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext affixedEntity(ServerLevel serverLevel, Entity entity, int affixLevel) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, entity.position())
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel);
    ExtraLootParamsHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.AFFIXED_ENTITY);
    return new LootContext.Builder(params).create(Optional.empty());
  }

  public static LootContext affixedLocation(ServerLevel serverLevel, Entity entity, Vec3 origin, int affixLevel) {
    LootParams.Builder builder = new LootParams.Builder(serverLevel)
      .withParameter(LootContextParams.THIS_ENTITY, entity)
      .withParameter(LootContextParams.ORIGIN, origin)
      .withParameter(ChampionsLootContextParams.AFFIX_LEVEL, affixLevel);
    ExtraLootParamsHelper.withParameters(entity, builder);
    LootParams params = builder.create(ChampionsLootContextParamSets.AFFIXED_LOCATION);
    return new LootContext.Builder(params).create(Optional.empty());
  }

}
