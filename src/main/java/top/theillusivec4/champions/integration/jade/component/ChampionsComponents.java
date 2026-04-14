package top.theillusivec4.champions.integration.jade.component;

import net.minecraft.resources.Identifier;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class ChampionsComponents {
	public static final Identifier ENTITY_AFFIXES = register("entity_affixes");
	public static final Identifier ENTITY_CHAMPION_PROPERTY = register("entity_champion_property");

	private ChampionsComponents() {
	}

	private static Identifier register(String name) {
		return ChampionsUtil.id(name);
	}
}
