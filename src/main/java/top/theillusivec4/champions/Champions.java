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
import top.theillusivec4.champions.api.ChampionHandlerEventListener;
import top.theillusivec4.champions.api.ChampionsApiImpl;
import top.theillusivec4.champions.api.IChampionsApi;
import top.theillusivec4.champions.api.affix.AffixEffectComponentTypes;
import top.theillusivec4.champions.api.affix.effect.AffixLocationBasedEffectTypes;
import top.theillusivec4.champions.api.affix.effect.entity.AffixEntityEffectTypes;
import top.theillusivec4.champions.api.affix.effect.value.AffixValueEffectTypes;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootContextBasedValueTypes;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootParamSourceTypes;
import top.theillusivec4.champions.common.attachments.AttachmentTypes;
import top.theillusivec4.champions.common.capabilities.Capabilities;
import top.theillusivec4.champions.common.commands.Commands;
import top.theillusivec4.champions.common.datagen.DataGenerationEventListener;
import top.theillusivec4.champions.common.effect.MobEffects;
import top.theillusivec4.champions.common.entity.EntityTypes;
import top.theillusivec4.champions.common.item.DataComponentTypes;
import top.theillusivec4.champions.common.item.Items;
import top.theillusivec4.champions.common.item.components.ItemEventListener;
import top.theillusivec4.champions.common.loot.predicates.LootItemConditionTypes;
import top.theillusivec4.champions.common.particles.ParticleTypes;
import top.theillusivec4.champions.common.registries.BuiltInRegistries;

@Mod(Champions.MODID)
public class Champions {
  public static final String MODID = "champions";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final IChampionsApi API = ChampionsApiImpl.getInstance();
  // champion instance
  private static Champions INSTANCE;
  public final ModContainer modContainer;

  public static Champions getInstance() {
    return INSTANCE;
  }

  public Champions(IEventBus modEventBus, ModContainer modContainer) {
    this.modContainer = modContainer;
    INSTANCE = this;
//    modEventBus.register(new ModEventHandler());

    Items.register(modEventBus);
    ParticleTypes.register(modEventBus);
    MobEffects.register(modEventBus);
    EntityTypes.register(modEventBus);
//    LootModifiers.register(modEventBus);
//    Stats.register(modEventBus);
    DataComponentTypes.register(modEventBus);
    Capabilities.register(modEventBus);

//    ChampionsRegistry.register(modEventBus);

    BuiltInRegistries.register(modEventBus);
    AffixEffectComponentTypes.register(modEventBus);
    LootContextBasedValueTypes.register(modEventBus);
    AffixValueEffectTypes.register(modEventBus);
    AffixEntityEffectTypes.register(modEventBus);
    AffixLocationBasedEffectTypes.register(modEventBus);
    LootParamSourceTypes.register(modEventBus);
    AttachmentTypes.register(modEventBus);
    LootItemConditionTypes.register(modEventBus);
    DataGenerationEventListener.register(modEventBus);

    ChampionHandlerEventListener.register();
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
