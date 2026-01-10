package top.theillusivec4.champions.data.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.util.Utils;

public final class LanguageKeys {
  // Item
  public static final String SUFFIX_ITEM_CHAMPION_SPAWN_EGG = "suffix.item.champion_spawn_egg";
  // ToolTip
  public static final String TOOLTIP_RANK_KEY = tooltipKey("rank");
  public static final String TOOLTIP_LEVEL_KEY = tooltipKey("level");
  public static final String TOOLTIP_COLOR_KEY = tooltipKey("color");
  public static final String TOOLTIP_PREFIX_NAME_KEY = tooltipKey("prefix_name");
  public static final String TOOLTIP_AFFIXES_KEY = tooltipKey("affixes");
  public static final String TOOLTIP_BOSS_KEY = tooltipKey("boss");
  public static final String TOOLTIP_IS_BOSS_KEY = tooltipKey("boss.is");
  public static final String TOOLTIP_NOT_BOSS_KEY = tooltipKey("boss.not");
  // Commands
  public static final String COMMANDS_AFFIX_SUCCESS_KEY = commandsKey("affix.success");
  public static final String COMMANDS_LEVEL_SUCCESS_KEY = commandsKey("level.success");
  // CreativeModeTab
  public static final String ITEM_GROUP_CHAMPION_SPAWN_EGGS = itemGroup("champion_spawn_eggs");
  public static final String ITEM_GROUP_CUSTOM_CHAMPION_SPAWN_EGGS = itemGroup("custom_champion_spawn_eggs");

  public static Component getLevelComponent(int level) {
    return Component.translatable(tooltipLevelKey(level));
  }

  public static Component getColorComponent(int color) {
    return Component.literal("■").withColor(color);
  }

  static String tooltipLevelKey(int level) {
    return tooltipKey("champions.level." + level);
  }

  private static String itemGroup(String name) {
    return "itemGroup." + Champions.MODID + "." + name;
  }

  private static String tooltipKey(String name) {
    return Util.makeDescriptionId("tooltip", Utils.id(name));
  }

  private static String commandsKey(String name) {
    return Util.makeDescriptionId("commands", Utils.id(name));
  }

  private LanguageKeys() {
  }
}
