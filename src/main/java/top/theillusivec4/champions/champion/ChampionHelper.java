package top.theillusivec4.champions.champion;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;

import java.util.*;
import java.util.stream.Stream;

public final class ChampionHelper {
  public static void filterCompatibleEnchantments(List<Holder<Affix>> affixes, Holder<Affix> target) {
    affixes.removeIf(e -> !Affix.areCompatible(e, target));
  }

  public static List<Holder<Affix>> getAvailableEnchantmentResults(int championLevel, Entity entity, Stream<Holder<Affix>> source) {
    List<Holder<Affix>> results = Lists.newArrayList();
    source.filter(affix -> affix.value().isSupportedEntityType(entity.getType())).forEach(results::add);
    return results;
  }

  public static int calculateChampionLevel(RandomSource random, DifficultyInstance difficultyInstance) {
    if (random.nextFloat() <= difficultyInstance.getSpecialMultiplier()) {
      int originLevel = Math.round(difficultyInstance.getEffectiveDifficulty());
      random.nextInt(Math.max(originLevel / 2, 1), 8);
      return Math.min(random.nextInt(Math.max(originLevel / 2, 1), 8), 8);
    }

    return 0;
  }

  public static List<Holder<Affix>> selectAffixes(Entity entity, int championLevel, Stream<Holder<Affix>> source) {
    List<Holder<Affix>> results = Lists.newArrayList();
    List<Holder<Affix>> list = getAvailableEnchantmentResults(championLevel, entity, source);
    Collections.shuffle(list);

    int i = 0;
    for (Holder<Affix> affix : list) {
      if (i >= championLevel) {
        break;
      } else if (isAffixCompatible(results, affix)) {
        results.add(affix);
        i++;
      }
    }

    return results;
  }

  public static TextColor selectColor(int championLevel) {
    return switch (championLevel) {
      case 1 -> Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.WHITE));
      case 2 -> Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.YELLOW));
      case 3 -> Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.AQUA));
      case 4 -> Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE));
      case 5 -> Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.GOLD));
      default -> Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.RED));
    };
  }

  public static Optional<Holder<Rank>> selectRank(Entity entity, int championLevel, Stream<Holder<Rank>> source) {
    return WeightedRandom.getRandomItem(
      entity.getRandom(),
      source.filter(rank1 -> rank1.value().matches(championLevel)).toList(),
      rank2 -> rank2.value().weight()
    );
  }

  public static boolean isAffixCompatible(Collection<Holder<Affix>> affixes, Holder<Affix> target) {
    for (Holder<Affix> affix : affixes) {
      if (!Affix.areCompatible(affix, target)) {
        return false;
      }
    }

    return true;
  }

  private ChampionHelper() {
  }
}
