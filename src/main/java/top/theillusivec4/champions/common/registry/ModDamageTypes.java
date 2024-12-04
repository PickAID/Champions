package top.theillusivec4.champions.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import top.theillusivec4.champions.Champions;

public class ModDamageTypes {
  public static final ResourceKey<DamageType> REFLECTION_DAMAGE = create("reflection");
  public static final ResourceKey<DamageType> ENKINDLING_BULLET = create("enkindling_bullet");

  public static DamageSource of(Level level, ResourceKey<DamageType> key) {
    return new DamageSource(level.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
  }

  public static DamageSource of(ResourceKey<DamageType> key, Entity directEntity) {
    return new DamageSource(directEntity.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key), directEntity);
  }

  public static DamageSource of(ResourceKey<DamageType> key, Entity directEntity, Entity causingEntity) {
    return new DamageSource(causingEntity.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key), directEntity, causingEntity);
  }

  /**
   * for data generation
   */
  public static void bootstrap(BootstrapContext<DamageType> context) {
    context.register(REFLECTION_DAMAGE, new DamageType("reflection", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
    context.register(ENKINDLING_BULLET, new DamageType("enkindling_bullet", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F, DamageEffects.BURNING));
  }

  private static ResourceKey<DamageType> create(String name) {
    return ResourceKey.create(Registries.DAMAGE_TYPE, Champions.getLocation(name));
  }
}
