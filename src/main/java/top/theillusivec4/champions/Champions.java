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

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
@SuppressWarnings("removal")
public class Champions {

    public static final String MODID = "champions";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final IChampionsApi API = ChampionsApiImpl.getInstance();
    // champion instance
    private static Champions INSTANCE;
    public final ModLoadingContext modContext;

    public Champions() {
        INSTANCE = this;
        this.modContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modContext.registerConfig(Type.CLIENT, ClientChampionsConfig.CLIENT_SPEC);
        modContext.registerConfig(Type.SERVER, ChampionsConfig.SERVER_SPEC);
        modContext.registerConfig(Type.COMMON, ChampionsConfig.COMMON_SPEC);
        Utils.createServerConfig(ChampionsConfig.RANKS_SPEC, "ranks");
        Utils.createServerConfig(ChampionsConfig.ENTITIES_SPEC, "entities");
        modEventBus.register(new ModEventHandler());

        if (Utils.isGameStagesLoaded()) {
            modContext.registerConfig(Type.SERVER, ChampionsConfig.STAGE_SPEC, "champions-gamestages.toml");
        }

        // register kubejs affix, if kubejs loaded
        if (Utils.isKubeJsLoaded()) {
            modEventBus.addListener(EventJSHandler::registerAffix);
        }
        // register item, particle, etc...
        if (Utils.isGatewaysLoaded()){
            MinecraftForge.EVENT_BUS.register(new GatewaysToEternityCompat());
        }
        ChampionsRegistry.register(modEventBus);
    }

    public static Champions getInstance() {
        return INSTANCE;
    }
}
