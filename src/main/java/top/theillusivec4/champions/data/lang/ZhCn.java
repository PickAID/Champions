package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import top.theillusivec4.champions.champion.affix.Affixes;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.world.damagesource.DamageTypes;

public class ZhCn extends ChampionsLanguageProvider {
  public ZhCn(PackOutput output) {
    super(output, "zh_cn");
  }

  @Override
  protected void addTranslations() {
    addDamageType(DamageTypes.REFLECTION_DAMAGE, "%1$s遭报应了!", "%1$s在与%2$s的战斗中遭到了报应!");
    addDamageType(DamageTypes.ENKINDLING_BULLET, "%1$s被火焰击中", "%1$s在与%2$s的战斗中被火焰击中");

    add(LanguageKeys.ITEM_CHAMPION_SPAWN_EGG, "强敌蛋");
    add(LanguageKeys.TOOLTIP_LEVEL, "等级：");
    add(LanguageKeys.TOOLTIP_COLOR, "颜色：");
    add(LanguageKeys.TOOLTIP_PREFIX_NAME, "前缀：");
    add(LanguageKeys.TOOLTIP_AFFIXES, "已有词缀：");
    add(LanguageKeys.TOOLTIP_BOSS, "强敌：");
    add(LanguageKeys.TOOLTIP_IS_BOSS, "是");
    add(LanguageKeys.TOOLTIP_NOT_BOSS, "否");

    add(LanguageKeys.COMMANDS_AFFIX_SUCCESS, "成功设置词缀：%s");
    add(LanguageKeys.COMMANDS_RANK_SUCCESS, "成功设置头衔：%s");
    add(LanguageKeys.COMMANDS_LEVEL_SUCCESS, "成功设置等级：%s");
    add(LanguageKeys.COMMANDS_BOSS_SUCCESS, "成功设置首领：%s");
    add(LanguageKeys.COMMANDS_COLOR_SUCCESS, "成功设置颜色：%s");

    add(LanguageKeys.ITEM_GROUP_CHAMPION_SPAWN_EGGS, "冠军：强敌 | 刷怪蛋");
    add(LanguageKeys.ITEM_GROUP_CUSTOM_CHAMPION_SPAWN_EGGS, "冠军：强敌 | 自定义刷怪蛋");

    add(LanguageKeys.CONFIG_DISPLAY_CHAMPION_OVERLAY, "显示生命值覆盖层");

    add(LanguageKeys.getLevelKey(1), "I");
    add(LanguageKeys.getLevelKey(2), "II");
    add(LanguageKeys.getLevelKey(3), "III");
    add(LanguageKeys.getLevelKey(4), "IV");
    add(LanguageKeys.getLevelKey(5), "V");

	  add(LanguageKeys.getPrefixKey(1), "普通");
	  add(LanguageKeys.getPrefixKey(2), "稀有");
	  add(LanguageKeys.getPrefixKey(3), "精英");
	  add(LanguageKeys.getPrefixKey(4), "传奇");
	  add(LanguageKeys.getPrefixKey(5), "终极");

    add(LanguageKeys.STAT_CHAMPION_MOBS_KILLED, "冠军生物击杀数");

    addRank(Ranks.COMMON, "普通");
    addRank(Ranks.SKILLED, "稀有");
    addRank(Ranks.ELITE, "精英");
    addRank(Ranks.LEGENDARY, "传奇");
    addRank(Ranks.ULTIMATE, "终极");
    addAffix(Affixes.ADAPTABLE, "适应");
    addAffix(Affixes.ARCTIC, "极寒");
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
