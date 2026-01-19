package top.theillusivec4.champions.data.registry;

import net.minecraft.core.RegistrySetBuilder;
import top.theillusivec4.champions.champion.affix.Affixes;
import top.theillusivec4.champions.champion.item.ChampionSpawnEggs;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.world.damagesource.DamageTypes;

public final class ChampionsRegistries {
  public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
    .add(Registries.AFFIX, Affixes::bootstrap)
    .add(Registries.RANK, Ranks::bootstrap)
    .add(Registries.SPAWN_EGG, ChampionSpawnEggs::bootstrap)
    .add(net.minecraft.core.registries.Registries.DAMAGE_TYPE, DamageTypes::bootstrap);

  private ChampionsRegistries() {
  }
}
