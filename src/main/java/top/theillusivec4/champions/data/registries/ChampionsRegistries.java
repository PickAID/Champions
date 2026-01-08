package top.theillusivec4.champions.data.registries;

import net.minecraft.core.RegistrySetBuilder;
import top.theillusivec4.champions.champion.affix.Affixes;
import top.theillusivec4.champions.champion.item.ChampionSpawnEggs;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.registries.Registries;

public final class ChampionsRegistries {
  public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
    .add(Registries.AFFIX, Affixes::bootstrap)
    .add(Registries.RANK, Ranks::bootstrap)
    .add(Registries.CHAMPION_SPAWN_EGG, ChampionSpawnEggs::bootstrap);

  private ChampionsRegistries() {
  }
}
