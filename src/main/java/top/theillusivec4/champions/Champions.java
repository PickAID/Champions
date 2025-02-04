/*
 * Copyright (C) 2018-2019  C4
 *
 * This file is part of Champions, a mod made for Minecraft.
 *
 * Champions is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Champions is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Champions.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.champions;

import com.electronwill.nightconfig.core.CommentedConfig;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.api.AffixDataLoader;
import top.theillusivec4.champions.api.ChampionsApiImpl;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.IChampionsApi;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.datagen.AffixConfigProvider;
import top.theillusivec4.champions.common.datagen.AttributesModifierDataProvider;
import top.theillusivec4.champions.common.datagen.ModEntityTypeTagsProvider;
import top.theillusivec4.champions.common.datagen.ModLanguageProvider;
import top.theillusivec4.champions.common.integration.gamestages.GameStagesPlugin;
import top.theillusivec4.champions.common.integration.theoneprobe.TheOneProbePlugin;
import top.theillusivec4.champions.common.item.ChampionEggItem;
import top.theillusivec4.champions.common.loot.EntityIsChampion;
import top.theillusivec4.champions.common.loot.LootItemChampionPropertyCondition;
import top.theillusivec4.champions.common.network.NetworkHandler;
import top.theillusivec4.champions.common.network.SPacketSyncAffixSetting;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.registry.ChampionsRegistry;
import top.theillusivec4.champions.common.registry.ModItems;
import top.theillusivec4.champions.common.stat.ChampionsStats;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.common.util.EntityManager;
import top.theillusivec4.champions.server.command.ChampionSelectorOptions;
import top.theillusivec4.champions.server.command.ChampionsCommand;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Mod(Champions.MODID)
public class Champions {

    public static final String MODID = "champions";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final IChampionsApi API = ChampionsApiImpl.getInstance();
    private static final AffixDataLoader dataLoader = new AffixDataLoader();
    public static boolean scalingHealthLoaded = false;
    public static boolean gameStagesLoaded = false;

    public Champions() {
        var modContext = ModLoadingContext.get();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::enqueueIMC);
        modContext.registerConfig(Type.CLIENT, ClientChampionsConfig.CLIENT_SPEC);
        modContext.registerConfig(Type.SERVER, ChampionsConfig.SERVER_SPEC);
        modContext.registerConfig(Type.COMMON, ChampionsConfig.COMMON_SPEC);
        createServerConfig(modContext,ChampionsConfig.RANKS_SPEC, "ranks");
        createServerConfig(modContext, ChampionsConfig.ENTITIES_SPEC, "entities");
        gameStagesLoaded = ModList.get().isLoaded("gamestages");

        if (gameStagesLoaded) {
            modContext
                    .registerConfig(Type.SERVER, ChampionsConfig.STAGE_SPEC, "champions-gamestages.toml");
        }
        eventBus.addListener(this::config);
        eventBus.addListener(this::setup);
        eventBus.addListener(this::registerCaps);
        eventBus.addListener(this::onGatherData);
        MinecraftForge.EVENT_BUS.addListener(this::onDatapackSync);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        ChampionsRegistry.register(eventBus);
        scalingHealthLoaded = ModList.get().isLoaded("scalinghealth");
    }

    private static void createServerConfig(ModLoadingContext modContext, ForgeConfigSpec spec, String suffix) {
        String fileName = "champions-" + suffix + ".toml";
        modContext.registerConfig(Type.SERVER, spec, fileName);
        File defaults = FMLPaths.GAMEDIR.get().resolve("defaultconfigs").resolve(fileName).toFile();

        if (!defaults.exists()) {
            try {
                FileUtils.copyInputStreamToFile(
                        Objects.requireNonNull(Champions.class.getClassLoader().getResourceAsStream(fileName)),
                        defaults);
            } catch (IOException e) {
                LOGGER.error("Error creating default config for {}", fileName);
            }
        }
    }

    public static ResourceLocation getLocation(final String path) {
        return new ResourceLocation(MODID, path);
    }

    public static AffixDataLoader getDataLoader() {
        return dataLoader;
    }

    private void setup(final FMLCommonSetupEvent evt) {
        ChampionCapability.register();
        NetworkHandler.register();
        evt.enqueueWork(() -> {
            ChampionsStats.setup();
            ChampionSelectorOptions.setup();
            Registry.register(Registry.LOOT_CONDITION_TYPE, Champions.getLocation("entity_champion"), EntityIsChampion.type);
            Registry.register(Registry.LOOT_CONDITION_TYPE, Champions.getLocation("champion_properties"), LootItemChampionPropertyCondition.INSTANCE);

            DispenseItemBehavior dispenseBehavior = (source, stack) -> {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                Optional<EntityType<?>> entityType = ChampionEggItem.getType(stack);
                entityType.ifPresent(type -> {
                    Entity entity = type.create(source.getLevel(), stack.getTag(), null, null,
                            source.getPos().relative(direction), MobSpawnType.DISPENSER, true,
                            direction != Direction.UP);

                    if (entity != null) {
                        ChampionCapability.getCapability(entity).ifPresent(champion -> {
                            if (ChampionHelper.isValidChampion(champion.getServer())) {
                                ChampionEggItem.read(champion, stack);
                                source.getLevel().addFreshEntity(champion.getLivingEntity());
                                stack.shrink(1);
                            }
                        });
                    }
                });
                return stack;
            };
            DispenserBlock.registerBehavior(ModItems.CHAMPION_EGG_ITEM.get(), dispenseBehavior);
        });
    }

    private void registerCaps(final RegisterCapabilitiesEvent evt) {
        evt.register(IChampion.class);
    }

    private void registerCommands(final RegisterCommandsEvent evt) {
        ChampionsCommand.register(evt.getDispatcher());
    }

    private void config(final ModConfigEvent evt) {

        if (!evt.getConfig().getModId().equals(MODID)) {
            return;
        }

        if (evt.getConfig().getType() == Type.SERVER) {
            synchronized (this) {

                IConfigSpec<?> spec = evt.getConfig().getSpec();
                CommentedConfig commentedConfig = evt.getConfig().getConfigData();

                if (evt instanceof ModConfigEvent.Loading) {

                    ChampionsConfig.bake();
                    // 重建管理器
                    try {
                        if (spec == ChampionsConfig.RANKS_SPEC) {
                            ChampionsConfig.transformRanks(commentedConfig);
                            RankManager.buildRanks();
                        } else if (spec == ChampionsConfig.ENTITIES_SPEC) {
                            ChampionsConfig.transformEntities(commentedConfig);
                            EntityManager.buildEntitySettings();
                        } else if (spec == ChampionsConfig.STAGE_SPEC && Champions.gameStagesLoaded) {
                            ChampionsConfig.entityStages = ChampionsConfig.STAGE.entityStages.get();
                            ChampionsConfig.tierStages = ChampionsConfig.STAGE.tierStages.get();
                            GameStagesPlugin.buildStages();
                        }
                    } catch (Exception e) {
                        LOGGER.error("Error loading config, please remove this file or check the format is correct: {}", FMLPaths.GAMEDIR.get().resolve(evt.getConfig().getFullPath()), e);
                    }

                }
            }
        } else if (evt.getConfig().getType() == Type.CLIENT) {
            ClientChampionsConfig.bake();
        } else if (evt.getConfig().getType() == Type.COMMON) {
            ChampionsConfig.bakeCommon();
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // register TheOneProbe integration
        if (ModList.get().isLoaded("theoneprobe")) {
            Champions.LOGGER.info("Champions detected TheOneProbe, registering plugin now");
            InterModComms.sendTo(MODID, "theoneprobe", "getTheOneProbe",
                    TheOneProbePlugin.GetTheOneProbe::new);
        }
    }

    private void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();

//    generator.addProvider(event.includeServer(), new ModGlobalLootModifierProvider(generator));
        generator.addProvider(event.includeServer(), new AffixConfigProvider(generator));
        generator.addProvider(event.includeServer(), new AttributesModifierDataProvider(generator));
        generator.addProvider(event.includeServer(), new ModEntityTypeTagsProvider(generator, existingFileHelper));
        // translate
        generator.addProvider(event.includeClient(), new ModLanguageProvider(generator));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(generator, "zh_cn"));
        // add more translate to data generation
        generator.addProvider(event.includeClient(), new ModLanguageProvider(generator, "ko_kr"));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(generator, "ru_ru"));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(generator, "tr_tr"));
        generator.addProvider(event.includeClient(), new ModLanguageProvider(generator, "uk_ua"));
    }

    private void onDatapackSync(OnDatapackSyncEvent event) {
        // send to single player login or reload for all relevant players.
        var relevantPlayers = event.getPlayerList().getPlayers();
        var syncAffixSetting = new SPacketSyncAffixSetting(getDataLoader().getLoadedData());
        // apply setting on server, and sync affix settings to client
        SPacketSyncAffixSetting.handelSettingMainThread();
        relevantPlayers.forEach(player -> NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), syncAffixSetting));
    }
}
