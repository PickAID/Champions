package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.LanguageProvider;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.util.ChampionsUtil;
import top.theillusivec4.champions.world.entity.affix.Affix;
import top.theillusivec4.champions.world.entity.champion.Rank;

public abstract class ChampionsLanguageProvider extends LanguageProvider {
  public ChampionsLanguageProvider(PackOutput output, String modid, String locale) {
    super(output, modid, locale);
  }

  public ChampionsLanguageProvider(PackOutput output, String locale) {
    super(output, ChampionsMod.MOD_ID, locale);
  }

  public static LanguageProvider zhCn(PackOutput output) {
    return new ZhCn(output);
  }

  public static LanguageProvider enUs(PackOutput output) {
    return new EnUs(output);
  }

  protected final void addAffix(ResourceKey<Affix> key, String name) {
    this.add(ChampionsUtil.makeDescriptionId("affix", key.identifier()), name);
  }

  protected final void addRank(ResourceKey<Rank> key, String name) {
    this.add(ChampionsUtil.makeDescriptionId("rank", key.identifier()), name);
  }

  protected final void addAffixLevel(int level, String name) {
    this.add("champions.affix.level." + level, name);
  }

  protected final void addChampionTier(int tier, String name) {
    this.add("champions.champion.tier." + tier, name);
  }

  protected final void addItemGroup(String name) {
    this.add("itemGroup.champions.champion_egg", name);
  }
}
