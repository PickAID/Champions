package top.theillusivec4.champions.deprecated.common.network;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.common.capability.ChampionAttachment;

import java.util.Set;

public record SPacketSyncChampion(int entityId, int tier, int defaultColor,
                                  Set<String> affixes) implements CustomPacketPayload {

  public static final Type<SPacketSyncChampion> TYPE = new Type<>(ChampionsMod.getLocation("sync_champion"));

  public static final StreamCodec<FriendlyByteBuf, SPacketSyncChampion> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT,
    SPacketSyncChampion::entityId,
    ByteBufCodecs.INT,
    SPacketSyncChampion::tier,
    ByteBufCodecs.INT,
    SPacketSyncChampion::defaultColor,
    ByteBufCodecs.fromCodec(NeoForgeExtraCodecs.setOf(Codec.STRING)), SPacketSyncChampion::affixes, SPacketSyncChampion::new
  );

  public static void handle(final SPacketSyncChampion data, final IPayloadContext cxt) {
    cxt.enqueueWork(() -> {
      ClientLevel world = Minecraft.getInstance().level;

      if (world != null) {
        Entity entity = world.getEntity(data.entityId);
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
