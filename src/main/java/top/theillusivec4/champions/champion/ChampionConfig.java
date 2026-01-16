package top.theillusivec4.champions.champion;

import net.minecraft.advancements.criterion.MinMaxBounds;
import top.theillusivec4.champions.champion.value.based.lootcontext.LevelBasedValue;

public record ChampionConfig(MinMaxBounds.Ints level, LevelBasedValue affixCount) {
  public int calculateLevel(int originLevel) {
    return Math.clamp(originLevel, this.level.min().orElse(1), this.level.max().orElse(5));
  }

  public int calculateAffixCount(int level) {
    return (int) this.affixCount.calculate(level);
  }
}
