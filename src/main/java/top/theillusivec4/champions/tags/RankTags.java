package top.theillusivec4.champions.tags;

import net.minecraft.tags.TagKey;
import top.theillusivec4.champions.world.entity.champion.Rank;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.ChampionsUtil;

public interface RankTags {
  TagKey<Rank> ORDER = create("order");

  private static TagKey<Rank> create(String name) {
    return TagKey.create(ChampionsRegistries.RANK, ChampionsUtil.id(name));
  }


}
