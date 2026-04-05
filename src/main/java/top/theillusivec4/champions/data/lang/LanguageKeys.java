package top.theillusivec4.champions.data.lang;

import net.minecraft.resources.Identifier;
import top.theillusivec4.champions.stats.Stats;
import top.theillusivec4.champions.util.Util;

/**
 * 翻译键
 */
public final class LanguageKeys {
	// 物品
	public static final String ITEM_CHAMPION_SPAWN_EGG = "champion.spawn_egg";
	// Tooltip
	public static final String TOOLTIP_LEVEL = tooltipKey("champion.level");
	public static final String TOOLTIP_COLOR = tooltipKey("champion.color");
	public static final String TOOLTIP_PREFIX_NAME = tooltipKey("champion.prefix_name");
	public static final String TOOLTIP_AFFIXES = tooltipKey("champion.affixes");
	public static final String TOOLTIP_BOSS = tooltipKey("champion.boss");
	public static final String TOOLTIP_IS_BOSS = tooltipKey("boss.is");
	public static final String TOOLTIP_NOT_BOSS = tooltipKey("boss.not");
	// 命令
	public static final String COMMANDS_AFFIX_SUCCESS = commandsKey("affix", CommandsKeyType.SUCCESS);
	public static final String COMMANDS_RANK_SUCCESS = commandsKey("rank", CommandsKeyType.SUCCESS);
	public static final String COMMANDS_LEVEL_SUCCESS = commandsKey("level", CommandsKeyType.SUCCESS);
	public static final String COMMANDS_BOSS_SUCCESS = commandsKey("boss", CommandsKeyType.SUCCESS);
	public static final String COMMANDS_COLOR_SUCCESS = commandsKey("color", CommandsKeyType.SUCCESS);
	// 创造模式物品栏
	public static final String ITEM_GROUP_CHAMPION_SPAWN_EGGS = itemGroup("champion_spawn_eggs");
	public static final String ITEM_GROUP_CUSTOM_CHAMPION_SPAWN_EGGS = itemGroup("custom_champion_spawn_eggs");
	// 配置
	public static final String CONFIG_DISPLAY_CHAMPION_OVERLAY = configKey("display_champion_overlay");
	// 统计信息
	public static final String STAT_CHAMPION_MOBS_KILLED = statKey(Stats.CHAMPION_MOBS_KILLED.getId());

	private LanguageKeys() {
	}

	public static String getLevelKey(int level) {
		return "champion.level." + level;
	}

	private static String itemGroup(String name) {
		return net.minecraft.util.Util.makeDescriptionId("itemGroup", Util.id(name));
	}

	private static String tooltipKey(String name) {
		return "tooltip" + "." + name;
	}

	public static String getPrefixKey(int level) {
		return "champion.prefix." + level;
	}

	private static String commandsErrorKey(String name) {
		return net.minecraft.util.Util.makeDescriptionId("error", Util.id(name));
	}

	private static String commandsKey(String name, CommandsKeyType type) {
		return net.minecraft.util.Util.makeDescriptionId("commands", Util.id(name)) + "." + type.getName();
	}

	private static String commandsKey(String name, CommandsKeyType type, String message) {
		return commandsKey(name, type) + "." + message;
	}

	private static String configKey(String name) {
		return net.minecraft.util.Util.makeDescriptionId("configuration", Util.id(name));
	}

	private static String statKey(Identifier id) {
		return net.minecraft.util.Util.makeDescriptionId("stat", id);
	}

	public enum CommandsKeyType {
		SUCCESS("success"),
		FAILED("failed"),
		ERROR("error");
		private final String name;

		CommandsKeyType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public enum ConfigKeyType {
		CLIENT("client");
		private final String name;

		ConfigKeyType(String name) {
			this.name = name;
		}
	}
}
