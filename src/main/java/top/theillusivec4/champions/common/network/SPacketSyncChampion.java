package top.theillusivec4.champions.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capabilities.ChampionAttachment;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.util.Utils;

import java.util.Set;
import java.util.stream.Collectors;

@Deprecated
public record SPacketSyncChampion(int entityId, int tier, String defaultColor,
                                  Set<Identifier> affixes) implements CustomPacketPayload {

  public static final Type<SPacketSyncChampion> TYPE = new Type<>(Utils.id("sync_champion"));

  public static final StreamCodec<FriendlyByteBuf, SPacketSyncChampion> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT,
    SPacketSyncChampion::entityId,
    ByteBufCodecs.INT,
    SPacketSyncChampion::tier,
    ByteBufCodecs.STRING_UTF8,
    SPacketSyncChampion::defaultColor,
    ByteBufCodecs.fromCodec(NeoForgeExtraCodecs.setOf(Identifier.CODEC)), SPacketSyncChampion::affixes, SPacketSyncChampion::new
  );

  public static void handle(final SPacketSyncChampion data, final IPayloadContext cxt) {
    cxt.enqueueWork(() -> {
      if (cxt.flow().isClientbound()) {
        var level = cxt.player().level();
        Entity entity = level.getEntity(data.entityId);
        assert entity != null;
        ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
          IChampion.Client clientChampion = champion.getClient();
          clientChampion.setRank(new Tuple<>(data.tier, data.defaultColor));
          clientChampion.setAffixes(data.affixes);
        });
      }
    });
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void syncChampionDataToPlayerTrackingEntity(IChampion.Server championData, LivingEntity targetEntity) {
    PacketDistributor.sendToPlayersTrackingEntity(targetEntity,
      new SPacketSyncChampion(targetEntity.getId(),
        championData.getRank().map(Rank::getTier).orElse(0),
        championData.getRank().map(Rank::getDefaultColor).orElse(TextColor.fromRgb(0)).toString(),
        championData.getAffixes().stream().map(IAffix::getIdentifier).collect(Collectors.toSet())));
  }
}
