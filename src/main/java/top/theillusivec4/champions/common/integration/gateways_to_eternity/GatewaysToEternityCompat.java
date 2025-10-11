package top.theillusivec4.champions.common.integration.gateways_to_eternity;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import shadows.gateways.event.GateEvent;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.IChampion;
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
		LivingEntity waveSpawnedEntity = event.getWaveEntity();
		ResourceLocation waveSpawnedEntityId = ForgeRegistries.ENTITIES.getKey(waveSpawnedEntity.getType());
		shadows.gateways.entity.GatewayEntity gatewayEntity = event.getEntity();
		int currentWave = gatewayEntity.getWave();
		settings.getLoadedData().values().forEach((gatewaysSetting) -> {
			List<ResourceLocation> entityList = gatewaysSetting.entityBlackList().isPresent() ? gatewaysSetting.entityBlackList().get() : new ArrayList<>();

			MinMaxBounds.IntBound waveRange = gatewaysSetting.waveRange();
			if (!gatewaysSetting.enable().orElse(false) || !waveRange.matches(currentWave) || entityList.contains(waveSpawnedEntityId)) {
				return;
			}

			ChampionCapability.getCapability(waveSpawnedEntity).ifPresent(champion -> {
				// first reset champions attribute
				IChampion.Server serverChampion = champion.getServer();
				if (ChampionHelper.isValidChampion(serverChampion)) {
					ChampionBuilder.resetAndUpdate(champion);
				}

				List<IAffix> affixes = new ArrayList<>();
				MinMaxBounds.IntBound tierRange = gatewaysSetting.tier();

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
