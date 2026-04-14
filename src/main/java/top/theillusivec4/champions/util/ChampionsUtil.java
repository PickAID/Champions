package top.theillusivec4.champions.util;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.ChampionsMod;

public final class ChampionsUtil {
	private ChampionsUtil() {
	}

	public static Identifier id(final String path) {
		return Identifier.fromNamespaceAndPath(ChampionsMod.MOD_ID, path);
	}

	public static String makeDescriptionId(String prefix, Identifier id) {
		return makeDescriptionId(prefix, id, null);
	}

	public static String makeDescriptionId(String prefix, Identifier id, @Nullable String suffix) {
		if (suffix != null) {
			return id.toLanguageKey(prefix, suffix);
		} else {
			return id.toLanguageKey(prefix);
		}
	}

}
