package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.LanguageProvider;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.util.ChampionsUtil;

public abstract class ChampionsLanguageProvider extends LanguageProvider {
  public ChampionsLanguageProvider(PackOutput output, String modid, String locale) {
    super(output, modid, locale);
  }

  public ChampionsLanguageProvider(PackOutput output, String locale) {
    super(output, ChampionsMod.MOD_ID, locale);
  }

  protected final void addAffix(ResourceKey<Affix> key, String name) {
    this.add(ChampionsUtil.makeDescriptionId("affix", key.location()), name);
  }

  protected final void addAffixLevel(int level, String name) {
    this.add("champions.affix.level." + level, name);
  }
}
