package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import top.theillusivec4.champions.affix.Affixes;
import top.theillusivec4.champions.champion.Ranks;

public class EnUs extends ChampionsLanguageProvider {
  public EnUs(PackOutput output) {
    super(output, "en_us");
  }

  @Override
  protected void addTranslations() {
    this.add("config.jade.plugin_champions.entity_affixes", "Entity Affixes");
    this.add("config.jade.plugin_champions.entity_champion", "Champion property");
    this.add("commands.champions.affix.add.success.single", "The affix %s has been added to %s.");
    this.add("commands.champions.affix.add.success.multiple", "Affix %s has been added to %s entities.");
    this.add("champions.configuration.display_health_overlay", "Display Health Overlay");
    this.add("champions.configuration.difficulty_threshold", "Difficulty Threshold");
    this.add("champions.configuration.default_affixable", "Default Affixable Value");
    this.add("champions.champion.color", "■");
    this.add("champions.champion.boss.true", "True");
    this.add("champions.champion.boss.false", "False");
    this.add("item.champions.champion.tier", "Tier: %s");
    this.add("item.champions.champion.prefix", "Prefix: %s");
    this.add("item.champions.champion.color", "Color: %s");
    this.add("item.champions.champion.boss", "Boss: %s");
    this.add("item.champions.stored_affixes", "Existing Affixes: ");
    addRank(Ranks.COMMON, "Common");
    addRank(Ranks.SKILLED, "Skilled");
    addRank(Ranks.ELITE, "Elite");
    addRank(Ranks.LEGENDARY, "Legendary");
    addRank(Ranks.ULTIMATE, "Ultimate");
    addAffix(Affixes.ADAPTABLE, "Adaptable");
    addAffix(Affixes.ARCTIC, "Arctic");
    addAffix(Affixes.DAMPENING, "Dampening");
    addAffix(Affixes.DESECRATING, "Desecrating");
    addAffix(Affixes.ENKINDLING, "Enkingling");
    addAffix(Affixes.HASTY, "Hasty");
    addAffix(Affixes.INFESTED, "Infested");
    addAffix(Affixes.KNOCKING, "Knocking");
    addAffix(Affixes.LIVELY, "Lively");
    addAffix(Affixes.MAGNETIC, "Magnetic");
    addAffix(Affixes.MOLTEN, "Molten");
    addAffix(Affixes.PARALYZING, "Paralyzing");
    addAffix(Affixes.PLAGUED, "Plagued");
    addAffix(Affixes.REFLECTIVE, "Reflective");
    addAffix(Affixes.SHIELDING, "Shielding");
    addAffix(Affixes.WOUNDING, "Wounding");
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
