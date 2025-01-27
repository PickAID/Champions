package top.theillusivec4.champions.common.rank;

import com.google.common.collect.ImmutableSortedMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.config.RanksConfig.RankConfig;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class RankManager {

    private static final TreeMap<Integer, Rank> RANKS = new TreeMap<>();

    private static final Rank EMPTY_RANK = new Rank();

    public static ImmutableSortedMap<Integer, Rank> getRanks() {
        return ImmutableSortedMap.copyOf(RANKS);
    }

    @Nonnull
    public static Rank getRank(int tier) {
        Rank rank = RANKS.get(tier);

        if (rank != null) {
            return rank;
        } else if (RANKS.isEmpty()) {
            return getEmptyRank();
        } else {
            return RANKS.firstEntry().getValue();
        }
    }

    public static Rank getLowestRank() {
        return RANKS.isEmpty() ? getEmptyRank() : RANKS.firstEntry().getValue();
    }

    public static Rank getEmptyRank() {
        return EMPTY_RANK;
    }

    public static Rank getHighestRank() {
        return RANKS.isEmpty() ? EMPTY_RANK : RANKS.lastEntry().getValue();
    }

    public static void buildRanks() {
        List<RankConfig> ranks = ChampionsConfig.ranks;

        if (ranks == null) {
            return;
        }
        ranks.forEach(rank -> {
            try {
                Rank newRank = getRankFromConfig(rank);
                RANKS.put(newRank.getTier(), newRank);
            } catch (NullPointerException e) {
                Champions.LOGGER.error("Null rank found with tier {} while building rank, skipping...", rank.tier);
            } catch (IllegalArgumentException e) {
                Champions.LOGGER.error("Invalid attribute found while building rank, skipping...");
            }
        });
    }

    private static Rank getRankFromConfig(RankConfig rank) throws IllegalArgumentException {
        if (rank.tier == null || rank.numAffixes == null || rank.weight == null
                || rank.defaultColor == null || rank.growthFactor == null || rank.effects == null
                || rank.presetAffixes == null) {
            throw new IllegalArgumentException("Missing rank attribute");
        }
        int tier;

        if (rank.tier < 0) {
            throw new IllegalArgumentException("Negative tier");
        } else {
            tier = rank.tier;
        }
        int numAffixes;

        if (rank.numAffixes < 0) {
            throw new IllegalArgumentException("Negative number of affixes");
        } else {
            numAffixes = rank.numAffixes;
        }
        int weight;

        if (rank.weight < 0) {
            throw new IllegalArgumentException("Non-positive chance");
        } else {
            weight = rank.weight;
        }
        var defaultColor = rank.defaultColor;

        int growthFactor;

        if (rank.growthFactor < 0) {
            throw new IllegalArgumentException("Negative growth factor");
        } else {
            growthFactor = rank.growthFactor;
        }
        List<Tuple<Holder<MobEffect>, Integer>> effects = new ArrayList<>();

        rank.effects.forEach(effect -> {
            String[] parsed = effect.split(";");
            Optional<Holder<MobEffect>> found = ForgeRegistries.MOB_EFFECTS.getHolder(new ResourceLocation(parsed[0]));

            if (found.isPresent()) {
                int amplifier = 0;

                if (parsed.length > 1) {
                    try {
                        amplifier = Integer.parseInt(parsed[1]);
                    } catch (NumberFormatException e) {
                        Champions.LOGGER
                                .error("Found invalid amplifier value for effect, setting to default 1");
                    }
                }
                effects.add(new Tuple<>(found.get(), amplifier));
            }
        });

        List<IAffix> presetAffixes = new ArrayList<>();
        rank.presetAffixes
                .forEach(affix -> Champions.API.getAffix(affix).ifPresent(presetAffixes::add));
        return new Rank(tier, numAffixes, growthFactor, weight, defaultColor, effects,
                presetAffixes);
    }
}
