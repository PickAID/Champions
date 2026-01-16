package top.theillusivec4.champions.tag;

import net.minecraft.tags.TagKey;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.util.Util;

public interface RankTags {
  TagKey<Rank> ORDER = create("order");

  private static TagKey<Rank> create(String name) {
    return TagKey.create(Registries.RANK, Util.id(name));
  }


}
