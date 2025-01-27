package top.theillusivec4.champions.common.util;

import com.google.common.collect.ImmutableSortedMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixCategory;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.rank.RankManager;

import java.util.*;

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
            CompoundTag championTag = tag.getCompound(CHAMPION_KEY);

            if (!championTag.isEmpty()) {
                Rank rank = RankManager.getLowestRank();

                if (championTag.contains("tier")) {

                    if (championTag.contains("tier", Tag.TAG_INT)) {
                        rank = RankManager.getRank(championTag.getInt("tier"));
                    } else if (championTag.contains("tier", Tag.TAG_COMPOUND)) {
                        CompoundTag valueTag = championTag.getCompound("tier");
                        Integer min = valueTag.contains("min") ? valueTag.getInt("min") : null;
                        Integer max = valueTag.contains("max") ? valueTag.getInt("max") : null;
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

                    if (championTag.contains("affixes", Tag.TAG_LIST)) {
                        ListTag listTag = championTag.getList("affixes", Tag.TAG_STRING);

                        for (int i = 0; i < listTag.size(); i++) {
                            ids.add(listTag.getString(i));
                        }
                    } else if (championTag.contains("affixes", Tag.TAG_COMPOUND)) {
                        CompoundTag affixesTag = championTag.getCompound("affixes");
                        count = affixesTag.contains("count", Tag.TAG_INT) ? affixesTag.getInt("count") : null;
                        ListTag listTag = affixesTag.getList("values", Tag.TAG_STRING);

                        for (int i = 0; i < listTag.size(); i++) {
                            ids.add(listTag.getString(i));
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
                affixes.forEach(affix -> affix.onInitialSpawn(champion));
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
