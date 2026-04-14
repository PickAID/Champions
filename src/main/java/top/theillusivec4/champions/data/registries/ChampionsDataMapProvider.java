package top.theillusivec4.champions.data.registries;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;
import top.theillusivec4.champions.core.registries.ChampionsDataMaps;
import top.theillusivec4.champions.world.entity.affix.Affixable;
import top.theillusivec4.champions.world.entity.champion.ChampionMobPreset;

import java.util.concurrent.CompletableFuture;

public class ChampionsDataMapProvider extends DataMapProvider {

  public ChampionsDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries);
  }

  public static ChampionsDataMapProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries){
    return new ChampionsDataMapProvider(output, registries);
  }

  @Override
  protected void gather(HolderLookup.Provider provider) {
    Builder<Affixable, EntityType<?>> affixableBuilder = builder(ChampionsDataMaps.AFFIXABLE);
    Builder<ChampionMobPreset, EntityType<?>> championMobPresetBuilder = builder(ChampionsDataMaps.CHAMPION_MOB_PRESET);
    Affixable.bootstrap(provider, affixableBuilder);
    ChampionMobPreset.bootstrap(provider, championMobPresetBuilder);
  }
}
