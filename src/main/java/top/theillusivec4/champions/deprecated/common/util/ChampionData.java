package top.theillusivec4.champions.deprecated.common.util;

import com.google.common.collect.ImmutableSortedMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.TagTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.api.affix.IAffix;
import top.theillusivec4.champions.deprecated.api.data.AffixCategory;
import top.theillusivec4.champions.deprecated.common.rank.Rank;
import top.theillusivec4.champions.deprecated.common.rank.RankManager;
import top.theillusivec4.champions.util.Utils;

import java.util.*;

@Deprecated
public class ChampionData {

  private static final String CHAMPION_KEY = "ChampionsData";
  private static final RandomSource RAND = RandomSource.create();

  /**
   * <p>Read tag data from this champion Persistent Data,<br>
   * if this champion rank < 0, will construct new affixes to champion. </p>
   *
   * @param champion to construct data.
   * @return True, if this champion has proper data, else false.
   */
  public static boolean read(IChampion champion) {
    LivingEntity livingEntity = champion.getLivingEntity();
    CompoundTag tag = livingEntity.getPersistentData();

    if (!tag.isEmpty()) {
      CompoundTag championTag = tag.getCompoundOrEmpty(CHAMPION_KEY);

      if (!championTag.isEmpty()) {
        Rank rank = RankManager.getLowestRank();

        if (championTag.contains("tier")) {

          if (championTag.contains("tier") && championTag.getType() == TagTypes.getType(3)) {
            rank = RankManager.getRank(championTag.getInt("tier").orElse(RankManager.getEmptyRank().getTier()));
          } else if (championTag.contains("tier") && championTag.getType() == TagTypes.getType(10)) {
            CompoundTag valueTag = championTag.getCompoundOrEmpty("tier");
            Integer min = valueTag.getInt("min").orElse(null);
            Integer max = valueTag.getInt("max").orElse(null);
            rank = createRank(livingEntity, min, max);
          }
        }
        if (rank.getTier() < 1) {
          Champions.LOGGER.error("Rank cannot be empty");
          return false;
        }
        champion.getServer().setRank(rank);
        List<IAffix> affixes = new ArrayList<>();
        Set<String> ids = new HashSet<>();
        Integer count = null;

        if (championTag.contains("affixes")) {

          if (championTag.contains("affixes") && championTag.getType() == TagTypes.getType(9)) {
            ListTag listTag = championTag.getListOrEmpty("affixes");

            for (int i = 0; i < listTag.size(); i++) {
              ids.add(listTag.getStringOr(i, ""));
            }
          } else if (championTag.contains("affixes") && championTag.getType() == TagTypes.getType(10)) {
            CompoundTag affixesTag = championTag.getCompoundOrEmpty("affixes");
            count = affixesTag.getInt("count").orElse(null);
            var listTag = affixesTag.getList("values");

            if (listTag.isPresent()) {
              for (int i = 0; i < listTag.get().size(); i++) {
                ids.add(listTag.get().getStringOr(i, ""));
              }
            }
          }
        }

        for (String id : ids) {
          Champions.API.getAffix(id).ifPresent(affixes::add);
        }
        int totalAffixes = count == null ? rank.getNumAffixes() : count;
        int toAdd = totalAffixes - affixes.size();

        if (toAdd > 0) {
          createAffixes(affixes, champion, totalAffixes);
        }
        ChampionBuilder.applyGrowth(champion, rank.getGrowthFactor());
        champion.getServer().setAffixes(affixes);
        Utils.consumeIfLifeCycle(affixes,
          lifecycle -> lifecycle.onInitialSpawn(champion));
        return true;
      }
    }
    return false;
  }

  private static void createAffixes(final List<IAffix> affixes, final IChampion champion,
                                    int total) {
    Map<AffixCategory, List<IAffix>> allAffixes = Champions.API.getCategoryMap();
    Map<AffixCategory, List<IAffix>> validAffixes = new HashMap<>();
    Optional<EntityManager.EntitySettings> entitySettings = EntityManager
      .getSettings(champion.getLivingEntity().getType());

    for (AffixCategory category : Champions.API.getCategories()) {
      validAffixes.put(category, new ArrayList<>());
    }
    allAffixes.forEach((k, v) -> validAffixes.get(k).addAll(v.stream().filter(affix -> !affixes.contains(affix)
      && entitySettings.map(entitySettings1 -> entitySettings1.canApply(affix)).orElse(true)
      && affix.canApply(champion)).toList()));
    ChampionBuilder.addAffixToList(total, affixes, validAffixes, RAND);
  }

  private static Rank createRank(final LivingEntity livingEntity, Integer min, Integer max) {

    if (ChampionHelper.notPotential(livingEntity)) {
      return RankManager.getEmptyRank();
    }
    ImmutableSortedMap<Integer, Rank> ranks = RankManager.getRanks();

    if (ranks.isEmpty()) {
      Champions.LOGGER.error(
        "No rank configuration found! Please check the 'champions-ranks.toml' file in the 'serverconfigs'.");
      return RankManager.getEmptyRank();
    }
    // 特殊判断 min 和 Max 相等的情况
    if (Objects.equals(min, max)) {
      Rank rank = ranks.get(min);
      if (rank != null) {
        return rank;
      } else {
        Champions.LOGGER.error("Tier {} not found in rank config!", min);
        return RankManager.getEmptyRank();
      }
    }

    Integer[] tierRange = new Integer[]{min, max};
    Integer firstTier = tierRange[0] != null ? tierRange[0] : ranks.firstKey();
    int maxTier = tierRange[1] != null ? tierRange[1] : -1;
    Iterator<Integer> iter = ranks.navigableKeySet().tailSet(firstTier, false).iterator();
    Rank result = ranks.get(firstTier);

    if (result == null) {
      Champions.LOGGER.error("Tier {} cannot be found in {}! Assigning lowest available rank to {}",
        firstTier, ranks, livingEntity);
      return RankManager.getEmptyRank();
    }

    while (iter.hasNext() && (result.getTier() < maxTier || maxTier == -1)) {
      Rank rank = ranks.get(iter.next());

      if (rank == null) {
        return result;
      }
      float chance = rank.getWeight();

      if (RAND.nextFloat() < chance) {
        result = rank;
      } else {
        return result;
      }
    }
    return result;
  }
}
