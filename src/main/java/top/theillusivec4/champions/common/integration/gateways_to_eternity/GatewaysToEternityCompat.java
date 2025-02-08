package top.theillusivec4.champions.common.integration.gateways_to_eternity;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.gateways.event.GateEvent;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.util.ChampionBuilder;
import top.theillusivec4.champions.common.util.ChampionHelper;

import java.util.ArrayList;
import java.util.List;

public class GatewaysToEternityCompat {

    private final GatewaysSettingLoader settings = new GatewaysSettingLoader();

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(settings);
    }

    @SubscribeEvent
    public void onWaveEntitySpawned(GateEvent.WaveEntitySpawned event) {
        // read gate setting, and compare entity is match setting, if matched, apply champions setting

        var waveSpawnedEntity = event.getWaveEntity();
        var waveSpawnedEntityId = ForgeRegistries.ENTITY_TYPES.getKey(waveSpawnedEntity.getType());
        var gatewayEntity = event.getEntity();
        var currentWave = gatewayEntity.getWave();
        settings.getLoadedData().forEach((resourceLocation, gatewaysSetting) -> {
            List<ResourceLocation> entityList = gatewaysSetting.entityBlackList().isPresent() ? gatewaysSetting.entityBlackList().get() : new ArrayList<>();

            var waveRange = gatewaysSetting.waveRange();
            if (!waveRange.matches(currentWave) || entityList.contains(waveSpawnedEntityId)) {
                return;
            }

            ChampionCapability.getCapability(waveSpawnedEntity).ifPresent(champion -> {
                // first reset champions attribute
                var serverChampion = champion.getServer();
                if (ChampionHelper.isValidChampion(serverChampion)) {
                    ChampionBuilder.resetAndUpdate(champion);
                }

                List<IAffix> affixes = new ArrayList<>();
                var tierRange = gatewaysSetting.tier();

                int minTier = tierRange.getMin() != null ? tierRange.getMin() : 0;
                int maxTier = tierRange.getMax() != null ? tierRange.getMax() : minTier;
                int rangedRandomTier = champion.getLivingEntity().getRandom().nextIntBetweenInclusive(minTier, maxTier);

                gatewaysSetting.affixes().forEach(affixId -> affixes.add(AffixRegistry.getRegistry().getValue(affixId)));

                ChampionBuilder.spawnPreset(champion, rangedRandomTier, affixes);
            });

        });


    }

}
