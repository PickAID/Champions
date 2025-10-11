package top.theillusivec4.champions.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.util.ChampionHelper;

import java.util.Optional;

public class MouseHelper {

    public static Optional<LivingEntity> getMouseOverChampion(Minecraft mc, float partialTicks) {
        Entity entity = mc.getCameraEntity();

        if (entity != null) {

            if (mc.level != null) {
                mc.getProfiler().push("mouse_champion");
                double d0 = ClientChampionsConfig.hudRange;
                RayTraceResult rayTraceResult = entity.pick(d0, partialTicks, false);
                Vector3d vec3d = entity.getEyePosition(partialTicks);
                double d1 = rayTraceResult.getLocation().distanceToSqr(vec3d);
	            Vector3d vec3d1 = entity.getViewVector(1.0F);
	            Vector3d vec3d2 = vec3d.add(vec3d1.x * d0, vec3d1.y * d0, vec3d1.z * d0);
                AxisAlignedBB axisalignedbb =
                        entity.getBoundingBox().expandTowards(vec3d1.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
                EntityRayTraceResult entityraytraceresult =
                        ProjectileHelper.getEntityHitResult(entity, vec3d, vec3d2, axisalignedbb,
                                (e) -> !e.isSpectator() && e.isPickable(), d1);

                if (entityraytraceresult != null) {
                    Entity entity1 = entityraytraceresult.getEntity();
                    Optional<IChampion> champion = ChampionCapability.getCapability(entity1).resolve();
                    if (champion.isPresent() && ChampionHelper.isValidChampion(champion.get().getClient()))
                        return champion.map(IChampion::getLivingEntity);
                }
                mc.getProfiler().pop();
            }
        }
        return Optional.empty();
    }
}
