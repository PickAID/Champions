package top.theillusivec4.champions.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ChunkMap.class)
public interface ChunkMapAccessor {

  @Accessor(value = "entityMap")
  Int2ObjectMap<ChunkMap.TrackedEntity> getEntityMap();
}
