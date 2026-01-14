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

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.attachment.Attachments;
import top.theillusivec4.champions.capability.Capabilities;
import top.theillusivec4.champions.champion.DifficultyBasedValues;
import top.theillusivec4.champions.champion.affix.AffixEffectComponents;
import top.theillusivec4.champions.champion.affix.effect.AffixEntityEffects;
import top.theillusivec4.champions.champion.affix.effect.AffixLocationBasedEffects;
import top.theillusivec4.champions.champion.affix.effect.AffixValueEffects;
import top.theillusivec4.champions.world.entity.EntityEventListener;
import top.theillusivec4.champions.champion.value.based.lootcontext.LevelBasedValues;
import top.theillusivec4.champions.champion.value.based.lootcontext.LootParamSourceTypes;
import top.theillusivec4.champions.command.Commands;
import top.theillusivec4.champions.component.DataComponents;
import top.theillusivec4.champions.config.CommonConfig;
import top.theillusivec4.champions.data.DataEventListener;
import top.theillusivec4.champions.deprecated.api.ChampionsApiImpl;
import top.theillusivec4.champions.deprecated.api.IChampionsApi;
import top.theillusivec4.champions.network.protocol.ClientGamePacketListener;
import top.theillusivec4.champions.particle.ParticleTypes;
import top.theillusivec4.champions.registry.BuiltInRegistries;
import top.theillusivec4.champions.server.champion.config.EntitySettingManager;
import top.theillusivec4.champions.server.config.ServerConfig;
import top.theillusivec4.champions.stats.Stats;
import top.theillusivec4.champions.util.Utils;
import top.theillusivec4.champions.world.effect.MobEffects;
import top.theillusivec4.champions.world.entity.EntityTypes;
import top.theillusivec4.champions.world.item.CreativeModeTabs;
import top.theillusivec4.champions.world.item.ItemEventListener;
import top.theillusivec4.champions.world.item.Items;
import top.theillusivec4.champions.world.loot.modifier.LootModifiers;
import top.theillusivec4.champions.world.loot.predicates.LootItemConditions;

import java.util.Objects;

@Mod(Champions.MODID)
public class Champions {
  public static final String MODID = "champions";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final String VERSION = "2.1.12.3";
  @Deprecated
  public static final IChampionsApi API = ChampionsApiImpl.getInstance();
  // champion instance
  private static Champions instance;
  private final CommonConfig commonConfig;
  private final ServerConfig serverConfig;
  private EntitySettingManager entitySettingManager;

  public static Champions getInstance() {
    return instance;
  }

  public Champions(IEventBus modEventBus, ModContainer modContainer) {
    instance = this;
    this.commonConfig = new CommonConfig();
    this.serverConfig = new ServerConfig();
    modContainer.registerConfig(ModConfig.Type.COMMON, this.commonConfig.getConfigSpec());
    modContainer.registerConfig(ModConfig.Type.SERVER, this.serverConfig.getConfigSpec());
    Items.register(modEventBus);
    ParticleTypes.register(modEventBus);
    MobEffects.register(modEventBus);
    EntityTypes.register(modEventBus);
    Stats.register(modEventBus);
    DataComponents.register(modEventBus);
    CreativeModeTabs.register(modEventBus);
    Capabilities.register(modEventBus);
    BuiltInRegistries.register(modEventBus);
    AffixEffectComponents.register(modEventBus);
    LevelBasedValues.register(modEventBus);
    DifficultyBasedValues.register(modEventBus);
    AffixValueEffects.register(modEventBus);
    AffixEntityEffects.register(modEventBus);
    AffixLocationBasedEffects.register(modEventBus);
    LootParamSourceTypes.register(modEventBus);
    Attachments.register(modEventBus);
    LootItemConditions.register(modEventBus);
    LootModifiers.register(modEventBus);
    DataEventListener.register(modEventBus);
    ClientGamePacketListener.register(modEventBus);
    EntityEventListener.register();
    ItemEventListener.register();
    ReloadEventListener.register();
    Commands.register();

//    modEventBus.register(this);
//    modEventBus.register(new ModEventHandler());
//    LootModifiers.register(modEventBus);
//    Stats.register(modEventBus);
//    ChampionsRegistry.register(modEventBus);
    // register champions config
//  modContainer.registerConfig(ModConfig.Type.COMMON, ChampionsConfig.COMMON_SPEC);
//  modContainer.registerConfig(ModConfig.Type.SERVER, ChampionsConfig.SERVER_SPEC);
//  modContainer.registerConfig(ModConfig.Type.CLIENT, ClientChampionsConfig.CLIENT_SPEC);

    // register GameStages compat config, if gameStages loaded

//    if (Utils.isGameStagesLoaded()) {
//      modContainer.registerConfig(ModConfig.Type.SERVER, ChampionsConfig.STAGE_SPEC, "champions-gamestages.toml");
//    }
//
//    if (Utils.isGatewaysLoaded()) {
//      NeoForge.EVENT_BUS.register(new GatewaysToEternityCompat());
//    }
//    if (Utils.isKubejsLoaded()){
//      NeoForge.EVENT_BUS.register(new NeoForgeJsEventHandler());
//    }

  }

  public CommonConfig getCommonConfig() {
    return commonConfig;
  }

  public ServerConfig getServerConfig() {
    return serverConfig;
  }

  public EntitySettingManager getChampionConfigSelectorManager() {
    return Objects.requireNonNull(entitySettingManager, "过早的访问配置选择管理器，至少应该在世界加载后");
  }

  public static final class ReloadEventListener {
    public static final Identifier CHAMPION_CONFIG_MANAGER = Utils.id("champion_config_manager");

    private static void register() {
      NeoForge.EVENT_BUS.register(new ReloadEventListener());
    }

    private ReloadEventListener() {
    }

    @SubscribeEvent
    public void onAddServerReloadListeners(AddServerReloadListenersEvent event) {
      EntitySettingManager manager = new EntitySettingManager(event.getRegistryAccess());
      event.addListener(CHAMPION_CONFIG_MANAGER, manager);
      Champions.instance.entitySettingManager = manager;
    }

  }
}
