package top.theillusivec4.champions.champion.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.champions.registry.BuiltInRegistries;

import java.util.function.Function;

public interface ProjectileProvider {
  Codec<ProjectileProvider> CODEC = BuiltInRegistries.PROJECTILE_PROVIDER_TYPE.byNameCodec().dispatch(ProjectileProvider::codec, Function.identity());

  static ProjectileProvider arrow() {
    return Projectiles.ArrowProvider.INSTANCE;
  }

  static ProjectileProvider arcticBullet() {
    return Projectiles.ArcticBulletProvider.INSTANCE;
  }

  static ProjectileProvider enkindlingBullet(){
    return Projectiles.EnkindlingBulletProvider.INSTANCE;
  }

  static ProjectileProvider shulkerBullet() {
    return Projectiles.ShulkerBulletProvider.INSTANCE;
  }

  static ProjectileProvider fireworkRocket() {
    return Projectiles.FireworkRocketProvider.INSTANCE;
  }

  static ProjectileProvider smallFireball() {
    return Projectiles.SmallFireballProvider.INSTANCE;
  }

  static ProjectileProvider largeFireball() {
    return Projectiles.LargeFireballProvider.INSTANCE;
  }

  static ProjectileProvider thrownSplashPotion() {
    return Projectiles.ThrownSplashPotionProvider.INSTANCE;
  }

  static ProjectileProvider thrownLingeringPotion() {
    return Projectiles.ThrownLingeringPotionProvider.INSTANCE;
  }

  Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem);

  MapCodec<? extends ProjectileProvider> codec();
}
