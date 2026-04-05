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
import top.theillusivec4.champions.champion.DifficultyBasedValues;
import top.theillusivec4.champions.champion.affix.AffixEffectComponents;
import top.theillusivec4.champions.champion.affix.Projectiles;
import top.theillusivec4.champions.champion.affix.effect.AffixEntityEffects;
import top.theillusivec4.champions.champion.affix.effect.AffixLocationBasedEffects;
import top.theillusivec4.champions.champion.affix.effect.AffixValueEffects;
import top.theillusivec4.champions.champion.value.based.lootcontext.LevelBasedValues;
import top.theillusivec4.champions.command.Commands;
import top.theillusivec4.champions.component.DataComponents;
import top.theillusivec4.champions.config.CommonConfig;
import top.theillusivec4.champions.data.DataEventListener;
import top.theillusivec4.champions.network.protocol.ClientGamePacketListener;
import top.theillusivec4.champions.particle.ParticleTypes;
import top.theillusivec4.champions.registry.BuiltInRegistries;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.server.champion.ChampionConfigManager;
import top.theillusivec4.champions.server.config.ServerConfig;
import top.theillusivec4.champions.stats.Stats;
import top.theillusivec4.champions.util.Util;
import top.theillusivec4.champions.world.effect.MobEffects;
import top.theillusivec4.champions.world.entity.EntityEventListener;
import top.theillusivec4.champions.world.entity.EntityTypes;
import top.theillusivec4.champions.world.item.CreativeModeTabs;
import top.theillusivec4.champions.world.item.ItemEventListener;
import top.theillusivec4.champions.world.item.Items;
import top.theillusivec4.champions.world.loot.modifier.LootModifiers;
import top.theillusivec4.champions.world.loot.predicates.LootItemConditions;
import top.theillusivec4.champions.world.loot.providers.number.NumberProviders;

import java.util.Objects;

@Mod(Champions.MODID)
public class Champions {
	public static final String MODID = "champions";
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String VERSION = "2.1.12.3";
	// champion instance
	private static Champions instance;
	private final CommonConfig commonConfig = new CommonConfig();
	private final ServerConfig serverConfig = new ServerConfig();
	private ChampionConfigManager entityConfigManager;
	private ChampionConfigManager levelConfigManager;

	public Champions(IEventBus modEventBus, ModContainer container) {
		instance = this;
		container.registerConfig(ModConfig.Type.COMMON, this.commonConfig.getConfigSpec());
		container.registerConfig(ModConfig.Type.SERVER, this.serverConfig.getConfigSpec());
		Items.register(modEventBus);
		ParticleTypes.register(modEventBus);
		MobEffects.register(modEventBus);
		EntityTypes.register(modEventBus);
		Stats.register(modEventBus);
		DataComponents.register(modEventBus);
		CreativeModeTabs.register(modEventBus);
//    Capabilities.register(modEventBus);
		BuiltInRegistries.register(modEventBus);
		AffixEffectComponents.register(modEventBus);
		LevelBasedValues.register(modEventBus);
		DifficultyBasedValues.register(modEventBus);
		AffixValueEffects.register(modEventBus);
		AffixEntityEffects.register(modEventBus);
		AffixLocationBasedEffects.register(modEventBus);
		Projectiles.register(modEventBus);
		Attachments.register(modEventBus);
		LootItemConditions.register(modEventBus);
		LootModifiers.register(modEventBus);
		NumberProviders.register(modEventBus);
		DataEventListener.register(modEventBus);
		ClientGamePacketListener.register(modEventBus);
		EntityEventListener.register();
		ItemEventListener.register();
		ReloadEventListener.register();
		Commands.register();
	}

	public static Champions getInstance() {
		return instance;
	}

	public CommonConfig getCommonConfig() {
		return commonConfig;
	}

	public ServerConfig getServerConfig() {
		return serverConfig;
	}

	public ChampionConfigManager getLevelConfigManager() {
		return Objects.requireNonNull(levelConfigManager, "过早的访问实体配置管理器");
	}

	public ChampionConfigManager getEntityConfigManager() {
		return Objects.requireNonNull(entityConfigManager, "过早的访问实体配置管理器");
	}

	public static final class ReloadEventListener {
		public static final Identifier ENTITY_CONFIG_MANAGER = Util.id("entity_config_manager");
		public static final Identifier LEVEL_CONFIG_MANAGER = Util.id("level_config_manager");

		private ReloadEventListener() {
		}

		private static void register() {
			NeoForge.EVENT_BUS.register(new ReloadEventListener());
		}

		@SubscribeEvent
		public void onAddServerReloadListeners(AddServerReloadListenersEvent event) {
			ChampionConfigManager entityConfigManager = new ChampionConfigManager(event.getRegistryAccess(), Registries.ENTITY_CONFIG);
			ChampionConfigManager levelConfigManager = new ChampionConfigManager(event.getRegistryAccess(), Registries.LEVEL_CONFIG);
			event.addListener(ENTITY_CONFIG_MANAGER, entityConfigManager);
			event.addListener(LEVEL_CONFIG_MANAGER, levelConfigManager);
			Champions.instance.entityConfigManager = entityConfigManager;
			Champions.instance.levelConfigManager = levelConfigManager;
		}

	}
}
