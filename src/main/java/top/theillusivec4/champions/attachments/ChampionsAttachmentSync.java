package top.theillusivec4.champions.attachments;

import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.common.util.FriendlyByteBufUtil;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.connection.ConnectionType;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.network.ChampionsSyncAttachmentsPayload;
import top.theillusivec4.champions.registries.ChampionsRegistryCallbacks;
import top.theillusivec4.champions.util.IChunkMapExtension;
import top.theillusivec4.champions.util.IIAttachmentHolderExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class ChampionsAttachmentSync {
  private ChampionsAttachmentSync() {
  }

  private static <T> @Nullable AttachmentSyncHandler<T> getSyncHandler(AttachmentType<T> type) {
    //noinspection unchecked
    return (AttachmentSyncHandler<T>) ChampionsRegistryCallbacks.Attachments.SYNC_HANDLER_MAP.get(type);
  }

  private static @Nullable ChampionsSyncAttachmentsPayload syncInitialAttachments(IAttachmentHolder holder, ServerPlayer to) {
    if (!holder.hasAttachments()) {
      return null;
    }
    IIAttachmentHolderExtension extension = (IIAttachmentHolderExtension) holder;
    boolean anySyncableAttachment = false;
    for (var attachment : extension.champions$getAttachmentMap().keySet()) {
      anySyncableAttachment = anySyncableAttachment | getSyncHandler(attachment) != null;
    }
    if (!anySyncableAttachment) {
      return null;
    }
    Map<AttachmentType<?>, AttachmentSyncHandler<?>> handlers = new HashMap<>();
    var data = FriendlyByteBufUtil.writeCustomData(buf -> {
      for (var entry : extension.champions$getAttachmentMap().entrySet()) {
        AttachmentType<?> attachment = entry.getKey();
        @SuppressWarnings("unchecked")
        var syncHandler = (AttachmentSyncHandler<Object>) getSyncHandler(attachment);
        if (syncHandler != null) {
          int indexBefore = buf.writerIndex();
          buf.writeBoolean(true);
          int indexBetween = buf.writerIndex();
          syncHandler.write(buf, entry.getValue());
          if (indexBetween < buf.writerIndex()) {
            // Actually wrote something
            handlers.put(attachment, syncHandler);

          } else {
            buf.writerIndex(indexBefore);
          }
        }
      }
    }, to.registryAccess());
    return new ChampionsSyncAttachmentsPayload(syncTarget(holder), handlers, data);
  }

  @SubscribeEvent
  private static void onChunkSent(ChunkWatchEvent.Sent event) {
    List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>();
    var chunkPayload = syncInitialAttachments(event.getChunk(), event.getPlayer());
    if (chunkPayload != null) {
      packets.add(chunkPayload.toVanillaClientbound());
    }
    for (var blockEntity : event.getChunk().getBlockEntities().values()) {
      var blockEntityPayload = syncInitialAttachments(blockEntity, event.getPlayer());
      if (blockEntityPayload != null) {
        packets.add(blockEntityPayload.toVanillaClientbound());
      }
    }
    if (!packets.isEmpty()) {
      event.getPlayer().connection.send(new ClientboundBundlePacket(packets));
    }
  }

  public static <T> void syncEntityUpdate(Entity entity, Supplier<AttachmentType<T>> type, Supplier<AttachmentSyncHandler<T>> syncHandler) {
    syncEntityUpdate(entity, type.get(), syncHandler.get());
  }

  public static <T> void syncEntityUpdate(Entity entity, AttachmentType<T> type, AttachmentSyncHandler<T> syncHandler) {
    if (entity.level() instanceof ServerLevel level) {
      List<ServerPlayer> players = getPlayersWatching(level.getChunkSource().chunkMap, entity);
      if (entity instanceof ServerPlayer player) {
        List<ServerPlayer> newPlayers = new ArrayList<>(players.size() + 1);
        newPlayers.addAll(players);
        newPlayers.add(player);
        players = newPlayers;
      }

      syncUpdate(entity, type, syncHandler, players);
    }
  }

  private static List<ServerPlayer> getPlayersWatching(ChunkMap chunkMap, Entity entity) {
    return ((IChunkMapExtension) chunkMap).champions$getPlayersWatching(entity);
//    var entityMap = ((ChunkMapAccessor) chunkMap).getEntityMap();
//    var trackedEntity = ((TrackedEntityAccessor) entityMap.get(entity.getId()));
//    if (trackedEntity != null) {
//      var ret = new java.util.ArrayList<ServerPlayer>(trackedEntity.getSeenBy().size());
//      for (var connection : trackedEntity.getSeenBy()) {
//        ret.add(connection.getPlayer());
//      }
//      return java.util.Collections.unmodifiableList(ret);
//    } else {
//      return List.of();
//    }
  }

  public static <T> void syncUpdate(AttachmentHolder holder, AttachmentType<T> type, AttachmentSyncHandler<T> syncHandler, List<ServerPlayer> players) {
    if (players.isEmpty()) return;

    var registryAccess = players.getFirst().registryAccess();
    var data = FriendlyByteBufUtil.writeCustomData(buf -> {
      var existingData = holder.getExistingData(type);
      if (existingData.isPresent()) {
        buf.writeBoolean(true);
        syncHandler.write(buf, existingData.get());
      } else {
        buf.writeBoolean(false);
      }
    }, registryAccess);
    var packet = new ChampionsSyncAttachmentsPayload(syncTarget(holder), Map.of(type, syncHandler), data);
    for (ServerPlayer player : players) {
      PacketDistributor.sendToPlayer(player, packet);
    }
  }

  private static ChampionsSyncAttachmentsPayload.Target syncTarget(IAttachmentHolder holder) {
    return switch (holder) {
      case BlockEntity blockEntity -> new ChampionsSyncAttachmentsPayload.BlockEntityTarget(blockEntity.getBlockPos());
      case LevelChunk chunk -> new ChampionsSyncAttachmentsPayload.ChunkTarget(chunk.getPos());
      case Entity entity -> new ChampionsSyncAttachmentsPayload.EntityTarget(entity.getId());
      case Level ignored -> new ChampionsSyncAttachmentsPayload.LevelTarget();
      default -> throw new UnsupportedOperationException("Attachment holder class is not supported: " + holder);
    };
  }

  public static void receiveSyncedDataAttachments(IAttachmentHolder holder, RegistryAccess registryAccess, Map<AttachmentType<?>, AttachmentSyncHandler<?>> handlers, byte[] bytes) {
    var buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(bytes), registryAccess, ConnectionType.NEOFORGE);
    try {
      for (Map.Entry<AttachmentType<?>, AttachmentSyncHandler<?>> entry : handlers.entrySet()) {
        @SuppressWarnings("unchecked")
        AttachmentType<Object> attachment = (AttachmentType<Object>) entry.getKey();
        @SuppressWarnings("unchecked")
        AttachmentSyncHandler<Object> handler = (AttachmentSyncHandler<Object>) entry.getValue();
        var value = handler.read(buf);
        holder.setData(attachment, value);
      }
    } catch (Exception exception) {
      throw new RuntimeException("Encountered exception when reading synced data attachments: " + handlers, exception);
    } finally {
      buf.release();
    }
  }
}
