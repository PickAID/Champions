package top.theillusivec4.champions.data.lang;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.LanguageProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;

public abstract class ChampionsLanguageProvider extends LanguageProvider {
  public ChampionsLanguageProvider(PackOutput output, String modid, String locale) {
    super(output, modid, locale);
  }

  protected ChampionsLanguageProvider(PackOutput output, String locale) {
    super(output, Champions.MODID, locale);
  }

  protected final void addRank(ResourceKey<Rank> key, String name) {
    LanguageHelper.addRank(this, key, name);
  }

  protected final void addDamageType(ResourceKey<DamageType> damageType, String msg, String byPlayer) {
    LanguageHelper.addDamageType(this, damageType, msg, byPlayer);
  }

  protected final void addAffix(ResourceKey<Affix> key, String name) {
    LanguageHelper.addAffix(this, key, name);
  }
}
