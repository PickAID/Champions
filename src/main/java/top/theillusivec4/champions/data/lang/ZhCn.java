package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import top.theillusivec4.champions.champion.affix.Affixes;
import top.theillusivec4.champions.champion.rank.Ranks;

public class ZhCn extends ChampionsLanguageProvider {
  public ZhCn(PackOutput output) {
    super(output, "zh_cn");
  }

  @Override
  protected void addTranslations() {
    add(LanguageKeys.ITEM_CHAMPION_SPAWN_EGG_KEY, "%s强敌蛋");
    add(LanguageKeys.TOOLTIP_RANK_KEY, "头衔：");
    add(LanguageKeys.TOOLTIP_LEVEL_KEY, "等级：");
    add(LanguageKeys.TOOLTIP_COLOR_KEY, "颜色：");
    add(LanguageKeys.TOOLTIP_PREFIX_NAME_KEY, "前缀：");
    add(LanguageKeys.TOOLTIP_AFFIXES_KEY, "已有词缀：");
    add(LanguageKeys.TOOLTIP_BOSS_KEY, "强敌：");
    add(LanguageKeys.TOOLTIP_IS_BOSS_KEY, "是");
    add(LanguageKeys.TOOLTIP_NOT_BOSS_KEY, "否");

    add(LanguageKeys.COMMANDS_AFFIX_SUCCESS_KEY, "成功设置词缀%s");
    add(LanguageKeys.COMMANDS_RANK_SUCCESS_KEY, "成功设置头衔%s");
    add(LanguageKeys.COMMANDS_LEVEL_SUCCESS_KEY, "成功设置等级%s");
    add(LanguageKeys.COMMANDS_BOSS_SUCCESS_KEY, "成功设置首领%s");
    add(LanguageKeys.COMMANDS_COLOR_SUCCESS_KEY, "成功设置颜色%s");
    add(LanguageKeys.COMMANDS_CONFIG_SELECTOR_SUCCESS_KEY, "成功应用配置%s");
    add(LanguageKeys.COMMANDS_ERROR_INVALID_CONFIG_SELECTOR_KEY, "无效的配置选择器%s");
    add(LanguageKeys.COMMANDS_ERROR_INVALID_CHAMPION_ENTITY_KEY, "无效的冠军实体%s");

    add(LanguageKeys.ITEM_GROUP_CHAMPION_SPAWN_EGGS, "冠军：强敌 | 刷怪蛋");
    add(LanguageKeys.ITEM_GROUP_CUSTOM_CHAMPION_SPAWN_EGGS, "冠军：强敌 | 自定义刷怪蛋");

    add(LanguageKeys.CONFIG_DISPLAY_CHAMPION_OVERLAY_KEY, "显示生命值覆盖层");

    add(LanguageUtil.getLevelKey(0), "无");
    add(LanguageUtil.getLevelKey(1), "⭐");
    add(LanguageUtil.getLevelKey(2), "⭐⭐");
    add(LanguageUtil.getLevelKey(3), "⭐⭐⭐");
    add(LanguageUtil.getLevelKey(4), "⭐⭐⭐⭐");
    add(LanguageUtil.getLevelKey(5), "⭐⭐⭐⭐⭐");

    add(LanguageKeys.STAT_CHAMPION_MOBS_KILLED_KEY, "冠军生物击杀数");

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
