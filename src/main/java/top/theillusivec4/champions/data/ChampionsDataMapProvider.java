package top.theillusivec4.champions.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.DataMapProvider;
import top.theillusivec4.champions.affix.Affixable;
import top.theillusivec4.champions.championmob.ChampionMobPreset;
import top.theillusivec4.champions.registries.ChampionsDataMaps;

import java.util.concurrent.CompletableFuture;

public class ChampionsDataMapProvider extends DataMapProvider {
  /**
   * Create a new provider.
   *
   * @param packOutput     the output location
   * @param lookupProvider a {@linkplain CompletableFuture} supplying the registries
   */
  public ChampionsDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, lookupProvider);
  }

  @Override
  protected void gather(HolderLookup.Provider provider) {
    Builder<Affixable, EntityType<?>> affixableBuilder = builder(ChampionsDataMaps.AFFIXABLE);
    Builder<ChampionMobPreset, EntityType<?>> championMobPresetBuilder = builder(ChampionsDataMaps.CHAMPION_MOB_PRESET);
    Affixable.bootstrap(provider, affixableBuilder);
    ChampionMobPreset.bootstrap(provider, championMobPresetBuilder);
  }
}
