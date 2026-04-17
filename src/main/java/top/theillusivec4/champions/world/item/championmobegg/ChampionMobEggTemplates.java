package top.theillusivec4.champions.world.item.championmobegg;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import top.theillusivec4.champions.api.championmob.ChampionMobEggTemplate;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.EntityAffixes;
import top.theillusivec4.champions.world.entity.affix.Affixes;
import top.theillusivec4.champions.api.championmob.Rank;
import top.theillusivec4.champions.world.entity.championmob.Ranks;
import top.theillusivec4.champions.api.championmob.provider.ChampionMobPropertyProvider;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class ChampionMobEggTemplates {
  public static final ResourceKey<ChampionMobEggTemplate> HUSK = register("husk");
  public static final ResourceKey<ChampionMobEggTemplate> MAGNETIC_HUSK = register("magnetic_husk");

  private ChampionMobEggTemplates() {
  }

  private static ResourceKey<ChampionMobEggTemplate> register(String name) {
    return ResourceKey.create(ChampionsRegistries.CHAMPION_MOB_EGG, ChampionsUtil.id(name));
  }

  public static void bootstrap(BootstrapContext<ChampionMobEggTemplate> context) {
    HolderGetter<Affix> affixes = context.lookup(ChampionsRegistries.AFFIX);
    HolderGetter<Rank> ranks = context.lookup(ChampionsRegistries.RANK);
    register(
      context,
      HUSK,
      ChampionMobEggTemplate.builder(() -> (net.minecraft.world.item.SpawnEggItem) Items.HUSK_SPAWN_EGG)
        .affixes(
          EntityAffixes.builder()
            .add(affixes.getOrThrow(Affixes.ADAPTABLE), 2)
        )
        .property(ChampionMobPropertyProvider.byRank(ranks.getOrThrow(Ranks.SKILLED)))
    );
    register(
      context,
      MAGNETIC_HUSK,
      ChampionMobEggTemplate.builder(() -> (net.minecraft.world.item.SpawnEggItem) Items.HUSK_SPAWN_EGG)
        .affixes(
          EntityAffixes.builder()
            .add(affixes.getOrThrow(Affixes.MAGNETIC), 2)
        )
        .property(ChampionMobPropertyProvider.byRank(ranks.getOrThrow(Ranks.SKILLED)))
    );
  }

  private static void register(BootstrapContext<ChampionMobEggTemplate> context, ResourceKey<ChampionMobEggTemplate> key, ChampionMobEggTemplate.Builder builder) {
    context.register(key, builder.build());
  }
}
