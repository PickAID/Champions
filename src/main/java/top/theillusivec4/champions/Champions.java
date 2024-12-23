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
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.api.ChampionsApiImpl;
import top.theillusivec4.champions.api.IChampionsApi;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.datagen.*;
import top.theillusivec4.champions.common.integration.theoneprobe.TheOneProbePlugin;
import top.theillusivec4.champions.common.item.ChampionEggItem;
import top.theillusivec4.champions.common.network.SPacketSyncAffixData;
import top.theillusivec4.champions.common.network.SPacketSyncChampion;
import top.theillusivec4.champions.common.network.SyncAffixSettingPacket;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.registry.ChampionsRegistry;
import top.theillusivec4.champions.common.registry.ModItems;
import top.theillusivec4.champions.common.registry.ModStats;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.common.util.EntityManager;
import top.theillusivec4.champions.server.command.ChampionSelectorOptions;
import top.theillusivec4.champions.server.command.ChampionsCommand;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static top.theillusivec4.champions.api.AffixRegistry.AFFIX_REGISTRY;

@Mod(Champions.MODID)
public class Champions {

  public static final String MODID = "champions";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final IChampionsApi API = ChampionsApiImpl.getInstance();

  public static boolean scalingHealthLoaded = false;
  public static boolean gameStagesLoaded = false;
  public static boolean kubejsLoaded = false;
  public final ModContainer modContainer;

  public Champions(IEventBus modEventBus, ModContainer modContainer) {
    this.modContainer = modContainer;
    modEventBus.addListener(this::enqueueIMC);
    modEventBus.addListener(this::registerNetwork);
    modEventBus.addListener(this::onGatherData);
    modEventBus.addListener(this::registerRegistries);
    modEventBus.addListener(this::onClientSetup);
//    modEventBus.addListener(TestCustomAffixHandler::testOnCustomAffixBuild);
    modContainer.registerConfig(ModConfig.Type.CLIENT, ClientChampionsConfig.CLIENT_SPEC);
    modContainer.registerConfig(ModConfig.Type.SERVER, ChampionsConfig.SERVER_SPEC);
    modContainer.registerConfig(ModConfig.Type.COMMON, ChampionsConfig.COMMON_SPEC);
    createServerConfig(ChampionsConfig.RANKS_SPEC, "ranks");
    createServerConfig(ChampionsConfig.ENTITIES_SPEC, "entities");

    if (gameStagesLoaded) {
      modContainer.registerConfig(ModConfig.Type.SERVER, ChampionsConfig.STAGE_SPEC, "champions-gamestages.toml");
    }
    modEventBus.addListener(this::config);
    modEventBus.addListener(this::setup);
    NeoForge.EVENT_BUS.addListener(this::onDatapackSync);
    NeoForge.EVENT_BUS.addListener(this::registerCommands);
    ChampionsRegistry.register(modEventBus);
    scalingHealthLoaded = ModList.get().isLoaded("scalinghealth");
    kubejsLoaded = ModList.get().isLoaded("kubejs");
  }

  private static void createServerConfig(ModConfigSpec spec, String suffix) {
    String fileName = "champions-" + suffix + ".toml";
    ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SERVER, spec, fileName);
    File defaults = FMLPaths.GAMEDIR.get().resolve("defaultconfigs").resolve(fileName).toFile();

