package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import top.theillusivec4.champions.affix.Affixes;

public class EnUs extends ChampionsLanguageProvider {
  public EnUs(PackOutput output) {
    super(output, "en_us");
  }

  @Override
  protected void addTranslations() {
    this.add("config.jade.plugin_champions.entity_affixes", "Entity Affixes");
    this.add("commands.champions.affix.add.success.single", "The affix %s has been added to %s.");
    this.add("commands.champions.affix.add.success.multiple", "Affix %s has been added to %s entities.");
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
  }
}
