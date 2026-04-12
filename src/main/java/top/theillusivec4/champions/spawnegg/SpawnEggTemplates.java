package top.theillusivec4.champions.spawnegg;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.affix.Affixes;
import top.theillusivec4.champions.champion.Rank;
import top.theillusivec4.champions.champion.Ranks;
import top.theillusivec4.champions.champion.provider.ChampionPropertyProvider;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class SpawnEggTemplates {
  public static final ResourceKey<SpawnEggTemplate> HUSK = register("husk");
  public static final ResourceKey<SpawnEggTemplate> MAGNETIC_HUSK = register("magnetic_husk");

  private SpawnEggTemplates() {
  }

  private static ResourceKey<SpawnEggTemplate> register(String name) {
    return ResourceKey.create(ChampionsRegistries.CHAMPION_EGG, ChampionsUtil.id(name));
  }

  public static void bootstrap(BootstrapContext<SpawnEggTemplate> context) {
    HolderGetter<Affix> affixes = context.lookup(ChampionsRegistries.AFFIX);
    HolderGetter<Rank> ranks = context.lookup(ChampionsRegistries.RANK);
    register(
      context,
      HUSK,
      SpawnEggTemplate.builder(() -> (net.minecraft.world.item.SpawnEggItem) Items.HUSK_SPAWN_EGG)
        .affixes(
          AffixContainer.builder()
            .add(affixes.getOrThrow(Affixes.ADAPTABLE), 2)
        )
        .property(ChampionPropertyProvider.rank(ranks.getOrThrow(Ranks.SKILLED)))
    );
    register(
      context,
      MAGNETIC_HUSK,
      SpawnEggTemplate.builder(() -> (net.minecraft.world.item.SpawnEggItem) Items.HUSK_SPAWN_EGG)
        .affixes(
          AffixContainer.builder()
            .add(affixes.getOrThrow(Affixes.MAGNETIC), 2)
        )
        .property(ChampionPropertyProvider.rank(ranks.getOrThrow(Ranks.SKILLED)))
    );
  }

  private static void register(BootstrapContext<SpawnEggTemplate> context, ResourceKey<SpawnEggTemplate> key, SpawnEggTemplate.Builder builder) {
    context.register(key, builder.build());
  }
}
