package top.theillusivec4.champions.mixin;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.network.ServerPlayerConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(value = ChunkMap.TrackedEntity.class)
public interface TrackedEntityAccessor {
  @Accessor(value = "seenBy")
  Set<ServerPlayerConnection> getSeenBy();
}
