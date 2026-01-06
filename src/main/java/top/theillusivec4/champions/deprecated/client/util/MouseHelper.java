package top.theillusivec4.champions.deprecated.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.deprecated.common.capabilities.ChampionAttachment;
import top.theillusivec4.champions.deprecated.common.util.ChampionHelper;

import java.util.Optional;

public class MouseHelper {

  public static Optional<LivingEntity> getMouseOverChampion(Minecraft mc, float partialTicks) {
    Entity entity = mc.getCameraEntity();

    if (entity instanceof LivingEntity) {

      if (mc.level != null) {
        Profiler.get().push("mouse_champion");
        double d0 = ClientChampionsConfig.hudRange;
        HitResult rayTraceResult = entity.pick(d0, partialTicks, false);
        Vec3 vec3d = entity.getEyePosition(partialTicks);
        double d1 = rayTraceResult.getLocation().distanceToSqr(vec3d);
        Vec3 vec3d1 = entity.getViewVector(1.0F);
        Vec3 vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
        AABB axisalignedbb =
          entity.getBoundingBox().expandTowards(vec3d1.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
        EntityHitResult entityraytraceresult =
          ProjectileUtil.getEntityHitResult(entity, vec3d, vec3d2, axisalignedbb,
            (e) -> !e.isSpectator() && e.isPickable(), d1);

        if (entityraytraceresult != null) {
          Entity entity1 = entityraytraceresult.getEntity();
          var champion = ChampionAttachment.getAttachment(entity1);
          if (champion.isPresent() && ChampionHelper.isValidChampion(champion.get().getClient()))
            return champion.map(IChampion::getLivingEntity);
        }
        Profiler.get().pop();
      }
    }
    return Optional.empty();
  }
}
