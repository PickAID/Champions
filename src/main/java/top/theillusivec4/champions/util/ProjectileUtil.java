package top.theillusivec4.champions.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public final class ProjectileUtil {
  private ProjectileUtil() {
  }

  public static <T extends Projectile> void spawnProjectileUsingShoot(T projectile, ServerLevel level, ItemStack item, double targetX, double targetY, double targetZ, float pow, float uncertainty) {
    spawnProjectile(projectile, level, item, t -> t.shoot(targetX, targetY, targetZ, pow, uncertainty));
  }

  public static <T extends Projectile> void spawnProjectile(T projectile, ServerLevel serverLevel, ItemStack itemStack, Consumer<T> shootFunction) {
    shootFunction.accept(projectile);
    serverLevel.addFreshEntity(projectile);
  }
}
