package top.theillusivec4.champions.champion.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.champions.registries.ChampionsBuiltInRegistries;

import java.util.function.Function;

public interface ProjectileProvider {
  Codec<ProjectileProvider> CODEC = ChampionsBuiltInRegistries.PROJECTILE_PROVIDER_TYPE.byNameCodec().dispatch(ProjectileProvider::codec, Function.identity());

  static ProjectileProvider arrow() {
    return ProjectileProviders.ArrowProvider.INSTANCE;
  }

  static ProjectileProvider arcticBullet() {
    return ProjectileProviders.ArcticBulletProvider.INSTANCE;
  }

  static ProjectileProvider enkindlingBullet(){
    return ProjectileProviders.EnkindlingBulletProvider.INSTANCE;
  }

  static ProjectileProvider shulkerBullet() {
    return ProjectileProviders.ShulkerBulletProvider.INSTANCE;
  }

  static ProjectileProvider fireworkRocket() {
    return ProjectileProviders.FireworkRocketProvider.INSTANCE;
  }

  static ProjectileProvider smallFireball() {
    return ProjectileProviders.SmallFireballProvider.INSTANCE;
  }

  static ProjectileProvider largeFireball() {
    return ProjectileProviders.LargeFireballProvider.INSTANCE;
  }

  static ProjectileProvider thrownSplashPotion() {
    return ProjectileProviders.ThrownSplashPotionProvider.INSTANCE;
  }

  static ProjectileProvider thrownLingeringPotion() {
    return ProjectileProviders.ThrownLingeringPotionProvider.INSTANCE;
  }

  Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem);

  MapCodec<? extends ProjectileProvider> codec();
}
