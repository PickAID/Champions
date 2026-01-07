package top.theillusivec4.champions.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.LanguageProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.Affixes;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.commands.SpawnEggCommand;

public final class LanguageProviders {

  public static LanguageProvider zhCn(PackOutput output) {
    return new ZhCn(output);
  }

  public static void addAffix(LanguageProvider provider, ResourceKey<Affix> key, String name) {
    provider.add(key.identifier().toLanguageKey("affix"), name);
  }

  public static String fullLevelKey(int level) {
    return LanguageKeys.LEVEL_PREFIX + "." + level;
  }

  public static void addRank(LanguageProvider provider, ResourceKey<Rank> key, String name) {
    provider.add(key.identifier().toLanguageKey("rank"), name);
  }

  public static void addLevel(LanguageProvider provider, int level, String name) {
    provider.add(fullLevelKey(level), name);
  }

  public static void addLevelTooltip(LanguageProvider provider, String name) {
    provider.add(LanguageKeys.LEVEL_TOOLTIP_KEY, name);
  }

  public static void addAffixesToolTip(LanguageProvider provider, String name) {
    provider.add(LanguageKeys.AFFIXES_TOOLTIP_KEY, name);
  }

  private LanguageProviders() {
  }

  public static class ZhCn extends LanguageProvider {
    private static final String LOCALE = "zh_cn";

    public ZhCn(PackOutput output) {
      super(output, Champions.MODID, LOCALE);
    }

    @Override
    protected void addTranslations() {
      add(SpawnEggCommand.SUCCESS_KEY, "已给予%s物品");
      add(SpawnEggCommand.FAILED_KEY, "物品%s不是一个刷怪蛋");
      add(LanguageKeys.RANK_TOOLTIP_KEY, "头衔：");
      add(LanguageKeys.LEVEL_TOOLTIP_KEY, "等级：");
      add(LanguageKeys.COLOR_TOOLTIP_KEY, "颜色：");
      add(LanguageKeys.PREFIX_NAME_TOOLTIP_KEY, "前缀：");
      add(LanguageKeys.AFFIXES_TOOLTIP_KEY, "已有词缀：");
      add(LanguageKeys.getLevelKey(1), "I");
      add(LanguageKeys.getLevelKey(2), "II");
      add(LanguageKeys.getLevelKey(3), "III");
      add(LanguageKeys.getLevelKey(4), "IV");
      add(LanguageKeys.getLevelKey(5), "V");
      addRank(Ranks.COMMON, "普通");
      addRank(Ranks.SKILLED, "稀有");
      addRank(Ranks.ELITE, "精英");
      addRank(Ranks.LEGENDARY, "传奇");
      addRank(Ranks.ULTIMATE, "终极");
      addAffix(Affixes.ADAPTABLE, "适应");
      addAffix(Affixes.ARCTIC, "严寒");
      addAffix(Affixes.DAMPENING, "抑制");
      addAffix(Affixes.DESECRATING, "亵渎");
      addAffix(Affixes.ENKINDLING, "点燃");
      addAffix(Affixes.HASTY, "急速");
      addAffix(Affixes.INFESTED, "感染");
      addAffix(Affixes.KNOCKING, "爆震");
      addAffix(Affixes.LIVELY, "活力");
      addAffix(Affixes.MAGNETIC, "磁力");
      addAffix(Affixes.MOLTEN, "熔融");
      addAffix(Affixes.PARALYZING, "瘫痪");
      addAffix(Affixes.PLAGUED, "瘟疫");
      addAffix(Affixes.REFLECTIVE, "反射");
      addAffix(Affixes.SHIELDING, "保护");
      addAffix(Affixes.WOUNDING, "创伤");
    }

    private void addAffixesTooltip(String name) {
      LanguageProviders.addAffixesToolTip(this, name);
    }

    private void addLevel(int level, String name) {
      LanguageProviders.addLevel(this, level, name);
    }

    private void addLevelTooltip(String name) {
      LanguageProviders.addLevelTooltip(this, name);
    }

    private void addRank(ResourceKey<Rank> key, String name) {
      LanguageProviders.addRank(this, key, name);
    }

    private void addAffix(ResourceKey<Affix> key, String name) {
      LanguageProviders.addAffix(this, key, name);
    }
  }


}
