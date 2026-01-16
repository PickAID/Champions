package top.theillusivec4.champions.tag;

import net.minecraft.tags.TagKey;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.util.Util;

public interface AffixTags {
  TagKey<Affix> DAMAGE_PROTECTION_EXCLUSIVE = create("exclusive_set/damage_protection");
  TagKey<Affix> DAMAGE_IMMUNITY_EXCLUSIVE = create("exclusive_set/damage_immunity");
  TagKey<Affix> DAMAGE_EXCLUSIVE = create("exclusive_set/damage");
  TagKey<Affix> RANGE_EXCLUSIVE = create("exclusive_set/range");
  TagKey<Affix> ATTACK_EXCLUSIVE = create("exclusive_set/attack");
  TagKey<Affix> CURSE = create("curse");

  private static TagKey<Affix> create(String name) {
    return TagKey.create(Registries.AFFIX, Util.id(name));
  }
}
