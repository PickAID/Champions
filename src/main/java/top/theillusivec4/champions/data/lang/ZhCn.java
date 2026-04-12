package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import top.theillusivec4.champions.affix.Affixes;
import top.theillusivec4.champions.champion.Ranks;

public class ZhCn extends ChampionsLanguageProvider {
  public ZhCn(PackOutput output) {
    super(output, "zh_cn");
  }

  @Override
  protected void addTranslations() {
    add("config.jade.plugin_champions.entity_affixes", "实体词缀");
    add("config.jade.plugin_champions.entity_champion_property", "冠军属性");
    add("commands.champions.affix.add.success.single", "已将词缀%s添加至%s");
    add("commands.champions.affix.add.success.multiple", "已为%s个实体添加词缀%s");
    add("commands.champions.affix.remove.success.single", "已将词缀%s从%s移除");
    add("commands.champions.affix.remove.success.multiple", "已将%s个实体的词缀%s移除");
    add("champions.configuration.display_health_overlay_on_target", "准星指向时显示生命条");
    add("champions.configuration.champion_spawn_difficulty_threshold", "冠军生物生成难度阈值");
    add("champions.configuration.default_affixable", "默认词缀亲和力");
    add("champions.configuration.affixable_factor", "词缀亲和力因子");
    add("champions.configuration.random_variration_factor", "随机变化因子");
    add("champions.configuration.min_affix_cost", "最小词缀代价");
    add("champions.configuration.max_affix_cost", "最大词缀代价");
    add("champions.champion.color", "■");
    add("champions.champion.boss.true", "是");
    add("champions.champion.boss.false", "否");
    add("item.champions.champion.tier", "等级: %s");
    add("item.champions.champion.prefix", "前缀: %s");
    add("item.champions.champion.color", "颜色: %s");
    add("item.champions.champion.boss", "首领: %s");
    add("item.champions.stored_affixes", "已有词缀: ");
    addItemGroup("冠军强敌：刷怪蛋");
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
    addAffixLevel(1, "I");
    addAffixLevel(2, "II");
    addAffixLevel(3, "III");
    addAffixLevel(4, "IV");
    addAffixLevel(5, "V");
    addAffixLevel(6, "VI");
    addAffixLevel(7, "VII");
    addAffixLevel(8, "VIII");
    addAffixLevel(9, "IX");
    addAffixLevel(10, "X");
    addChampionTier(1, "⭐");
    addChampionTier(2, "⭐⭐");
    addChampionTier(3, "⭐⭐⭐");
    addChampionTier(4, "⭐⭐⭐⭐");
    addChampionTier(5, "⭐⭐⭐⭐⭐");
  }
}
