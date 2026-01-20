package top.theillusivec4.champions.champion.affix;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface EntityProvider {
  Entity provide(ServerLevel level, Vec3 position);

  MapCodec<? extends EntityProvider> codec();
}
