package top.theillusivec4.champions.common.integration.kubejs;

import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.integration.kubejs.event.CustomAffixEventJS;
import top.theillusivec4.champions.common.integration.kubejs.event.KubeJsEvents;

public class KubeJSHooks {

  public static void onKubeJSAffixBuild(IAffix affix) {
    KubeJsEvents.ON_BUILD.post(new CustomAffixEventJS.OnBuild(affix));
  }
}
