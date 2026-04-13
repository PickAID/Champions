package top.theillusivec4.champions.championmob;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.affix.AffixHelper;
import top.theillusivec4.champions.affix.AffixInstance;
import top.theillusivec4.champions.championmob.property.ChampionPropertyHelper;
import top.theillusivec4.champions.registries.ChampionsDataMaps;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.server.ChampionsServerConfig;
import top.theillusivec4.champions.util.ChampionsUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class ChampionMobHelper {
  private ChampionMobHelper() {
  }

  public static Optional<Holder<Rank>> selectRank(RandomSource random, Entity entity, Stream<? extends Holder<Rank>> possible) {
    return selectRank(random, entity.getType(), possible);
  }

  public static Optional<Holder<Rank>> selectRank(RandomSource random, EntityType<?> entity, Stream<? extends Holder<Rank>> possible) {
    return ChampionsUtil.getRandom(random,
      possible.filter(rank -> rank.value().isSupported(entity)).toList()
    );
  }

  public static void doApplyPreset(ServerLevel level, Entity entity, DifficultyInstance difficulty) {
    ChampionMobPreset preset = getPreset(entity);
    if (preset != null) {
      preset.apply(level, level.getRandom(), entity, difficulty);
    }
  }

  public static void doFinalizeSpawn(ServerLevel level, Mob mob, double x, double y, double z, DifficultyInstance difficulty, MobSpawnType reason) {
    RandomSource random = level.getRandom();
    HolderLookup<Affix> affixes = level.registryAccess().lookupOrThrow(ChampionsRegistries.AFFIX);
    HolderLookup<Rank> ranks = level.registryAccess().lookupOrThrow(ChampionsRegistries.RANK);


    if (difficulty.isHarderThan(ChampionsServerConfig.CHAMPION_SPAWN_DIFFICULTY_THRESHOLD.get().floatValue())) {
      selectRank(random, mob, ranks.listElements()).ifPresent(rank -> {
        List<AffixInstance> list = AffixHelper.selectAffixByLevel(
          random,
          mob.getType(),
          rank.value().tier(),
          affixes.listElements(),
          rank.value().createAffixInstances(mob, random, difficulty).toList()
        );
        if (!list.isEmpty()) {
          AffixHelper.updateEntity(mob,
            mutable -> list.forEach(instance -> mutable.upgrade(instance.affix(), instance.level()))
          );
          applyRank(mob, rank);
        }
      });
    }
  }

  public static @Nullable ChampionMobPreset getPreset(Entity entity) {
    //noinspection deprecation
    return entity.getType().builtInRegistryHolder().getData(ChampionsDataMaps.CHAMPION_MOB_PRESET);
  }

  public static void applyRank(Entity entity, Holder<Rank> rank) {
    ChampionPropertyHelper.updateEntity(entity, mutable ->
      mutable.setPrefix(rank.value().description())
        .setTier(rank.value().tier())
        .setColor(rank.value().color())
        .setBoss(rank.value().boss())
    );
    ChampionPropertyHelper.refreshBossbar(entity);
  }
}
