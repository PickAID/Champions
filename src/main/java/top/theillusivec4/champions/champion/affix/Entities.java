package top.theillusivec4.champions.champion.affix;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class Entities {
  public record AreaEffectCloudProvider() implements EntityProvider{

    @Override
    public Entity provide(ServerLevel level, Vec3 position) {
      AreaEffectCloud areaEffectCloud = new AreaEffectCloud(level, position.x(), position.y(), position.z());

      return null;
    }

    @Override
    public MapCodec<? extends EntityProvider> codec() {
      return null;
    }
  }
  private Entities() {
  }
}
