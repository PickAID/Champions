package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.LanguageProvider;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;

public final class LanguageHelper {

  public static LanguageProvider zhCn(PackOutput output) {
    return new ZhCn(output);
  }

  public static void addAffix(LanguageProvider provider, ResourceKey<Affix> key, String name) {
    provider.add(key.identifier().toLanguageKey("affix"), name);
  }

  public static void addRank(LanguageProvider provider, ResourceKey<Rank> key, String name) {
    provider.add(key.identifier().toLanguageKey("rank"), name);
  }

  public static void addLevelTooltip(LanguageProvider provider, String name) {
    provider.add(LanguageKeys.TOOLTIP_LEVEL_KEY, name);
  }

  public static void addAffixesToolTip(LanguageProvider provider, String name) {
    provider.add(LanguageKeys.TOOLTIP_AFFIXES_KEY, name);
  }

  private LanguageHelper() {
  }


}
