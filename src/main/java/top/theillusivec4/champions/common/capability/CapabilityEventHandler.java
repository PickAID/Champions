package top.theillusivec4.champions.common.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.network.NetworkHandler;
import top.theillusivec4.champions.common.network.SPacketSyncChampion;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.util.ChampionBuilder;
import top.theillusivec4.champions.common.util.ChampionHelper;

import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class CapabilityEventHandler {

    @SubscribeEvent
    public void attachCapabilities(final AttachCapabilitiesEvent<Entity> evt) {
        Entity entity = evt.getObject();

        if (ChampionHelper.isValidChampionEntity(entity)) {
	        ChampionCapability.Provider provider = ChampionCapability.createProvider((LivingEntity) entity);
	        evt.addCapability(ChampionCapability.ID, provider);
	        evt.addListener(provider::invalidate);
        }
    }

    /**
     * Preset of special spawn ranks
     * @param evt the finalizeSpawn event
     */
    @SubscribeEvent
    public void onSpecialSpawn(LivingSpawnEvent.SpecialSpawn evt) {
        LivingEntity entity = evt.getEntityLiving();

        if (!entity.level.isClientSide()) {
            ChampionCapability.getCapability(entity).ifPresent(champion -> {
                IChampion.Server serverChampion = champion.getServer();

                if (!serverChampion.getRank().isPresent()) {
                    // Todo: Custom entity spawn rank base on mob spawn type
                    if (!ChampionsConfig.championSpawners && evt.getSpawnReason() == SpawnReason.SPAWNER) {
                        serverChampion.setRank(RankManager.getLowestRank());
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public void onLivingConvert(LivingConversionEvent.Post evt) {
        LivingEntity entity = evt.getEntityLiving();

        if (!entity.level.isClientSide()) {
	        ChampionCapability.Provider provider = ChampionCapability.createProvider(entity);
            LivingEntity outcome = evt.getOutcome();
            ChampionCapability.getCapability(entity).ifPresent(
                    oldChampion -> {
                        if (ChampionHelper.isValidChampion(oldChampion.getServer())) {
                            ChampionCapability.getCapability(outcome)
                                    .ifPresent(newChampion -> {
                                        ChampionBuilder.copy(oldChampion, newChampion);
                                        IChampion.Server serverChampion = newChampion.getServer();
                                        NetworkHandler.syncChampionDataToPlayerTrackingEntity(serverChampion, outcome);
                                    });
                        }
                    });
        }
    }

    @SubscribeEvent
    public void startTracking(PlayerEvent.StartTracking evt) {
        Entity entity = evt.getTarget();
        PlayerEntity playerEntity = evt.getPlayer();

        if (playerEntity instanceof ServerPlayerEntity) {
	        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) playerEntity;
	        ChampionCapability.getCapability(entity).ifPresent(champion -> {
                IChampion.Server serverChampion = champion.getServer();
                if (ChampionHelper.isValidChampion(serverChampion)) {
                    NetworkHandler.INSTANCE
                            .send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                                    new SPacketSyncChampion(entity.getId(),
                                            serverChampion.getRank().map(Rank::getTier).orElse(0),
                                            serverChampion.getRank().map(Rank::getDefaultColor).orElse(Color.fromRgb(0)).toString(),
                                            serverChampion.getAffixes().stream().map(IAffix::getIdentifier)
                                                    .collect(Collectors.toSet())));
                }
            });
        }
    }
}
