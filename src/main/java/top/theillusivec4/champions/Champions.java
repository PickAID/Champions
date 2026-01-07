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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.champion.affix.AffixEffectComponents;
import top.theillusivec4.champions.champion.EntityEventListener;
import top.theillusivec4.champions.champion.affix.effect.AffixLocationBasedEffects;
import top.theillusivec4.champions.champion.affix.effect.entity.AffixEntityEffects;
import top.theillusivec4.champions.champion.affix.effect.value.AffixValueEffects;
import top.theillusivec4.champions.champion.affix.lootcontextbasedvalue.LootContextBasedValues;
import top.theillusivec4.champions.champion.affix.lootcontextbasedvalue.LootParamSourceTypes;
import top.theillusivec4.champions.attachments.Attachments;
import top.theillusivec4.champions.capabilities.Capabilities;
import top.theillusivec4.champions.commands.Commands;
import top.theillusivec4.champions.components.DataComponents;
import top.theillusivec4.champions.data.DataEventListener;
import top.theillusivec4.champions.deprecated.api.ChampionsApiImpl;
import top.theillusivec4.champions.deprecated.api.IChampionsApi;
import top.theillusivec4.champions.effects.MobEffects;
import top.theillusivec4.champions.entities.EntityTypes;
import top.theillusivec4.champions.items.ItemEventListener;
import top.theillusivec4.champions.items.Items;
import top.theillusivec4.champions.loot.predicates.LootItemConditions;
import top.theillusivec4.champions.particles.ParticleTypes;
import top.theillusivec4.champions.registries.BuiltInRegistries;
import top.theillusivec4.champions.stats.Stats;

@Mod(Champions.MODID)
public class Champions {
  public static final String MODID = "champions";
  public static final Logger LOGGER = LogManager.getLogger();
  @Deprecated
  public static final IChampionsApi API = ChampionsApiImpl.getInstance();
  // champion instance
  @Deprecated
  private static Champions INSTANCE;
  @Deprecated
  public final ModContainer modContainer;

  public static Champions getInstance() {
    return INSTANCE;
  }

  public Champions(IEventBus modEventBus, ModContainer modContainer) {
//    modEventBus.register(this);
    this.modContainer = modContainer;
    INSTANCE = this;
//    modEventBus.register(new ModEventHandler());

    Items.register(modEventBus);
    ParticleTypes.register(modEventBus);
    MobEffects.register(modEventBus);
    EntityTypes.register(modEventBus);
//    LootModifiers.register(modEventBus);
    Stats.register(modEventBus);
    DataComponents.register(modEventBus);
//    Stats.register(modEventBus);
    Capabilities.register(modEventBus);

//    ChampionsRegistry.register(modEventBus);

    BuiltInRegistries.register(modEventBus);
    AffixEffectComponents.register(modEventBus);
    LootContextBasedValues.register(modEventBus);
    AffixValueEffects.register(modEventBus);
    AffixEntityEffects.register(modEventBus);
    AffixLocationBasedEffects.register(modEventBus);
    LootParamSourceTypes.register(modEventBus);
    Attachments.register(modEventBus);
    LootItemConditions.register(modEventBus);

    DataEventListener.register(modEventBus);
    EntityEventListener.register();
    ItemEventListener.register();
    Commands.register();

    // register champions config
//    modContainer.registerConfig(ModConfig.Type.COMMON, ChampionsConfig.COMMON_SPEC);
//    modContainer.registerConfig(ModConfig.Type.SERVER, ChampionsConfig.SERVER_SPEC);
//    modContainer.registerConfig(ModConfig.Type.CLIENT, ClientChampionsConfig.CLIENT_SPEC);


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

}
