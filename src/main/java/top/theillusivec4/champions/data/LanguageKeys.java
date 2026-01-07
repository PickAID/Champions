package top.theillusivec4.champions.data;

import net.minecraft.network.chat.Component;

public final class LanguageKeys {
  public static final String RANK_TOOLTIP_KEY = "champions.rank.tooltip";
  public static final String LEVEL_TOOLTIP_KEY = "champions.level.tooltip";
  public static final String COLOR_TOOLTIP_KEY = "champions.color.tooltip";
  public static final String PREFIX_NAME_TOOLTIP_KEY = "champions.prefix_name.tooltip";
  public static final String AFFIXES_TOOLTIP_KEY = "affixes.tooltip";
  static final String LEVEL_PREFIX = "champions.level";

  public static Component getLevelName(int level) {
    return Component.translatable(getLevelKey(level));
  }

  public static Component getColorName(int color) {
    return Component.literal("■").withColor(color);
  }

  static String getLevelKey(int level) {
    return LEVEL_PREFIX + "." + level;
  }

  private LanguageKeys() {
  }
}
