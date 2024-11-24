package top.theillusivec4.champions.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixSetting;

import java.util.HashMap;
import java.util.Map;

public record SyncAffixSettingPacket(
  Map<ResourceLocation, AffixSetting> affixSettingMap) implements CustomPacketPayload {
  public static final StreamCodec<FriendlyByteBuf, SyncAffixSettingPacket> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.map(
      HashMap::new,
      ByteBufCodecs.fromCodec(ResourceLocation.CODEC),
      ByteBufCodecs.fromCodec(AffixSetting.CODEC)
    ),
    SyncAffixSettingPacket::affixSettingMap,    // getter function
    SyncAffixSettingPacket::new    // factory function
  );
  public static final Type<SyncAffixSettingPacket> TYPE = new Type<>(Champions.getLocation("sync_affix_setting"));

  public static void handle(final SyncAffixSettingPacket data, final IPayloadContext cxt) {
    cxt.enqueueWork(() -> {
      if (cxt.flow().isClientbound()) {
        // client cached data from server
        Champions.getDataLoader().cache(data.affixSettingMap);
      }
    });
    cxt.enqueueWork(SyncAffixSettingPacket::handelSettingMainThread);
  }

  /**
   * Apply setting and category map from datapack
   */
  private static void handelSettingMainThread() {
    Champions.getDataLoader().getLoadedData().forEach((resourceLocation, affixSetting) ->
      Champions.API.getAffix(affixSetting.type()).ifPresent(affix -> {
        affix.applySetting(affixSetting);
        Champions.API.addCategory(affix.getCategory(), affix);
      }));
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
