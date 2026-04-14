package top.theillusivec4.champions.world.item.champion;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.ChampionsUtil;
import top.theillusivec4.champions.world.entity.affix.Affix;
import top.theillusivec4.champions.world.entity.affix.Affixes;
import top.theillusivec4.champions.world.entity.affix.EntityAffixes;
import top.theillusivec4.champions.world.entity.champion.Rank;
import top.theillusivec4.champions.world.entity.champion.Ranks;
import top.theillusivec4.champions.world.entity.champion.property.provider.ChampionMobPropertyProvider;

public final class ChampionEggTemplates {
  public static final ResourceKey<ChampionEggTemplate> HUSK = register("husk");
  public static final ResourceKey<ChampionEggTemplate> MAGNETIC_HUSK = register("magnetic_husk");

  private ChampionEggTemplates() {
  }

  private static ResourceKey<ChampionEggTemplate> register(String name) {
    return ResourceKey.create(ChampionsRegistries.CHAMPION_MOB_EGG, ChampionsUtil.id(name));
  }

  public static void bootstrap(BootstrapContext<ChampionEggTemplate> context) {
    HolderGetter<Affix> affixes = context.lookup(ChampionsRegistries.AFFIX);
    HolderGetter<Rank> ranks = context.lookup(ChampionsRegistries.RANK);
    register(
      context,
      HUSK,
      ChampionEggTemplate.builder(() -> (net.minecraft.world.item.SpawnEggItem) Items.HUSK_SPAWN_EGG)
        .affixes(
          EntityAffixes.builder()
            .add(affixes.getOrThrow(Affixes.ADAPTABLE), 2)
        )
        .property(ChampionMobPropertyProvider.byRank(ranks.getOrThrow(Ranks.SKILLED)))
    );
    register(
      context,
      MAGNETIC_HUSK,
      ChampionEggTemplate.builder(() -> (net.minecraft.world.item.SpawnEggItem) Items.HUSK_SPAWN_EGG)
        .affixes(
          EntityAffixes.builder()
            .add(affixes.getOrThrow(Affixes.MAGNETIC), 2)
        )
        .property(ChampionMobPropertyProvider.byRank(ranks.getOrThrow(Ranks.SKILLED)))
    );
  }

  private static void register(BootstrapContext<ChampionEggTemplate> context, ResourceKey<ChampionEggTemplate> key, ChampionEggTemplate.Builder builder) {
    context.register(key, builder.build());
  }
}
