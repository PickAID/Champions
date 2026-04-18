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

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.advancements.criterion.ChampionsEntitySubPredicates;
import top.theillusivec4.champions.core.attachments.ChampionsAttachments;
import top.theillusivec4.champions.core.components.ChampionsDataComponents;
import top.theillusivec4.champions.core.particles.ChampionsParticleTypes;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.core.registries.ChampionsDataMaps;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.data.languages.ChampionsLanguageProvider;
import top.theillusivec4.champions.data.registries.ChampionsDataMapProvider;
import top.theillusivec4.champions.data.registries.ModdedRegistries;
import top.theillusivec4.champions.data.tags.AffixTagsProvider;
import top.theillusivec4.champions.network.ChampionsBossEventPayload;
import top.theillusivec4.champions.network.ChampionsPayloads;
import top.theillusivec4.champions.server.ChampionsServerConfig;
import top.theillusivec4.champions.stats.ChampionsStats;
import top.theillusivec4.champions.world.effect.ChampionsMobEffects;
import top.theillusivec4.champions.world.entity.ChampionsEntityTypes;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.AffixEffectComponents;
import top.theillusivec4.champions.world.entity.affix.LevelBasedValues;
import top.theillusivec4.champions.world.entity.affix.ProjectileTemplates;
import top.theillusivec4.champions.world.entity.affix.effects.AffixEntityEffects;
import top.theillusivec4.champions.world.entity.affix.effects.AffixLocationBasedEffects;
import top.theillusivec4.champions.world.entity.affix.effects.AffixValueEffects;
import top.theillusivec4.champions.api.affix.provider.AffixProvider;
import top.theillusivec4.champions.world.entity.affix.providers.AffixProviders;
import top.theillusivec4.champions.api.championmob.Rank;
import top.theillusivec4.champions.world.entity.championmob.providers.ChampionPropertyProviders;
import top.theillusivec4.champions.world.item.ChampionsCreativeModeTabs;
import top.theillusivec4.champions.world.item.champion.ChampionEggTemplate;
import top.theillusivec4.champions.world.level.storage.loot.predicates.ChampionsLootItemConditions;

import java.util.concurrent.CompletableFuture;

@Mod(Champions.MOD_ID)
public class Champions {
	public static final String MOD_ID = "champions";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String VERSION = "26.1.0.1-beta-1";

	public Champions(IEventBus bus, ModContainer container) {
		bus.register(this);
		container.registerConfig(ModConfig.Type.SERVER, ChampionsServerConfig.SPEC);
		ChampionsAttachments.register(bus);
		ChampionsDataComponents.register(bus);
		ChampionsEntityTypes.register(bus);
		ChampionsMobEffects.register(bus);
		ChampionsParticleTypes.register(bus);
		ChampionsLootItemConditions.register(bus);
		ChampionsCreativeModeTabs.register(bus);
		ChampionsEntitySubPredicates.register(bus);
		ChampionsStats.register(bus);
		AffixEffectComponents.register(bus);
		ProjectileTemplates.register(bus);
		LevelBasedValues.register(bus);
		AffixLocationBasedEffects.register(bus);
		AffixEntityEffects.register(bus);
		AffixValueEffects.register(bus);
		AffixProviders.register(bus);
		ChampionPropertyProviders.register(bus);
	}

	@SubscribeEvent
	public void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(ChampionsRegistries.AFFIX, Affix.DIRECT_CODEC, Affix.DIRECT_CODEC);
		event.dataPackRegistry(ChampionsRegistries.AFFIX_PROVIDER, AffixProvider.DIRECT_CODEC, AffixProvider.DIRECT_CODEC);
		event.dataPackRegistry(ChampionsRegistries.RANK, Rank.DIRECT_CODEC, Rank.DIRECT_CODEC);
		event.dataPackRegistry(ChampionsRegistries.CHAMPION_MOB_EGG, ChampionEggTemplate.DIRECT_CODEC, ChampionEggTemplate.DIRECT_CODEC);
	}

	@SubscribeEvent
	public void registerRegistries(NewRegistryEvent event) {
		event.register(ChampionsBuiltInRegistries.AFFIX_EFFECT_COMPONENT_TYPE);
		event.register(ChampionsBuiltInRegistries.AFFIX_VALUE_EFFECT_TYPE);
		event.register(ChampionsBuiltInRegistries.AFFIX_ENTITY_EFFECT_TYPE);
		event.register(ChampionsBuiltInRegistries.AFFIX_LOCATION_BASED_EFFECT_TYPE);
		event.register(ChampionsBuiltInRegistries.LEVEL_BASED_VALUE_TYPE);
		event.register(ChampionsBuiltInRegistries.PROJECTILE_TEMPLATE_TYPE);
		event.register(ChampionsBuiltInRegistries.AFFIX_PROVIDER_TYPE);
		event.register(ChampionsBuiltInRegistries.CHAMPION_PROPERTY_PROVIDER_TYPE);
	}

	@SubscribeEvent
	public void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
		var registrar = event.registrar(VERSION);
		registrar.playToClient(ChampionsPayloads.BOSS_EVENT, ChampionsBossEventPayload.STREAM_CODEC);
	}

	@SubscribeEvent
	public void registerDataMaps(RegisterDataMapTypesEvent event) {
		event.register(ChampionsDataMaps.AFFIXABLE);
		event.register(ChampionsDataMaps.CHAMPION_MOB_PRESET);
	}

	@SubscribeEvent
	public void generateData(GatherDataEvent.Client event) {
		DataGenerator dataGenerator = event.getGenerator();
		PackOutput output = dataGenerator.getPackOutput();

		event.createDatapackRegistryObjects(ModdedRegistries.BUILDER);
		CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
		event.addProvider(AffixTagsProvider.create(output, registries));
		event.addProvider(ChampionsDataMapProvider.create(output, registries));
		event.addProvider(ChampionsLanguageProvider.zhCn(output));
		event.addProvider(ChampionsLanguageProvider.enUs(output));
	}

	@SubscribeEvent
	public void registerPlugins(InterModEnqueueEvent event) {
//		if (ModList.get().isLoaded("theoneprobe")) {
//			InterModComms.sendTo(MOD_ID, "theoneprobe", "getTheOneProbe", ChampionsTheOneProbePlugin::create);
//		}
	}

}
