package top.theillusivec4.champions.data.registries;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.world.item.championmobegg.ChampionMobEggTemplates;
import top.theillusivec4.champions.world.entity.championmob.Ranks;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.world.damagesource.ChampionsDamageTypes;
import top.theillusivec4.champions.world.entity.affix.Affixes;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class ModdedRegistries {
  private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
    .add(Registries.DAMAGE_TYPE, ChampionsDamageTypes::bootstrap)
    .add(ChampionsRegistries.AFFIX, Affixes::bootstrap)
    .add(ChampionsRegistries.RANK, Ranks::bootstrap)
    .add(ChampionsRegistries.CHAMPION_MOB_EGG, ChampionMobEggTemplates::bootstrap);

  private ModdedRegistries() {
  }

  public static DatapackBuiltinEntriesProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    return new DatapackBuiltinEntriesProvider(output, registries, BUILDER, Set.of(Champions.MOD_ID));
  }
}
