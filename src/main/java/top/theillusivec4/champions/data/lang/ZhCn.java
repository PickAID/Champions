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
    this.add("config.jade.plugin_champions.entity_affixes", "实体词缀");
    this.add("config.jade.plugin_champions.entity_champion", "冠军属性");
    this.add("commands.champions.affix.add.success.single", "已将词缀%s添加至%s");
    this.add("commands.champions.affix.add.success.multiple", "已为%s个实体添加词缀%s");
    this.add("champions.configuration.display_health_overlay", "显示生命值");
    this.add("champions.configuration.difficulty_threshold", "难度阈值");
    this.add("champions.configuration.default_affixable", "默认词缀亲和力");
    this.add("champions.champion.color", "■");
    this.add("champions.champion.boss.true", "是");
    this.add("champions.champion.boss.false", "否");
    this.add("item.champions.champion.tier", "等级: %s");
    this.add("item.champions.champion.prefix", "前缀: %s");
    this.add("item.champions.champion.color", "颜色: %s");
    this.add("item.champions.champion.boss", "首领: %s");
    this.add("item.champions.stored_affixes", "已有词缀: ");
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
