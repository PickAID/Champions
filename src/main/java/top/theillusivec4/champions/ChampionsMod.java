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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.ModifyRegistriesEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.affix.*;
import top.theillusivec4.champions.affix.effects.AffixEntityEffects;
import top.theillusivec4.champions.affix.effects.AffixLocationBasedEffects;
import top.theillusivec4.champions.affix.effects.AffixValueEffects;
import top.theillusivec4.champions.affix.provider.AffixProvider;
import top.theillusivec4.champions.affix.provider.AffixProviders;
import top.theillusivec4.champions.attachments.ChampionsAttachments;
import top.theillusivec4.champions.champion.Rank;
import top.theillusivec4.champions.champion.Ranks;
import top.theillusivec4.champions.client.network.ChampionsClientPayloadHandler;
import top.theillusivec4.champions.component.ChampionsDataComponents;
import top.theillusivec4.champions.data.ChampionsDataMapProvider;
import top.theillusivec4.champions.data.lang.EnUs;
import top.theillusivec4.champions.data.lang.ZhCn;
import top.theillusivec4.champions.deprecated.api.IChampionsApi;
import top.theillusivec4.champions.deprecated.api.impl.ChampionsApiImpl;
import top.theillusivec4.champions.deprecated.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.deprecated.common.affix.core.AffixManager;
import top.theillusivec4.champions.deprecated.common.capability.ChampionAttachment;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.deprecated.common.datagen.ModAdvancementProvider;
import top.theillusivec4.champions.deprecated.common.datagen.ModDamageTypeTagsProvider;
import top.theillusivec4.champions.deprecated.common.datagen.ModDatapackProvider;
import top.theillusivec4.champions.deprecated.common.datagen.ModGlobalLootModifierProvider;
import top.theillusivec4.champions.deprecated.common.integration.theoneprobe.TheOneProbePlugin;
import top.theillusivec4.champions.deprecated.common.item.ChampionEggItem;
import top.theillusivec4.champions.deprecated.common.network.SPacketSyncAffixData;
import top.theillusivec4.champions.deprecated.common.network.SPacketSyncChampion;
import top.theillusivec4.champions.deprecated.common.rank.RankManager;
import top.theillusivec4.champions.deprecated.common.registry.ModItems;
import top.theillusivec4.champions.deprecated.common.registry.ModStats;
import top.theillusivec4.champions.deprecated.common.util.ChampionHelper;
import top.theillusivec4.champions.deprecated.common.util.EntityManager;
import top.theillusivec4.champions.deprecated.server.command.ChampionSelectorOptions;
import top.theillusivec4.champions.deprecated.server.command.ChampionsCommand;
import top.theillusivec4.champions.network.ChampionsBossEventPayload;
import top.theillusivec4.champions.particles.ChampionsParticleTypes;
import top.theillusivec4.champions.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.registries.ChampionsDataMaps;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.server.ChampionsServerConfig;
import top.theillusivec4.champions.world.damagesource.ChampionsDamageTypes;
import top.theillusivec4.champions.world.effect.ChampionsMobEffects;
import top.theillusivec4.champions.world.entity.ChampionsEntityTypes;
import top.theillusivec4.champions.world.loot.predicates.ChampionsLootItemConditions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(ChampionsMod.MOD_ID)
public class ChampionsMod {
  public static final String VERSION = "21.1.0.2.1.8.4";
  public static final String MOD_ID = "champions";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final IChampionsApi API = ChampionsApiImpl.getInstance();

  public static boolean scalingHealthLoaded = false;
  public static boolean gameStagesLoaded = false;

  public ChampionsMod(IEventBus modEventBus, ModContainer modContainer) {
    // New
    modEventBus.register(this);
    modContainer.registerConfig(ModConfig.Type.SERVER, ChampionsServerConfig.SPEC);
    ChampionsAttachments.register(modEventBus);
    ChampionsDataComponents.register(modEventBus);
    ChampionsEntityTypes.register(modEventBus);
    ChampionsMobEffects.register(modEventBus);
    ChampionsParticleTypes.register(modEventBus);
    ChampionsLootItemConditions.register(modEventBus);
    AffixEffectComponents.register(modEventBus);
    ProjectileTemplates.register(modEventBus);
    LevelBasedValues.register(modEventBus);
    AffixLocationBasedEffects.register(modEventBus);
    AffixEntityEffects.register(modEventBus);
    AffixValueEffects.register(modEventBus);
    AffixProviders.register(modEventBus);
    // Old
//    modEventBus.addListener(this::enqueueIMC);
//    modEventBus.addListener(this::registerNetwork);
//    modEventBus.addListener(this::onGatherData);
//    modContainer.registerConfig(ModConfig.Type.CLIENT, ClientChampionsConfig.CLIENT_SPEC);
//    modContainer.registerConfig(ModConfig.Type.SERVER, ChampionsConfig.SERVER_SPEC);
//    modContainer.registerConfig(ModConfig.Type.COMMON, ChampionsConfig.COMMON_SPEC);
//    createServerConfig(ChampionsConfig.RANKS_SPEC, "ranks");
//    createServerConfig(ChampionsConfig.AFFIXES_SPEC, "affixes");
//    createServerConfig(ChampionsConfig.ENTITIES_SPEC, "entities");
//
//    if (gameStagesLoaded) {
//      modContainer.registerConfig(ModConfig.Type.SERVER, ChampionsConfig.STAGE_SPEC, "champions-gamestages.toml");
//    }
//    modEventBus.addListener(this::config);
//    modEventBus.addListener(this::setup);
//    NeoForge.EVENT_BUS.addListener(this::registerCommands);
//    ChampionsRegistry.register(modEventBus);
//    scalingHealthLoaded = ModList.get().isLoaded("scalinghealth");
  }

