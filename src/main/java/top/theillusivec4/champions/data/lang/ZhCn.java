package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import top.theillusivec4.champions.champion.affix.Affixes;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.command.SpawnEggCommand;

public class ZhCn extends ChampionsLanguageProvider {
  public ZhCn(PackOutput output) {
    super(output, "zh_cn");
  }

  @Override
  protected void addTranslations() {
    add(LanguageKeys.PREFIX_NAME_ITEM_CHAMPION_SPAWN_EGG, "强敌 ");
    add(SpawnEggCommand.SUCCESS_KEY, "已给予%s物品");
    add(SpawnEggCommand.FAILED_KEY, "物品%s不是一个刷怪蛋");
    add(LanguageKeys.TOOLTIP_RANK_KEY, "头衔：");
    add(LanguageKeys.TOOLTIP_LEVEL_KEY, "等级：");
    add(LanguageKeys.TOOLTIP_COLOR_KEY, "颜色：");
    add(LanguageKeys.TOOLTIP_PREFIX_NAME_KEY, "前缀：");
    add(LanguageKeys.TOOLTIP_AFFIXES_KEY, "已有词缀：");
    add(LanguageKeys.TOOLTIP_BOSS_KEY, "强敌：");
    add(LanguageKeys.TOOLTIP_IS_BOSS_KEY, "是");
    add(LanguageKeys.TOOLTIP_NOT_BOSS_KEY, "否");
    add(LanguageKeys.ITEM_GROUP_CHAMPION_SPAWN_EGGS, "冠军：强敌 | 刷怪蛋");
    add(LanguageKeys.tooltipLevelKey(1), "I");
    add(LanguageKeys.tooltipLevelKey(2), "II");
    add(LanguageKeys.tooltipLevelKey(3), "III");
    add(LanguageKeys.tooltipLevelKey(4), "IV");
    add(LanguageKeys.tooltipLevelKey(5), "V");
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
}
