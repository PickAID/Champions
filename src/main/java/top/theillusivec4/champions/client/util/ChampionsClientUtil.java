package top.theillusivec4.champions.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public final class ChampionsClientUtil {
  private ChampionsClientUtil() {
  }

  public static Entity getMouseEntity(float partialTicks) {
    if (Minecraft.getInstance().player != null) {
      return getMouseEntity(Minecraft.getInstance().player.entityInteractionRange(), partialTicks);
    }

    return null;
  }

  public static Entity getMouseEntity(double maxDistance, float partialTicks) {
    Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
    if (cameraEntity != null) {
      Vec3 from = cameraEntity.getEyePosition(partialTicks);
      Vec3 direction = cameraEntity.getViewVector(partialTicks);
      double maxDistanceSqr = maxDistance * maxDistance;
      Vec3 to = from.add(direction.x * maxDistance, direction.y * maxDistance, direction.z * maxDistance);
      AABB box = cameraEntity.getBoundingBox().expandTowards(direction.scale(maxDistance)).inflate(1.0, 1.0, 1.0);
      EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(cameraEntity, from, to, box, Entity::isPickable, maxDistanceSqr);
      return entityHitResult != null ? entityHitResult.getEntity() : null;
    }
    return null;
  }
}
