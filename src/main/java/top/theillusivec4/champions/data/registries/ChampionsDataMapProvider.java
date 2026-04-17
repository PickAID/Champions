package top.theillusivec4.champions.data.registries;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.common.data.DataMapProvider;
import top.theillusivec4.champions.api.affix.Affixable;
import top.theillusivec4.champions.api.affix.provider.AffixProvider;
import top.theillusivec4.champions.api.championmob.provider.ChampionMobPropertyProvider;
import top.theillusivec4.champions.core.registries.ChampionsDataMaps;
import top.theillusivec4.champions.api.championmob.ChampionMobPreset;

import java.util.concurrent.CompletableFuture;

public class ChampionsDataMapProvider extends DataMapProvider {

  public ChampionsDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries);
  }

  public static ChampionsDataMapProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    return new ChampionsDataMapProvider(output, registries);
  }

  @Override
  protected void gather(HolderLookup.Provider provider) {
    Builder<Affixable, EntityType<?>> affixableBuilder = builder(ChampionsDataMaps.AFFIXABLE);
    Builder<ChampionMobPreset, EntityType<?>> championMobPresetBuilder = builder(ChampionsDataMaps.CHAMPION_MOB_PRESET);
    Affixables.bootstrap(provider, affixableBuilder);
    ChampionMobPresets.bootstrap(provider, championMobPresetBuilder);
  }

  public static class Affixables {

    public static void bootstrap(HolderLookup.Provider provider, Builder<Affixable, EntityType<?>> context) {
      for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
        if (entityType.getCategory() != MobCategory.MONSTER) {
          register(context, entityType, 0);
        }
      }
    }

    private static void register(Builder<Affixable, EntityType<?>> context, EntityType<?> entityType, int value) {
      context.add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType), new Affixable(value), false);
    }
  }

  public static class ChampionMobPresets {

    public static void bootstrap(HolderLookup.Provider provider, Builder<ChampionMobPreset, EntityType<?>> context) {
      //    HolderGetter<Affix> affixes = provider.lookupOrThrow(ChampionsRegistries.AFFIX);
      //    HolderGetter<Rank> ranks = provider.lookupOrThrow(ChampionsRegistries.RANK);
      //    register(
      //      context,
      //      EntityType.HUSK,
      //      HolderSet.direct(
      //        Holder.direct(AffixProvider.single(affixes.getOrThrow(Affixes.ADAPTABLE), ConstantInt.of(1)))
      //      ),
      //      ChampionPropertyProvider.byRank(ranks.getOrThrow(Ranks.ELITE))
      //    );
      //    register(
      //      context,
      //      EntityType.ZOMBIE,
      //      HolderSet.direct(
      //        Holder.direct(AffixProvider.single(affixes.getOrThrow(Affixes.ADAPTABLE), UniformInt.of(1, 3))),
      //        Holder.direct(AffixProvider.single(affixes.getOrThrow(Affixes.DAMPENING), UniformInt.of(1, 3)))
      //      ),
      //      ChampionPropertyProvider.single(
      //        ChampionProperty.builder()
      //          .prefix(Component.literal("Test"))
      //          .tier(3)
      //          .color(TextColor.fromLegacyFormat(ChatFormatting.RED))
      //          .boss(false)
      //      )
      //    );
      //    register(
      //      context,
      //      EntityType.SKELETON,
      //      HolderSet.direct(
      //        Holder.direct(AffixProvider.byCost(affixes.getOrThrow(AffixTags.DAMAGE_PROTECTION_EXCLUSIVE), UniformInt.of(20, 60))),
      //        Holder.direct(AffixProvider.byCostWithDifficulty(affixes.getOrThrow(AffixTags.DAMAGE_EXCLUSIVE), 20, 80))
      //      ),
      //      ChampionPropertyProvider.byRank(ranks.getOrThrow(Ranks.COMMON))
      //    );
    }

    private static void register(Builder<ChampionMobPreset, EntityType<?>> context, EntityType<?> entityType, HolderSet<AffixProvider> affixes, ChampionMobPropertyProvider property) {
      context.add(
        BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType),
        new ChampionMobPreset(affixes, property),
        false
      );
    }
  }
}
