package top.theillusivec4.champions.server.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.command.arguments.EntityOptions;
import net.minecraft.command.arguments.EntitySelectorParser;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.util.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChampionSelectorOptions {

    public static void setup() {
	    EntityOptions.register("champions", ChampionSelectorOptions::championsArgument,
                entitySelectorParser -> true,
                Utils.translatable("argument.entity.options.champions.description"));
    }

    private static void championsArgument(EntitySelectorParser parser) throws CommandSyntaxException {
        StringReader reader = parser.getReader();
        boolean invert = parser.shouldInvertValue();
        CompoundNBT CompoundNBT = (new JsonToNBT(reader)).readStruct();
        Set<String> affixes = new HashSet<>();
        MinMaxBounds.IntBound matches = MinMaxBounds.IntBound.atLeast(1);
        MinMaxBounds.IntBound count = MinMaxBounds.IntBound.ANY;

        if (CompoundNBT.contains("affixes", Constants.NBT.TAG_LIST)) {
            ListNBT listTag = CompoundNBT.getList("affixes", Constants.NBT.TAG_STRING);

            for (int i = 0; i < listTag.size(); i++) {
                affixes.add(listTag.getString(i));
            }
        } else if (CompoundNBT.contains("affixes", Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT tag = CompoundNBT.getCompound("affixes");
	        ListNBT listTag = tag.getList("values", Constants.NBT.TAG_STRING);

            for (int i = 0; i < listTag.size(); i++) {
                affixes.add(listTag.getString(i));
            }
            count = fromTag(tag, "count", count);
            matches = fromTag(tag, "matches", matches);
        }
        MinMaxBounds.IntBound tier = fromTag(CompoundNBT, "tier", MinMaxBounds.IntBound.ANY);
        MinMaxBounds.IntBound finalCount = count;
        MinMaxBounds.IntBound finalMatches = matches;
        parser.addPredicate(entity -> {
            boolean flag = matches(entity, affixes, tier, finalCount, finalMatches);
            return invert != flag;
        });
    }

    private static MinMaxBounds.IntBound fromTag(CompoundNBT origin, String key,
                                             MinMaxBounds.IntBound defaultValue) {

        if (origin.contains(key, Constants.NBT.TAG_INT)) {
            int tier = origin.getInt(key);
            return MinMaxBounds.IntBound.exactly(tier);
        } else if (origin.contains(key, Constants.NBT.TAG_COMPOUND)) {
            CompoundNBT tag = origin.getCompound(key);
            Integer min = null;
            Integer max = null;

            if (tag.contains("min", Constants.NBT.TAG_INT)) {
                min = tag.getInt("min");
            }

            if (tag.contains("max", Constants.NBT.TAG_INT)) {
                max = tag.getInt("max");
            }

            if (min == null && max == null) {
                return MinMaxBounds.IntBound.ANY;
            } else if (min != null && max == null) {
                return MinMaxBounds.IntBound.atLeast(min);
            } else if (min == null) {
                return new MinMaxBounds.IntBound(0,10);
            } else {
                return new MinMaxBounds.IntBound(min, max);
            }
        }
        return defaultValue;
    }

    private static boolean matches(Entity entity, Set<String> affixes, MinMaxBounds.IntBound tier,
                                   MinMaxBounds.IntBound count, MinMaxBounds.IntBound matches) {
        return ChampionCapability.getCapability(entity).map(champion -> {
            IChampion.Server server = champion.getServer();
            int championTier = server.getRank().map(Rank::getTier).orElse(0);

            if (championTier <= 0 || !tier.matches(championTier)) {
                return false;
            }
            List<IAffix> championAffixes = server.getAffixes();

            if (affixes.isEmpty()) {
                return count.matches(championAffixes.size());
            } else {
                Set<String> ids =
                        championAffixes.stream().map(IAffix::toString).collect(Collectors.toSet());
                int found = 0;

                for (String affix : affixes) {

                    if (ids.contains(affix)) {
                        found++;
                    }
                }
                return matches.matches(found) && count.matches(championAffixes.size());
            }
        }).orElse(false);
    }
}