  private static void createServerConfig(ModConfigSpec spec, String suffix) {
    String fileName = "champions-" + suffix + ".toml";
    ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SERVER, spec, fileName);
    File defaults = FMLPaths.GAMEDIR.get().resolve("defaultconfigs").resolve(fileName).toFile();

    if (!defaults.exists()) {
      try {
        FileUtils.copyInputStreamToFile(Objects.requireNonNull(ChampionsMod.class.getClassLoader().getResourceAsStream(fileName)), defaults);
      } catch (IOException e) {
        LOGGER.error("Error creating default config for " + fileName);
      }
    }
  }

  public static ResourceLocation getLocation(final String path) {
    return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
  }

  @SubscribeEvent
  private void registerRegistries(NewRegistryEvent event) {
    event.register(ChampionsBuiltInRegistries.AFFIX_PROVIDER_TYPE);
    event.register(ChampionsBuiltInRegistries.PROJECTILE_TEMPLATE_TYPE);
    event.register(ChampionsBuiltInRegistries.LEVEL_BASED_VALUE_TYPE);
    event.register(ChampionsBuiltInRegistries.AFFIX_EFFECT_COMPONENT_TYPE);
    event.register(ChampionsBuiltInRegistries.AFFIX_LOCATION_BASED_EFFECT_TYPE);
    event.register(ChampionsBuiltInRegistries.AFFIX_ENTITY_EFFECT_TYPE);
    event.register(ChampionsBuiltInRegistries.AFFIX_VALUE_EFFECT_TYPE);
  }

  @SubscribeEvent
  private void onModifyRegistries(ModifyRegistriesEvent event) {

  }

  @SubscribeEvent
  private void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
    event.dataPackRegistry(ChampionsRegistries.AFFIX, Affix.DIRECT_CODEC, Affix.DIRECT_CODEC);
    event.dataPackRegistry(ChampionsRegistries.AFFIX_PROVIDER, AffixProvider.DIRECT_CODEC, AffixProvider.DIRECT_CODEC);
    event.dataPackRegistry(ChampionsRegistries.RANK, Rank.DIRECT_CODEC, Rank.DIRECT_CODEC);
  }

  @SubscribeEvent
  public void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
    PayloadRegistrar registrar = event.registrar(VERSION);
    registrar.playToClient(
      ChampionsBossEventPayload.TYPE,
      ChampionsBossEventPayload.STREAM_CODEC,
      ChampionsClientPayloadHandler::handleBossEvent
    );
  }

  @SubscribeEvent
  public void registerDataMaps(RegisterDataMapTypesEvent event) {
    event.register(ChampionsDataMaps.AFFIXABLE);
  }

  @SubscribeEvent
  public void generateData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    ExistingFileHelper helper = event.getExistingFileHelper();
    PackOutput output = generator.getPackOutput();
    CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
    RegistrySetBuilder builder = new RegistrySetBuilder()
      .add(Registries.DAMAGE_TYPE, ChampionsDamageTypes::bootstrap)
      .add(ChampionsRegistries.AFFIX, Affixes::bootstrap)
      .add(ChampionsRegistries.RANK, Ranks::bootstrap);
    var datapackRegistries = new DatapackBuiltinEntriesProvider(output, lookup, builder, Set.of(ChampionsMod.MOD_ID));
    lookup = datapackRegistries.getRegistryProvider();

    generator.addProvider(event.includeServer(), new ChampionsDataMapProvider(output, lookup));
    generator.addProvider(event.includeServer(), datapackRegistries);
    generator.addProvider(event.includeClient(), new ZhCn(output));
    generator.addProvider(event.includeClient(), new EnUs(output));
  }

  private void setup(final FMLCommonSetupEvent evt) {
    ChampionAttachment.register();
    AffixManager.register();
    evt.enqueueWork(() -> {
      ModStats.registerFormatter();
      ChampionSelectorOptions.setup();
      DispenseItemBehavior dispenseBehavior = (source, stack) -> {
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        Optional<EntityType<?>> entityType = ChampionEggItem.getType(stack);
        entityType.ifPresent(type -> {
          Entity entity = type.create(source.level(), (s) -> stack.getTags(), source.pos().relative(direction), MobSpawnType.DISPENSER, true, direction != Direction.UP);
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

  private void registerCommands(final RegisterCommandsEvent evt) {
    ChampionsCommand.register(evt.getDispatcher());
  }

  private void config(final ModConfigEvent.Loading evt) {

    if (!evt.getConfig().getModId().equals(MOD_ID)) {
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
        } else if (spec == ChampionsConfig.AFFIXES_SPEC) {
          ChampionsConfig.transformAffixes(commentedConfig);
          AffixManager.buildAffixSettings();
        } else if (spec == ChampionsConfig.ENTITIES_SPEC) {
          ChampionsConfig.transformEntities(commentedConfig);
          EntityManager.buildEntitySettings();
        } else if (spec == ChampionsConfig.STAGE_SPEC && ChampionsMod.gameStagesLoaded) {
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
      ChampionsMod.LOGGER.info("Champions detected TheOneProbe, registering plugin now");
      InterModComms.sendTo(MOD_ID, "theoneprobe", "getTheOneProbe", TheOneProbePlugin.GetTheOneProbe::new);
    }
  }

  private void registerNetwork(final RegisterPayloadHandlersEvent event) {
    final PayloadRegistrar registrar = event.registrar("champions");
    registrar.playToClient(SPacketSyncAffixData.TYPE, SPacketSyncAffixData.STREAM_CODEC, SPacketSyncAffixData::handle);
    registrar.playToClient(SPacketSyncChampion.TYPE, SPacketSyncChampion.STREAM_CODEC, SPacketSyncChampion::handle);
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

  }
}
