package top.theillusivec4.champions.world.damagesource;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import top.theillusivec4.champions.util.Util;

public final class ChampionsDamageTypes {
  public static final ResourceKey<DamageType> REFLECTION_DAMAGE = register("reflection");
  public static final ResourceKey<DamageType> ENKINDLING_BULLET = register("enkindling_bullet");

  private ChampionsDamageTypes() {
  }

  public static void bootstrap(BootstrapContext<DamageType> context) {
    context.register(REFLECTION_DAMAGE, new DamageType("reflection", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
    context.register(ENKINDLING_BULLET, new DamageType("enkindling_bullet", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F, DamageEffects.BURNING));
  }

  private static ResourceKey<DamageType> register(String name) {
    return ResourceKey.create(Registries.DAMAGE_TYPE, Util.id(name));
  }
}
