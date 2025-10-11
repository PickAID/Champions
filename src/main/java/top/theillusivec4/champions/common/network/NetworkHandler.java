package top.theillusivec4.champions.common.network;


import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.Color;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.util.Utils;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class NetworkHandler {

    private static final String PTC_VERSION = "1";

    public static SimpleChannel INSTANCE;

    private static int id = 0;

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder.named(Utils.getLocation("main"))
                .networkProtocolVersion(() -> PTC_VERSION).clientAcceptedVersions(PTC_VERSION::equals)
                .serverAcceptedVersions(PTC_VERSION::equals).simpleChannel();

        register(SPacketSyncChampion.class, SPacketSyncChampion::encode, SPacketSyncChampion::decode,
                SPacketSyncChampion::handle);
        register(SPacketSyncAffixData.class, SPacketSyncAffixData::encode, SPacketSyncAffixData::decode,
                SPacketSyncAffixData::handle);
        register(SPacketSyncAffixSetting.class, SPacketSyncAffixSetting::encode, SPacketSyncAffixSetting::decode,
                SPacketSyncAffixSetting::handle);
    }

    private static <M> void register(Class<M> messageType, BiConsumer<M, PacketBuffer> encoder,
                                     Function<PacketBuffer, M> decoder,
                                     BiConsumer<M, Supplier<NetworkEvent.Context>> messageConsumer) {
        INSTANCE.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
    }

    public static void syncChampionDataToPlayerTrackingEntity(IChampion.Server championData, LivingEntity targetEntity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> targetEntity),
                new SPacketSyncChampion(targetEntity.getId(),
                        championData.getRank().map(Rank::getTier).orElse(0),
                        championData.getRank().map(Rank::getDefaultColor).orElse(Color.fromRgb(0)).serialize(),
                        championData.getAffixes().stream().map(IAffix::getIdentifier)
                                .collect(Collectors.toSet())));
    }
}
