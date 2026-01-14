package top.theillusivec4.champions.champion.entity.config;

import net.minecraft.core.HolderSet;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import top.theillusivec4.champions.champion.ChampionConfig;
import top.theillusivec4.champions.champion.DifficultyBasedValue;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.value.based.lootcontext.LevelBasedValue;

import java.util.Optional;

public record EntityConfig(
  Optional<LootItemCondition> requirements,
  DifficultyBasedValue level,
  HolderSet<Affix> affixes,
  LevelBasedValue affixCount
) {
  public ChampionConfig provide(DifficultyInstance instance) {
    return null;
  }

  public record EntityConfigEntry(
    Optional<LootItemCondition> requirements,
    int weight,
    DifficultyBasedValue level,
    HolderSet<Affix> affixes,
    LevelBasedValue affixCount
  ) {

  }
}
