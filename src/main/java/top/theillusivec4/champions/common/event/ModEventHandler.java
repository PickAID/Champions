package top.theillusivec4.champions.common.event;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.client.integration.theoneprobe.TheOneProbePlugin;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.datagen.*;
import top.theillusivec4.champions.common.item.dispense.DispenseHandler;
import top.theillusivec4.champions.common.loot.EntityIsChampion;
import top.theillusivec4.champions.common.loot.LootItemChampionPropertyCondition;
import top.theillusivec4.champions.common.network.NetworkHandler;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.registry.ModItems;
import top.theillusivec4.champions.common.stat.ChampionsStats;
import top.theillusivec4.champions.common.util.EntityManager;
import top.theillusivec4.champions.common.util.Utils;
import top.theillusivec4.champions.server.command.ChampionSelectorOptions;

public class ModEventHandler {
    @SubscribeEvent
    public void onCommonSetup(final FMLCommonSetupEvent evt) {
        evt.enqueueWork(() -> {
            ChampionCapability.register();
            NetworkHandler.register();
            ChampionsStats.setup();
            ChampionSelectorOptions.setup();
            Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, Utils.getLocation("entity_champion"), EntityIsChampion.type);
            Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, Utils.getLocation("champion_properties"), LootItemChampionPropertyCondition.INSTANCE);
            DispenserBlock.registerBehavior(ModItems.CHAMPION_EGG_ITEM.get(), DispenseHandler::handleChampionEggDispense);
        });
    }

    @SubscribeEvent
    public void registerCapabilities(final RegisterCapabilitiesEvent evt) {
        evt.register(IChampion.class);
    }

    @SubscribeEvent
    public void config(final ModConfigEvent.Loading evt) {

        if (!evt.getConfig().getModId().equals(Champions.MODID)) {
            return;
        }

        if (evt.getConfig().getType() == ModConfig.Type.SERVER) {
            synchronized (this) {

                IConfigSpec<?> spec = evt.getConfig().getSpec();
                CommentedConfig commentedConfig = evt.getConfig().getConfigData();
                // 重建管理器
                try {
                    if (spec == ChampionsConfig.SERVER_SPEC){
                        ChampionsConfig.buildServer();
                    } else if (spec == ChampionsConfig.RANKS_SPEC) {
                        ChampionsConfig.transformRanks(commentedConfig);
                        RankManager.buildRanks();
                    } else if (spec == ChampionsConfig.ENTITIES_SPEC) {
                        ChampionsConfig.transformEntities(commentedConfig);
                        EntityManager.buildEntitySettings();
                    } else if (spec == ChampionsConfig.STAGE_SPEC && Utils.isGameStagesLoaded()) {
                        ChampionsConfig.buildStageConfig();
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
    public void enqueueIMC(final InterModEnqueueEvent event) {
        // register TheOneProbe integration
        if (ModList.get().isLoaded("theoneprobe")) {
            Champions.LOGGER.info("Champions detected TheOneProbe, registering plugin now");
            InterModComms.sendTo(Champions.MODID, "theoneprobe", "getTheOneProbe", TheOneProbePlugin.GetTheOneProbe::new);
        }
    }

    @SubscribeEvent
    public void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();
        // datapack provider for lookup datapack entries(RegistrySetBuilder).
        var datapackProvider = generator.addProvider(event.includeServer(), new ModDatapackProvider(packOutput, lookupProvider));

        // generator.addProvider(event.includeServer(),  new ModGlobalLootModifierProvider(generator));
        generator.addProvider(event.includeServer(),new GatewaysConfigProvider(packOutput,datapackProvider.getRegistryProvider()));
        generator.addProvider(event.includeServer(), new ModDamageTypeTagsProvider(packOutput, datapackProvider.getRegistryProvider(), existingFileHelper));
        generator.addProvider(event.includeServer(), new AffixConfigProvider(packOutput, datapackProvider.getRegistryProvider()));
        generator.addProvider(event.includeServer(), new AttributesModifierDataProvider(packOutput, datapackProvider.getRegistryProvider()));
        generator.addProvider(event.includeServer(), new ModEntityTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));
        // translate
        generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput, "zh_cn"));
        // add more translate to data generation
        generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput, "ko_kr"));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput, "ru_ru"));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput, "tr_tr"));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(packOutput, "uk_ua"));
    }
}
