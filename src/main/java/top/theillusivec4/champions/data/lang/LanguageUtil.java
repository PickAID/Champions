package top.theillusivec4.champions.data.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class LanguageUtil {
  public static MutableComponent getLevelComponent(int level) {
    if (level <= 5) {
      return Component.translatable(getLevelKey(level));
    } else {
      return Component.literal("⭐x" + level);
    }
  }

  public static MutableComponent getColorComponent(int color) {
    return Component.literal("■").withColor(color);
  }

  public static String getLevelKey(int level) {
    return LanguageKeys.tooltipKey("champions.level." + level);
  }

  private LanguageUtil() {
  }
}
