package top.theillusivec4.champions.data.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import top.theillusivec4.champions.util.Utils;

public final class LanguageKeys {
  // ToolTip
  public static final String TOOLTIP_RANK_KEY = tooltipKey("rank");
  public static final String TOOLTIP_LEVEL_KEY = tooltipKey("level");
  public static final String TOOLTIP_COLOR_KEY = tooltipKey("color");
  public static final String TOOLTIP_PREFIX_NAME_KEY = tooltipKey("prefix_name");
  public static final String TOOLTIP_AFFIXES_KEY = tooltipKey("affixes");
  // Commands
  public static final String COMMANDS_AFFIX_SUCCESS_KEY = commandsKey("affix.success");
  public static final String COMMANDS_LEVEL_SUCCESS_KEY = commandsKey("level.success");

  public static Component getLevelName(int level) {
    return Component.translatable(tooltipLevelKey(level));
  }

  public static Component getColorName(int color) {
    return Component.literal("■").withColor(color);
  }

  static String tooltipLevelKey(int level) {
    return tooltipKey("champions.level." + level);
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
