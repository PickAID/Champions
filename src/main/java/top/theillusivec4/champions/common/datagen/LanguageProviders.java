package top.theillusivec4.champions.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.LanguageProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.Affixes;
import top.theillusivec4.champions.common.commands.SpawnEggCommand;

public final class LanguageProviders {
  public static final String LEVEL_PREFIX = "champions.level";
  public static final String LEVEL_TOOLTIP_KEY = "champions.level.tooltip";
  public static final String AFFIXES_TOOLTIP_KEY = "affixes.tooltip";

  public static LanguageProvider zhCn(PackOutput output) {
    return new ZhCn(output);
  }

  public static void addAffix(LanguageProvider provider, ResourceKey<Affix> key, String name) {
    provider.add(key.identifier().toLanguageKey("affix"), name);
  }

  public static String fullLevelKey(int level) {
    return LEVEL_PREFIX + "." + level;
  }

  public static void addLevel(LanguageProvider provider, int level, String name) {
    provider.add(fullLevelKey(level), name);
  }

  public static void addLevelTooltip(LanguageProvider provider, String name) {
    provider.add(LEVEL_TOOLTIP_KEY, name);
  }

  public static void addAffixesToolTip(LanguageProvider provider, String name) {
    provider.add(AFFIXES_TOOLTIP_KEY, name);
  }

  private LanguageProviders() {
  }

  public static class ZhCn extends LanguageProvider {
    private static final String LOCALE = "zh_cn";

    public ZhCn(PackOutput output) {
      super(output, Champions.MODID, LOCALE);
    }

    @Override
    protected void addTranslations() {
      add(SpawnEggCommand.SUCCESS_KEY, "已给予%s物品");
      add(SpawnEggCommand.FAILED_KEY, "物品%s不是一个刷怪蛋");
      addLevelTooltip("等级：");
      addLevel(1, "I");
      addLevel(2, "II");
      addLevel(3, "III");
      addLevel(4, "IV");
      addLevel(5, "V");
      addAffixesTooltip("已有词缀：");
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

    private void addAffixesTooltip(String name) {
      LanguageProviders.addAffixesToolTip(this, name);
    }

    private void addLevel(int level, String name) {
      LanguageProviders.addLevel(this, level, name);
    }

    private void addLevelTooltip(String name) {
      LanguageProviders.addLevelTooltip(this, name);
    }

    private void addAffix(ResourceKey<Affix> key, String name) {
      LanguageProviders.addAffix(this, key, name);
    }
  }


}
