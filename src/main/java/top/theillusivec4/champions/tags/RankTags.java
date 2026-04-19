package top.theillusivec4.champions.tags;

import net.minecraft.tags.TagKey;
import top.theillusivec4.champions.api.championmob.Rank;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.Util;

public interface RankTags {
  TagKey<Rank> ORDER = create("order");

  private static TagKey<Rank> create(String name) {
    return TagKey.create(ChampionsRegistries.RANK, Util.id(name));
  }


}
