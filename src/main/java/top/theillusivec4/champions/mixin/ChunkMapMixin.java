package top.theillusivec4.champions.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.theillusivec4.champions.util.IChunkMapExtension;

import java.util.Collections;
import java.util.List;

@Mixin(value = ChunkMap.class)
public abstract class ChunkMapMixin implements IChunkMapExtension {
  @Shadow
  @Final
  private Int2ObjectMap<ChunkMap.TrackedEntity> entityMap;

  @Override
  public List<ServerPlayer> champions$getPlayersWatching(Entity entity) {
    var trackedEntity = (TrackedEntityAccessor) (this.entityMap.get(entity.getId()));
    if (trackedEntity != null) {
      var ret = new java.util.ArrayList<ServerPlayer>(trackedEntity.getSeenBy().size());
      for (var connection : trackedEntity.getSeenBy()) {
        ret.add(connection.getPlayer());
      }
      return Collections.unmodifiableList(ret);
    } else {
      return List.of();
    }
  }
}
