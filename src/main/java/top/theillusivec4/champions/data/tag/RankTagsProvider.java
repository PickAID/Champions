package top.theillusivec4.champions.data.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.tag.RankTags;

import java.util.concurrent.CompletableFuture;

public class RankTagsProvider extends KeyTagProvider<Rank> {
  public RankTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
    super(output, ChampionsRegistries.RANK, lookupProvider, modId);
  }

  public RankTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(output, ChampionsRegistries.RANK, lookupProvider, Champions.MODID);
  }

  @Override
  protected void addTags(HolderLookup.Provider registries) {
    tag(RankTags.ORDER)
      .add(Ranks.COMMON)
      .add(Ranks.SKILLED)
      .add(Ranks.ELITE)
      .add(Ranks.LEGENDARY)
      .add(Ranks.ULTIMATE);
  }
}
