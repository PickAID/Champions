package top.theillusivec4.champions.common.integration.kubejs;

import net.neoforged.fml.ModLoader;
import top.theillusivec4.champions.api.IAffix;

public class ChampionHooks {
  public static void onCustomAffixBuild(IAffix affix) {
    ModLoader.postEvent(new CustomAffixEvent.OnBuild(affix));
  }
}
