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

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.api.ChampionsApiImpl;
import top.theillusivec4.champions.api.IChampionsApi;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.event.ModEventHandler;
import top.theillusivec4.champions.common.integration.gateways_to_eternity.GatewaysToEternityCompat;
import top.theillusivec4.champions.common.integration.kubejs.eventjs.EventJSHandler;
import top.theillusivec4.champions.common.registry.ChampionsRegistry;
import top.theillusivec4.champions.common.util.Utils;

@Mod(Champions.MODID)
public class Champions {

  public static final String MODID = "champions";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final IChampionsApi API = ChampionsApiImpl.getInstance();
  // champion instance
  private static Champions INSTANCE;
  public final ModContainer modContainer;

  public Champions(IEventBus modEventBus, ModContainer modContainer) {
    this.modContainer = modContainer;
    INSTANCE = this;
    modEventBus.register(new ModEventHandler());
    ChampionsRegistry.register(modEventBus);
    // register champions config
    modContainer.registerConfig(ModConfig.Type.COMMON, ChampionsConfig.COMMON_SPEC);
    modContainer.registerConfig(ModConfig.Type.SERVER, ChampionsConfig.SERVER_SPEC);
    modContainer.registerConfig(ModConfig.Type.CLIENT, ClientChampionsConfig.CLIENT_SPEC);
    // register GameStages compat config, if gameStages loaded
    if (Utils.isGameStagesLoaded()) {
      modContainer.registerConfig(ModConfig.Type.SERVER, ChampionsConfig.STAGE_SPEC, "champions-gamestages.toml");
    }

    // register kubejs affix, if kubejs loaded
    if (Utils.isKubeJsLoaded()) {
      modEventBus.addListener(EventJSHandler::registerAffix);
    }

    if (Utils.isGatewaysLoaded()){
      NeoForge.EVENT_BUS.register(new GatewaysToEternityCompat());
    }

  }

  public static Champions getInstance() {
    return INSTANCE;
  }

}
