package top.theillusivec4.champions.champion.entity.config;

import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.DifficultyInstance;
import top.theillusivec4.champions.champion.ChampionConfig;
import top.theillusivec4.champions.champion.DifficultyBasedValue;
import top.theillusivec4.champions.champion.affix.Affix;

public record EntityConfig(
  DifficultyBasedValue level,
  DifficultyBasedValue color,
  Component prefixName,
  HolderSet<Affix> affixes,
  DifficultyBasedValue affixAmount
) {
  public ChampionConfig provide(DifficultyInstance instance) {
    return null;
  }
}