    if (!defaults.exists()) {
      try {
        FileUtils.copyInputStreamToFile(Objects.requireNonNull(Champions.class.getClassLoader().getResourceAsStream(fileName)), defaults);
      } catch (IOException e) {
        LOGGER.error("Error creating default config for {}", fileName);
      }
    }
  }

  public static ResourceLocation getLocation(final String path) {
    return ResourceLocation.fromNamespaceAndPath(MODID, path);
  }

  private void registerRegistries(NewRegistryEvent event) {
    event.register(AFFIX_REGISTRY);
  }

  private void setup(final FMLCommonSetupEvent evt) {
    ChampionAttachment.register();
    evt.enqueueWork(() -> {
      ModStats.registerFormatter();
      ChampionSelectorOptions.setup();
      DispenseItemBehavior dispenseBehavior = (source, stack) -> {
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        Optional<EntityType<?>> entityType = ChampionEggItem.getType(stack);
        entityType.ifPresent(type -> {
          Entity entity = type.create(source.level(), (s) -> stack.getTags(), source.pos().relative(direction), MobSpawnType.DISPENSER, true, direction != Direction.UP);
          assert entity != null;
          ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
            if (ChampionHelper.isValidChampion(champion.getServer())) {
              ChampionEggItem.read(champion, stack);
              source.level().addFreshEntity(champion.getLivingEntity());
              stack.shrink(1);
            }
          });
        });
        return stack;
      };
      DispenserBlock.registerBehavior(ModItems.CHAMPION_EGG_ITEM.get(), dispenseBehavior);
    });
  }

  private void onClientSetup(final FMLClientSetupEvent event) {
    this.modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
  }

  private void registerCommands(final RegisterCommandsEvent evt) {
    ChampionsCommand.register(evt.getDispatcher());
  }

  private void config(final ModConfigEvent.Loading evt) {

    if (!evt.getConfig().getModId().equals(MODID)) {
      return;
    }

    if (evt.getConfig().getType() == ModConfig.Type.SERVER) {
      synchronized (this) {

        IConfigSpec spec = evt.getConfig().getSpec();
        CommentedConfig commentedConfig = evt.getConfig().getLoadedConfig().config();
        ChampionsConfig.bake();
        // 重建管理器
        if (spec == ChampionsConfig.RANKS_SPEC) {
          ChampionsConfig.transformRanks(commentedConfig);
          RankManager.buildRanks();
        } else if (spec == ChampionsConfig.ENTITIES_SPEC) {
          ChampionsConfig.transformEntities(commentedConfig);
          EntityManager.buildEntitySettings();
        } else if (spec == ChampionsConfig.STAGE_SPEC && Champions.gameStagesLoaded) {
          ChampionsConfig.entityStages = ChampionsConfig.STAGE.entityStages.get();
          ChampionsConfig.tierStages = ChampionsConfig.STAGE.tierStages.get();
        }
      }
    } else if (evt.getConfig().getType() == ModConfig.Type.CLIENT) {
      ClientChampionsConfig.bake();
    } else if (evt.getConfig().getType() == ModConfig.Type.COMMON) {
      ChampionsConfig.bakeCommon();
    }
  }

  private void enqueueIMC(final InterModEnqueueEvent event) {
    // register TheOneProbe integration
    if (ModList.get().isLoaded("theoneprobe")) {
      Champions.LOGGER.info("Champions detected TheOneProbe, registering plugin now");
      InterModComms.sendTo(MODID, "theoneprobe", "getTheOneProbe", TheOneProbePlugin.GetTheOneProbe::new);
    }
  }

  private void registerNetwork(final RegisterPayloadHandlersEvent event) {
    final PayloadRegistrar registrar = event.registrar("champions");
    registrar.playToClient(SPacketSyncAffixData.TYPE, SPacketSyncAffixData.STREAM_CODEC, SPacketSyncAffixData::handle);
    registrar.playToClient(SPacketSyncChampion.TYPE, SPacketSyncChampion.STREAM_CODEC, SPacketSyncChampion::handle);
    registrar.playToClient(SyncAffixSettingPacket.TYPE, SyncAffixSettingPacket.STREAM_CODEC, SyncAffixSettingPacket::handle);
  }

  private void onGatherData(GatherDataEvent event) {
    var generator = event.getGenerator();
    var packOutput = generator.getPackOutput();
    var lookupProvider = event.getLookupProvider();
    var existingFileHelper = event.getExistingFileHelper();
    // datapack provider for lookup datapack entries(RegistrySetBuilder).
    var datapackProvider = generator.addProvider(event.includeServer(), new ModDatapackProvider(packOutput, lookupProvider));

    generator.addProvider(event.includeServer(), new ModGlobalLootModifierProvider(packOutput, lookupProvider));
    generator.addProvider(event.includeServer(), new ModAdvancementProvider(packOutput, lookupProvider, existingFileHelper, List.of(new ModAdvancementProvider.Generator())));
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

  private void onDatapackSync(OnDatapackSyncEvent event) {
    // send to single player login or reload for all relevant players.
    var relevantPlayers = event.getRelevantPlayers();

    var syncAffixSetting = new SyncAffixSettingPacket(API.getAffixDataLoader().getLoadedData());
    // apply setting on server, and sync affix settings to client
    SyncAffixSettingPacket.handelSettingMainThread();
    relevantPlayers.forEach(player -> PacketDistributor.sendToPlayer(player, syncAffixSetting));
  }
}
