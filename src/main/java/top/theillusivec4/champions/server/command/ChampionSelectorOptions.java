package top.theillusivec4.champions.server.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capabilities.ChampionAttachment;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.rank.RankManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Deprecated
public class ChampionSelectorOptions {

  public static void setup() {
    EntitySelectorOptions.register("champions", ChampionSelectorOptions::championsArgument,
      entitySelectorParser -> true,
      Component.translatable("argument.entity.options.champions.description"));
  }

  private static void championsArgument(EntitySelectorParser parser) throws CommandSyntaxException {
    StringReader reader = parser.getReader();
    boolean invert = parser.shouldInvertValue();
    CompoundTag compoundtag = TagParser.parseCompoundAsArgument(reader);
    Set<String> affixes = new HashSet<>();
    MinMaxBounds.Ints matches = MinMaxBounds.Ints.atLeast(1);
    MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;

    if (compoundtag.contains("affixes")) {
      ListTag listTag = compoundtag.getListOrEmpty("affixes");

      for (int i = 0; i < listTag.size(); i++) {
        affixes.add(listTag.getString(i).orElse(""));
      }
    } else if (compoundtag.contains("affixes")) {
      CompoundTag tag = compoundtag.getCompoundOrEmpty("affixes");
      ListTag listTag = tag.getListOrEmpty("values");

      for (int i = 0; i < listTag.size(); i++) {
        affixes.add(listTag.getString(i).orElse(""));
      }
      count = fromTag(tag, "count", count);
      matches = fromTag(tag, "matches", matches);
    }
    MinMaxBounds.Ints tier = fromTag(compoundtag, "tier", MinMaxBounds.Ints.ANY);
    MinMaxBounds.Ints finalCount = count;
    MinMaxBounds.Ints finalMatches = matches;
    parser.addPredicate(entity -> {
      boolean flag = matches(entity, affixes, tier, finalCount, finalMatches);
      return invert != flag;
    });
  }

  private static MinMaxBounds.Ints fromTag(CompoundTag origin, String key,
                                           MinMaxBounds.Ints defaultValue) {

    if (origin.contains(key)) {
      int tier = origin.getInt(key).orElse(RankManager.getEmptyRank().getTier());
      return MinMaxBounds.Ints.exactly(tier);
    } else if (origin.contains(key)) {
      CompoundTag tag = origin.getCompoundOrEmpty(key);
      Integer min = null;
      Integer max = null;

      if (tag.contains("min")) {
        min = tag.getInt("min").orElse(null);
      }

      if (tag.contains("max")) {
        max = tag.getInt("max").orElse(null);
      }

      if (min == null && max == null) {
        return MinMaxBounds.Ints.ANY;
      } else if (min != null && max == null) {
        return MinMaxBounds.Ints.atLeast(min);
      } else if (min == null) {
        return MinMaxBounds.Ints.atMost(max);
      } else {
        return MinMaxBounds.Ints.between(min, max);
      }
    }
    return defaultValue;
  }

  private static boolean matches(Entity entity, Set<String> affixes, MinMaxBounds.Ints tier,
                                 MinMaxBounds.Ints count, MinMaxBounds.Ints matches) {
    return ChampionAttachment.getAttachment(entity).map(champion -> {
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
          championAffixes.stream().map(IAffix::getIdentifier).map(Identifier::toString).collect(Collectors.toSet());
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
