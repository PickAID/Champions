package top.theillusivec4.champions.common.integration.gateways_to_eternity;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.gateways.event.GateEvent;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.util.ChampionBuilder;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.common.util.Utils;

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
		var waveSpawnedEntity = event.getWaveEntity();
		var waveSpawnedEntityId = ForgeRegistries.ENTITIES.getKey(waveSpawnedEntity.getType());
		var gatewayEntity = event.getEntity();
		var currentWave = gatewayEntity.getWave();
		settings.getLoadedData().values().forEach((gatewaysSetting) -> {
			List<ResourceLocation> entityList = gatewaysSetting.entityBlackList().isPresent() ? gatewaysSetting.entityBlackList().get() : new ArrayList<>();

			var waveRange = gatewaysSetting.waveRange();
			if (!gatewaysSetting.enable().orElse(false) || !waveRange.matches(currentWave) || entityList.contains(waveSpawnedEntityId)) {
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
				int maxTier = tierRange.getMax() != null ? tierRange.getMax() : RankManager.getHighestRank().getTier();
				// make sure maxTier not smaller than minTier
				maxTier = Math.max(minTier, maxTier);
				int rangedRandomTier = Utils.nextIntInclusive(champion.getLivingEntity().getRandom(), minTier, maxTier);

				gatewaysSetting.affixes().forEach(affixId -> affixes.add(AffixRegistry.getRegistry().getValue(affixId)));

				ChampionBuilder.spawnPreset(champion, rangedRandomTier, affixes);
			});

		});


	}

}
