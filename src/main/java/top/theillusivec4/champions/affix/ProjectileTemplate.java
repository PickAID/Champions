package top.theillusivec4.champions.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.champions.registries.ChampionsBuiltInRegistries;

import java.util.function.Function;

public interface ProjectileTemplate {
  Codec<ProjectileTemplate> CODEC = Codec.lazyInitialized(() -> ChampionsBuiltInRegistries.PROJECTILE_TEMPLATE_TYPE.byNameCodec().dispatch(ProjectileTemplate::codec, Function.identity()));

  static ProjectileTemplate arrow() {
    return ProjectileTemplates.ArrowProvider.INSTANCE;
  }

  static ProjectileTemplate arcticBullet() {
    return ProjectileTemplates.ArcticBulletProvider.INSTANCE;
  }

  static ProjectileTemplate enkindlingBullet() {
    return ProjectileTemplates.EnkindlingBulletProvider.INSTANCE;
  }

  static ProjectileTemplate shulkerBullet() {
    return ProjectileTemplates.ShulkerBulletProvider.INSTANCE;
  }

  static ProjectileTemplate fireworkRocket() {
    return ProjectileTemplates.FireworkRocketProvider.INSTANCE;
  }

  static ProjectileTemplate smallFireball() {
    return ProjectileTemplates.SmallFireballProvider.INSTANCE;
  }

  static ProjectileTemplate largeFireball() {
    return ProjectileTemplates.LargeFireballProvider.INSTANCE;
  }

  static ProjectileTemplate thrownSplashPotion() {
    return ProjectileTemplates.ThrownSplashPotionProvider.INSTANCE;
  }

  static ProjectileTemplate thrownLingeringPotion() {
    return ProjectileTemplates.ThrownLingeringPotionProvider.INSTANCE;
  }

  Projectile create(ServerLevel level, Entity source, ItemStack item);

  MapCodec<? extends ProjectileTemplate> codec();
}
