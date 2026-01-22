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

  public static List<Holder<Affix>> getAvailableEnchantmentResults(Entity entity, Stream<Holder<Affix>> source) {
    List<Holder<Affix>> results = Lists.newArrayList();
    source.filter(affix -> affix.value().isSupportedEntityType(entity.getType())).forEach(results::add);
    return results;
  }

  /**
   * 计算等级, 区域难度四舍五入：[1, 7], 减半得到基值：[1, 3], 生成不小于该值不超过8的随机数
   * 最小返回1，最大返回5
   * @param random 随机
   * @param difficultyInstance 难度
   * @return 等级
   */
  public static int calculateChampionLevel(RandomSource random, DifficultyInstance difficultyInstance) {
    int originLevel = Math.round(difficultyInstance.getEffectiveDifficulty());
    return Math.min(random.nextInt(Math.max(originLevel / 2, 1), 8), 8);
  }

  public static List<Holder<Affix>> selectAffixes(Entity entity, int championLevel, Stream<Holder<Affix>> source) {
    List<Holder<Affix>> results = Lists.newArrayList();
    List<Holder<Affix>> list = getAvailableEnchantmentResults(entity, source);
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

  public static int selectColor(int championLevel) {
    return switch (championLevel) {
      case 1 -> TextColor.parseColor("#FFC0CB").getOrThrow().getValue();
      case 2 -> TextColor.parseColor("#FFFF00").getOrThrow().getValue();
      case 3 -> TextColor.parseColor("#FF9900").getOrThrow().getValue();
      case 4 -> TextColor.parseColor("#66FFFF").getOrThrow().getValue();
      case 5 -> TextColor.parseColor("#CC33FF").getOrThrow().getValue();
      default -> Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.WHITE)).getValue();
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
