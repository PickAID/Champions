package top.theillusivec4.champions.client.network;

import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.attachments.ChampionsAttachmentSync;
import top.theillusivec4.champions.network.ChampionsSyncAttachmentsPayload;

public final class ChampionsClientPayloadHandler {
  private ChampionsClientPayloadHandler() {
  }

  public static void handleSyncAttachment(ChampionsSyncAttachmentsPayload payload, IPayloadContext context) {
    switch (payload.target()) {
      case ChampionsSyncAttachmentsPayload.BlockEntityTarget(var pos) -> {
        var blockEntity = context.player().level().getBlockEntity(pos);
        if (blockEntity == null) {
          ChampionsMod.LOGGER.warn("Received synced attachments from unknown block entity");
        } else {
          ChampionsAttachmentSync.receiveSyncedDataAttachments(blockEntity, context.player().registryAccess(), payload.handlers(), payload.syncPayload());
        }
      }
      case ChampionsSyncAttachmentsPayload.ChunkTarget(var pos) -> {
        var chunk = context.player().level().getChunk(pos.x, pos.z, ChunkStatus.FULL, false);
        if (chunk == null) {
          ChampionsMod.LOGGER.warn("Received synced attachments from unknown chunk");
        } else {
          ChampionsAttachmentSync.receiveSyncedDataAttachments(chunk, chunk.getLevel().registryAccess(), payload.handlers(), payload.syncPayload());
        }
      }
      case ChampionsSyncAttachmentsPayload.EntityTarget(var entityId) -> {
        var entity = context.player().level().getEntity(entityId);
        if (entity == null) {
          ChampionsMod.LOGGER.warn("Received synced attachments from unknown entity");
        } else {
          ChampionsAttachmentSync.receiveSyncedDataAttachments(entity, entity.registryAccess(), payload.handlers(), payload.syncPayload());
        }
      }
      case ChampionsSyncAttachmentsPayload.LevelTarget() -> {
        ChampionsAttachmentSync.receiveSyncedDataAttachments(context.player().level(), context.player().registryAccess(), payload.handlers(), payload.syncPayload());
      }
    }
  }
}
