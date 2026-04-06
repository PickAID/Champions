package top.theillusivec4.champions.tag;

import net.minecraft.tags.TagKey;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.Util;

public final class AffixTags {
	public static final TagKey<Affix> DAMAGE_PROTECTION_EXCLUSIVE = create("exclusive_set/damage_protection");
	public static final TagKey<Affix> DAMAGE_IMMUNITY_EXCLUSIVE = create("exclusive_set/damage_immunity");
	public static final TagKey<Affix> DAMAGE_EXCLUSIVE = create("exclusive_set/damage");
	public static final TagKey<Affix> RANGE_EXCLUSIVE = create("exclusive_set/range");
	public static final TagKey<Affix> ATTACK_EXCLUSIVE = create("exclusive_set/attack");
	public static final TagKey<Affix> CURSE = create("curse");

	private static TagKey<Affix> create(String name) {
		return TagKey.create(ChampionsRegistries.AFFIX, Util.id(name));
	}
}
