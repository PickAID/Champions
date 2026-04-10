package top.theillusivec4.champions.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import top.theillusivec4.champions.attachments.AttachmentSyncHandler;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.ChampionsUtil;

import java.util.HashMap;
import java.util.Map;

public record ChampionsSyncAttachmentsPayload(
  Target target,
  Map<AttachmentType<?>, AttachmentSyncHandler<?>> handlers,
  byte[] syncPayload
) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<ChampionsSyncAttachmentsPayload> TYPE = new Type<>(ChampionsUtil.id("sync_attachment"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ChampionsSyncAttachmentsPayload> STREAM_CODEC = StreamCodec.composite(
    Target.STREAM_CODEC,
    ChampionsSyncAttachmentsPayload::target,
    ByteBufCodecs.map(HashMap::new, ByteBufCodecs.fromCodec(NeoForgeRegistries.ATTACHMENT_TYPES.byNameCodec()), ByteBufCodecs.fromCodec(ChampionsRegistries.ATTACHMENT_SYNC_HANDLER.byNameCodec())),
    ChampionsSyncAttachmentsPayload::handlers,
    ByteBufCodecs.BYTE_ARRAY,
    ChampionsSyncAttachmentsPayload::syncPayload,
    ChampionsSyncAttachmentsPayload::new
  );

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public sealed interface Target {
    StreamCodec<RegistryFriendlyByteBuf, Target> STREAM_CODEC = StreamCodec.of(
      (buf, target) -> {
        switch (target) {
          case BlockEntityTarget(var pos) -> {
            buf.writeByte(0);
            buf.writeBlockPos(pos);
          }
          case ChunkTarget(var pos) -> {
            buf.writeByte(1);
            buf.writeChunkPos(pos);
          }
          case EntityTarget(var entityId) -> {
            buf.writeByte(2);
            buf.writeVarInt(entityId);
          }
          case LevelTarget() -> {
            buf.writeByte(3);
          }
        }
      },
      buf -> {
        int type = buf.readByte();
        switch (type) {
          case 0 -> {
            return new BlockEntityTarget(buf.readBlockPos());
          }
          case 1 -> {
            return new ChunkTarget(buf.readChunkPos());
          }
          case 2 -> {
            return new EntityTarget(buf.readVarInt());
          }
          case 3 -> {
            return new LevelTarget();
          }
          default -> throw new IllegalArgumentException("Unknown target type: " + type);
        }
      }
    );
  }

  public record BlockEntityTarget(BlockPos pos) implements Target {

  }

  public record ChunkTarget(ChunkPos pos) implements Target {

  }

  public record EntityTarget(int entity) implements Target {

  }

  public record LevelTarget() implements Target {

  }
}
