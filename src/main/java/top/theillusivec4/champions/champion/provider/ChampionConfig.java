package top.theillusivec4.champions.champion.provider;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;

public record ChampionConfig(
  HolderSet<Affix> affixes,
  Holder<Rank> rank,
  Component prefix,
  int level,
  int color,
  boolean boss
) {
  public void apply(Entity entity) {

  }
}
