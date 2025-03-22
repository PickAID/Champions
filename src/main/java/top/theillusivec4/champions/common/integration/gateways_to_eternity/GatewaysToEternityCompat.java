package top.theillusivec4.champions.common.integration.gateways_to_eternity;

import dev.shadowsoffire.gateways.event.GateEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.util.ChampionBuilder;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.common.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class GatewaysToEternityCompat {

  private final GatewaysSettingLoader settings = new GatewaysSettingLoader();

  @SubscribeEvent
  public void onAddReloadListener(AddServerReloadListenersEvent event) {
    event.addListener(Utils.getLocation("gateways_setting"), settings);
  }

  @SubscribeEvent
  public void onWaveEntitySpawned(GateEvent.WaveEntitySpawned event) {
    // Based gate setting, compare entity is match setting, if matched, apply champions setting
    var waveSpawnedEntity = event.getWaveEntity();
    var waveSpawnedEntityId = BuiltInRegistries.ENTITY_TYPE.getKey(waveSpawnedEntity.getType());
    var gatewayEntity = event.getEntity();
    var currentWave = gatewayEntity.getWave();
    var eventGateWayEntityType = GatewaysSetting.byEntityType(gatewayEntity);
    settings.getLoadedData().values().stream().filter(gatewaysSetting -> gatewaysSetting.enable().orElse(false))
      .forEach((gatewaysSetting) -> {
        var gatewayTypeSetting = gatewaysSetting.gatewayType();
        List<ResourceLocation> entityList = gatewaysSetting.entityBlackList().isPresent() ? gatewaysSetting.entityBlackList().get() : new ArrayList<>();

        var waveRange = gatewaysSetting.waveRange();
        if (!gatewaysSetting.enable().orElse(false) || eventGateWayEntityType != gatewayTypeSetting || !waveRange.matches(currentWave) || entityList.contains(waveSpawnedEntityId)) {
          return;
        }

        ChampionAttachment.getAttachment(waveSpawnedEntity).ifPresent(champion -> {
          // first reset champions attribute
          var serverChampion = champion.getServer();
          if (ChampionHelper.isValidChampion(serverChampion)) {
            ChampionBuilder.resetAndUpdate(champion);
          }

          List<IAffix> affixes = new ArrayList<>();
          var tierRange = gatewaysSetting.tierRange();

          int minTier = tierRange.min().orElse(0);
          int maxTier = tierRange.max().orElse(RankManager.getHighestRank().getTier());
          int rangedRandomTier = champion.getLivingEntity().getRandom().nextIntBetweenInclusive(minTier, maxTier);

          gatewaysSetting.affixes().forEach(affixId -> affixes.add(AffixRegistry.AFFIX_REGISTRY.getValue(affixId)));

          ChampionBuilder.spawnPreset(champion, rangedRandomTier, affixes);
        });

      });


  }

}
