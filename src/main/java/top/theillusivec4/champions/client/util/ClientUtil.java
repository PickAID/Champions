package top.theillusivec4.champions.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class ClientUtil {
  public static Minecraft getClient() {
    // 受加载机制影响 Minecraft实例不能作为依赖通过构造函数注入
    return Objects.requireNonNull(Minecraft.getInstance(), "过早的访问 Minecraft 实例");
  }

  public static @Nullable Entity getMouseEntity(double maxDistance, float partialTicks) {
    Entity cameraEntity = getClient().getCameraEntity();
    if (cameraEntity != null) {
      Vec3 from = cameraEntity.getEyePosition(partialTicks);
      Vec3 direction = cameraEntity.getViewVector(partialTicks);
      double maxDistanceSqr = maxDistance * maxDistance;
      Vec3 to = from.add(direction.x * maxDistance, direction.y * maxDistance, direction.z * maxDistance);
      AABB box = cameraEntity.getBoundingBox().expandTowards(direction.scale(maxDistance)).inflate(1.0, 1.0, 1.0);
      EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(cameraEntity, from, to, box, EntitySelector.CAN_BE_PICKED, maxDistanceSqr);
      return entityHitResult != null ? entityHitResult.getEntity() : null;
    }
    return null;
  }

  public static @Nullable Entity getMouseEntity(float partialTicks) {
    if (getClient().player != null) {
      return getMouseEntity(getClient().player.entityInteractionRange(), partialTicks);
    }

    return null;
  }

  private ClientUtil() {
  }
}
