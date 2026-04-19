package top.theillusivec4.champions.api.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.world.entity.affix.ProjectileTemplates;

import java.util.function.Function;

public interface ProjectileTemplate {
  Codec<ProjectileTemplate> CODEC = ChampionsBuiltInRegistries.PROJECTILE_TEMPLATE_TYPE.byNameCodec().dispatch(ProjectileTemplate::codec, Function.identity());

  static ProjectileTemplate arrow() {
    return ProjectileTemplates.ArrowTemplate.INSTANCE;
  }

  static ProjectileTemplate arcticBullet() {
    return ProjectileTemplates.ArcticBulletTemplate.INSTANCE;
  }

  static ProjectileTemplate enkindlingBullet(){
    return ProjectileTemplates.EnkindlingBulletTemplate.INSTANCE;
  }

  static ProjectileTemplate shulkerBullet() {
    return ProjectileTemplates.ShulkerBulletTemplate.INSTANCE;
  }

  static ProjectileTemplate fireworkRocket() {
    return ProjectileTemplates.FireworkRocketTemplate.INSTANCE;
  }

  static ProjectileTemplate smallFireball() {
    return ProjectileTemplates.SmallFireballTemplate.INSTANCE;
  }

  static ProjectileTemplate largeFireball() {
    return ProjectileTemplates.LargeFireballTemplate.INSTANCE;
  }

  static ProjectileTemplate thrownSplashPotion() {
    return ProjectileTemplates.ThrownSplashPotionTemplate.INSTANCE;
  }

  static ProjectileTemplate thrownLingeringPotion() {
    return ProjectileTemplates.ThrownLingeringPotionTemplate.INSTANCE;
  }

  Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem);

  MapCodec<? extends ProjectileTemplate> codec();
}
