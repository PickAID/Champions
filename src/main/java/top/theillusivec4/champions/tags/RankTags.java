package top.theillusivec4.champions.tags;

import net.minecraft.tags.TagKey;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.registries.Registries;
import top.theillusivec4.champions.util.Utils;

public final class RankTags {
  public static final TagKey<Rank> ORDER = create("order");

  private static TagKey<Rank> create(String name) {
    return TagKey.create(Registries.RANK, Utils.id(name));
  }

  private RankTags() {
  }
}
