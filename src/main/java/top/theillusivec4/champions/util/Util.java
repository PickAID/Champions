package top.theillusivec4.champions.util;

import net.minecraft.resources.Identifier;
import top.theillusivec4.champions.Champions;

public final class Util {
	private Util() {
	}

	public static Identifier id(final String path) {
		return Identifier.fromNamespaceAndPath(Champions.MODID, path);
	}

}
