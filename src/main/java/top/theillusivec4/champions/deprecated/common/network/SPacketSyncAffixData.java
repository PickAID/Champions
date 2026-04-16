package top.theillusivec4.champions.deprecated.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.common.capability.ChampionAttachment;

public record SPacketSyncAffixData(int entityId, String affixId, CompoundTag data) implements CustomPacketPayload {

  public static final Type<SPacketSyncAffixData> TYPE = new Type<>(Champions.getLocation("main"));

  public static final StreamCodec<FriendlyByteBuf, SPacketSyncAffixData> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.VAR_INT,
    SPacketSyncAffixData::entityId,
    ByteBufCodecs.STRING_UTF8,
    SPacketSyncAffixData::affixId,
    ByteBufCodecs.COMPOUND_TAG,
    SPacketSyncAffixData::data,
    SPacketSyncAffixData::new
  );

  public static void handle(final SPacketSyncAffixData data, final IPayloadContext cxt) {
    cxt.enqueueWork(() -> {
      ClientLevel world = Minecraft.getInstance().level;

      if (world != null) {
        Entity entity = world.getEntity(data.entityId);
        ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
          IChampion.Client clientChampion = champion.getClient();
          clientChampion.getAffix(data.affixId)
            .ifPresent(affix -> affix.readSyncTag(champion, data.data));
        });
      }
    });
  }

  @Override
  @NotNull
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}

