package top.theillusivec4.champions.data.lang;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class LanguageUtil {
	private LanguageUtil() {
	}

	public static MutableComponent getPrefixComponent(int level) {
		return Component.translatable(LanguageKeys.getPrefixKey(level));
	}

	public static MutableComponent getLevelComponent(int level) {
		return Component.translatable(LanguageKeys.getLevelKey(level));
	}

	public static MutableComponent getColorComponent(int color) {
		return Component.literal("■").withColor(color);
	}

	public static MutableComponent getBossStatusComponent(boolean boss) {
		return boss ? Component.translatable(LanguageKeys.TOOLTIP_IS_BOSS) : Component.translatable(LanguageKeys.TOOLTIP_NOT_BOSS);
	}
}
