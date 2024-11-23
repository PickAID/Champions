package top.theillusivec4.champions.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionAttachment;

import java.util.Set;

public record SPacketSyncChampion(int entityId, int tier, String defaultColor,
                                  Set<ResourceLocation> affixes) implements CustomPacketPayload {

  public static final Type<SPacketSyncChampion> TYPE = new Type<>(Champions.getLocation("sync_champion"));

  public static final StreamCodec<FriendlyByteBuf, SPacketSyncChampion> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT,
    SPacketSyncChampion::entityId,
    ByteBufCodecs.INT,
    SPacketSyncChampion::tier,
    ByteBufCodecs.STRING_UTF8,
    SPacketSyncChampion::defaultColor,
    ByteBufCodecs.fromCodec(NeoForgeExtraCodecs.setOf(ResourceLocation.CODEC)), SPacketSyncChampion::affixes, SPacketSyncChampion::new
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
  @NotNull
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
