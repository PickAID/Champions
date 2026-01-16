package top.theillusivec4.champions.deprecated.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;
import top.theillusivec4.champions.util.Util;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public record SyncAffixSettingPacket(
  Map<Identifier, AffixSetting> affixSettingMap) implements CustomPacketPayload {
  public static final StreamCodec<FriendlyByteBuf, SyncAffixSettingPacket> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.map(
      HashMap::new,
      ByteBufCodecs.fromCodec(Identifier.CODEC),
      ByteBufCodecs.fromCodec(AffixSetting.CODEC)
    ),
    SyncAffixSettingPacket::affixSettingMap,    // getter function
    SyncAffixSettingPacket::new    // factory function
  );
  public static final Type<SyncAffixSettingPacket> TYPE = new Type<>(Util.id("sync_affix_setting"));

  public static void handle(final SyncAffixSettingPacket data, final IPayloadContext cxt) {
    cxt.enqueueWork(() -> {
      if (cxt.flow().isClientbound()) {
        // client cached data from server
        Champions.API.getAffixDataLoader().cache(data.affixSettingMap);
      }
    });
    cxt.enqueueWork(SyncAffixSettingPacket::handelSettingMainThread);
  }

  /**
   * Apply setting and category map from datapack
   */
  public static void handelSettingMainThread() {
    Champions.API.getAffixDataLoader().getLoadedData().forEach((resourceLocation, affixSetting) ->
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
