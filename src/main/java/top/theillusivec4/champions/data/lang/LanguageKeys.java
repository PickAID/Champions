package top.theillusivec4.champions.data.lang;

import net.minecraft.resources.Identifier;
import top.theillusivec4.champions.stats.Stats;
import top.theillusivec4.champions.util.Util;

/**
 * 翻译键
 */
public final class LanguageKeys {
  // 物品
  public static final String ITEM_CHAMPION_SPAWN_EGG_KEY = "suffix.item.champion_spawn_egg";
  // 物品工具栏
  public static final String TOOLTIP_RANK_KEY = tooltipKey("rank");
  public static final String TOOLTIP_LEVEL_KEY = tooltipKey("level");
  public static final String TOOLTIP_COLOR_KEY = tooltipKey("color");
  public static final String TOOLTIP_PREFIX_NAME_KEY = tooltipKey("prefix_name");
  public static final String TOOLTIP_AFFIXES_KEY = tooltipKey("affixes");
  public static final String TOOLTIP_BOSS_KEY = tooltipKey("boss");
  public static final String TOOLTIP_IS_BOSS_KEY = tooltipKey("boss.is");
  public static final String TOOLTIP_NOT_BOSS_KEY = tooltipKey("boss.not");
  // 命令
  public static final String COMMANDS_AFFIX_SUCCESS_KEY = commandsKey("affix", CommandsKeyType.SUCCESS);
  public static final String COMMANDS_RANK_SUCCESS_KEY = commandsKey("rank", CommandsKeyType.SUCCESS);
  public static final String COMMANDS_LEVEL_SUCCESS_KEY = commandsKey("level", CommandsKeyType.SUCCESS);
  public static final String COMMANDS_BOSS_SUCCESS_KEY = commandsKey("boss", CommandsKeyType.SUCCESS);
  public static final String COMMANDS_COLOR_SUCCESS_KEY = commandsKey("color", CommandsKeyType.SUCCESS);
  public static final String COMMANDS_CONFIG_SELECTOR_SUCCESS_KEY = commandsKey("config_selector", CommandsKeyType.SUCCESS);
  public static final String COMMANDS_ERROR_NO_CONFIG_SELECTOR_ON_CLIENT_KEY = commandsErrorKey("no_config_selector_on_client");
  public static final String COMMANDS_ERROR_INVALID_CONFIG_SELECTOR_KEY = commandsErrorKey("invalid_config_selector");
  public static final String COMMANDS_ERROR_INVALID_CHAMPION_ENTITY_KEY = commandsErrorKey("invalid_champion_entity");
  // 创造模式物品栏
  public static final String ITEM_GROUP_CHAMPION_SPAWN_EGGS = itemGroup("champion_spawn_eggs");
  public static final String ITEM_GROUP_CUSTOM_CHAMPION_SPAWN_EGGS = itemGroup("custom_champion_spawn_eggs");
  // 配置
  public static final String CONFIG_DISPLAY_CHAMPION_OVERLAY_KEY = configKey("display_champion_overlay");
  // 统计信息
  public static final String STAT_CHAMPION_MOBS_KILLED_KEY = statKey(Stats.CHAMPION_MOBS_KILLED.getId());

  static String itemGroup(String name) {
    return net.minecraft.util.Util.makeDescriptionId("itemGroup", Util.id(name));
  }

  static String tooltipKey(String name) {
    return net.minecraft.util.Util.makeDescriptionId("tooltip", Util.id(name));
  }

  static String commandsErrorKey(String name) {
    return net.minecraft.util.Util.makeDescriptionId("error", Util.id(name));
  }

  static String commandsKey(String name, CommandsKeyType type) {
    return net.minecraft.util.Util.makeDescriptionId("commands", Util.id(name)) + "." + type.getName();
  }

  static String commandsKey(String name, CommandsKeyType type, String message) {
    return commandsKey(name, type) + "." + message;
  }

  static String configKey(String name) {
    return net.minecraft.util.Util.makeDescriptionId("configuration", Util.id(name));
  }

  static String statKey(Identifier id) {
    return net.minecraft.util.Util.makeDescriptionId("stat", id);
  }

  private LanguageKeys() {
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
