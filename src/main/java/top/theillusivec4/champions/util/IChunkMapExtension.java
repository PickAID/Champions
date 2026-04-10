package top.theillusivec4.champions.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.List;

public interface IChunkMapExtension {
  List<ServerPlayer> champions$getPlayersWatching(Entity entity);
}
