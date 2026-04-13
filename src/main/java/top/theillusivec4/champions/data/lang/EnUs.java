package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import top.theillusivec4.champions.world.entity.affix.Affixes;
import top.theillusivec4.champions.world.entity.champion.Ranks;

public class EnUs extends ChampionsLanguageProvider {
  public EnUs(PackOutput output) {
    super(output, "en_us");
  }

  @Override
  protected void addTranslations() {
    add("config.jade.plugin_champions.entity_affixes", "Entity Affixes");
    add("config.jade.plugin_champions.entity_champion_property", "Champion property");
    add("commands.champions.affix.add.success.single", "The affix %s has been added to %s.");
    add("commands.champions.affix.add.success.multiple", "Affix %s has been added to %s entities.");
    add("commands.champions.affix.remove.success.single", "The affix &s has been removed from %s.");
    add("commands.champions.affix.remove.success.multiple", "Affix %s of %s entities has been removed.");
    add("champions.configuration.display_health_overlay_on_target", "Display health bar on target");
    add("champions.configuration.champion_spawn_difficulty_threshold", "Champion Spawn Difficulty Threshold");
    add("champions.configuration.default_affixable", "Default Affixable Value");
    add("champions.configuration.affixable_factor", "Affixable Factor");
    add("champions.configuration.random_factor", "Random Factor");
    add("champions.configuration.min_affix_cost", "Minimum Affix Cost");
    add("champions.configuration.max_affix_cost", "Maximum Affix Cost");
    add("champions.champion.color", "■");
    add("champions.champion.boss.true", "True");
    add("champions.champion.boss.false", "False");
    add("item.champions.champion.tier", "Tier: %s");
    add("item.champions.champion.prefix", "Prefix: %s");
    add("item.champions.champion.color", "Color: %s");
    add("item.champions.champion.boss", "Boss: %s");
    add("item.champions.stored_affixes", "Existing Affixes: ");
    addItemGroup("Champions: Spawn Egg");
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
