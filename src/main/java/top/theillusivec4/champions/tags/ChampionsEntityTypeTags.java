package top.theillusivec4.champions.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import top.theillusivec4.champions.util.ChampionsUtil;

public class ChampionsEntityTypeTags {
	public static final TagKey<EntityType<?>> IS_ENDER = create("is_ender");
	public static final TagKey<EntityType<?>> ALLOW_CHAMPIONS = create("allow_champions");

	private static TagKey<EntityType<?>> create(String name) {
		return TagKey.create(Registries.ENTITY_TYPE, ChampionsUtil.id(name));
	}
}
