package top.theillusivec4.champions.common.event;

import com.electronwill.nightconfig.core.CommentedConfig;
import mcjty.theoneprobe.TheOneProbe;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.client.integration.theoneprobe.TheOneProbePlugin;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.datagen.*;
import top.theillusivec4.champions.common.item.dispense.DispenseHandler;
import top.theillusivec4.champions.common.network.SPacketSyncAffixData;
import top.theillusivec4.champions.common.network.SPacketSyncChampion;
import top.theillusivec4.champions.common.network.SyncAffixSettingPacket;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.registry.ModItems;
import top.theillusivec4.champions.common.registry.ModStats;
import top.theillusivec4.champions.common.util.EntityManager;
import top.theillusivec4.champions.common.util.Utils;
import top.theillusivec4.champions.server.command.ChampionSelectorOptions;

import java.util.List;

import static top.theillusivec4.champions.api.AffixRegistry.AFFIX_REGISTRY;

public class ModEventHandler {

  @SubscribeEvent
  private void registerNetwork(final RegisterPayloadHandlersEvent event) {
    final PayloadRegistrar registrar = event.registrar(Champions.MODID);
    registrar.playToClient(SPacketSyncAffixData.TYPE, SPacketSyncAffixData.STREAM_CODEC, SPacketSyncAffixData::handle);
    registrar.playToClient(SPacketSyncChampion.TYPE, SPacketSyncChampion.STREAM_CODEC, SPacketSyncChampion::handle);
    registrar.playToClient(SyncAffixSettingPacket.TYPE, SyncAffixSettingPacket.STREAM_CODEC, SyncAffixSettingPacket::handle);
  }

  @SubscribeEvent
  private void onCommonSetup(final FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      Utils.createServerConfig(ChampionsConfig.RANKS_SPEC, "ranks");
      Utils.createServerConfig(ChampionsConfig.ENTITIES_SPEC, "entities");
      ChampionAttachment.register();
      ModStats.registerFormatter();
      ChampionSelectorOptions.setup();
      DispenserBlock.registerBehavior(ModItems.CHAMPION_EGG_ITEM.get(), DispenseHandler::handleChampionEggDispense);
    });
  }

  @SubscribeEvent
  private void onInterModEnqueue(final InterModEnqueueEvent event) {
    // register TheOneProbe integration
    if (ModList.get().isLoaded(TheOneProbe.MODID)) {
      Champions.LOGGER.info("Champions detected TheOneProbe, registering plugin now");
      InterModComms.sendTo(Champions.MODID, TheOneProbe.MODID, "getTheOneProbe", TheOneProbePlugin.GetTheOneProbe::new);
    }
  }

  @SubscribeEvent
  private void onGatherServerData(GatherDataEvent.Server event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    var dataGenerator = event.getGenerator();
    // datapack provider for lookup datapack entries(RegistrySetBuilder).
    var datapackProvider = dataGenerator.addProvider(true, new ModDatapackProvider(packOutput, lookupProvider));

    event.addProvider(new ModGlobalLootModifierProvider(packOutput, lookupProvider));
    event.addProvider(new ModAdvancementProvider(packOutput, lookupProvider, List.of(new ModAdvancementProvider.Generator())));
    event.addProvider(new ModDamageTypeTagsProvider(packOutput, datapackProvider.getRegistryProvider()));
    event.addProvider(new AffixConfigProvider(packOutput, datapackProvider.getRegistryProvider()));
    event.addProvider(new AttributesModifierDataProvider(packOutput, datapackProvider.getRegistryProvider()));
    event.addProvider(new ModEntityTypeTagsProvider(packOutput, lookupProvider));
  }

  @SubscribeEvent
  private void onGatherClientData(GatherDataEvent.Client event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
//    var lookupProvider = event.getLookupProvider();
    // translate
    event.addProvider(new ModLanguageProvider(packOutput));
    event.addProvider(new ModLanguageProvider(packOutput, "zh_cn"));
    // add more translate to data generation
    event.addProvider(new ModLanguageProvider(packOutput, "ko_kr"));
    event.addProvider(new ModLanguageProvider(packOutput, "ru_ru"));
    event.addProvider(new ModLanguageProvider(packOutput, "tr_tr"));
    event.addProvider(new ModLanguageProvider(packOutput, "uk_ua"));
  }

  @SubscribeEvent
  private void config(final ModConfigEvent.Loading evt) {

    if (!evt.getConfig().getModId().equals(Champions.MODID)) {
      return;
    }

    if (evt.getConfig().getType() == ModConfig.Type.SERVER) {
      ChampionsConfig.bake();
      synchronized (this) {
        IConfigSpec spec = evt.getConfig().getSpec();
        CommentedConfig commentedConfig = evt.getConfig().getLoadedConfig().config();
        // 重建管理器
        try {
          if (spec == ChampionsConfig.RANKS_SPEC) {
            ChampionsConfig.transformRanks(commentedConfig);
            RankManager.buildRanks();
          } else if (spec == ChampionsConfig.ENTITIES_SPEC) {
            ChampionsConfig.transformEntities(commentedConfig);
            EntityManager.buildEntitySettings();
          } else if (spec == ChampionsConfig.STAGE_SPEC && Utils.isGameStagesLoaded()) {
            ChampionsConfig.entityStages = ChampionsConfig.STAGE.entityStages.get();
            ChampionsConfig.tierStages = ChampionsConfig.STAGE.tierStages.get();
          }
        } catch (Exception e) {
          Champions.LOGGER.error("Error loading config, please remove this file or check the format is correct: {}", FMLPaths.GAMEDIR.get().resolve(evt.getConfig().getFullPath()), e);
        }
      }
    } else if (evt.getConfig().getType() == ModConfig.Type.CLIENT) {
      ClientChampionsConfig.bake();
    } else if (evt.getConfig().getType() == ModConfig.Type.COMMON) {
      ChampionsConfig.bakeCommon();
    }
  }

  @SubscribeEvent
  private void onClientSetup(final FMLClientSetupEvent event) {
    Champions.getInstance().modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
  }

  @SubscribeEvent
  private void registerRegistries(NewRegistryEvent event) {
    event.register(AFFIX_REGISTRY);
  }

}
