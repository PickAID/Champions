package top.theillusivec4.champions.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.KubeStartupEvent;
import top.theillusivec4.champions.api.IAffix;

public abstract class CustomAffixEventJS implements KubeStartupEvent {
  private final IAffix affix;

  public CustomAffixEventJS(IAffix affix) {
    this.affix = affix;
  }

  public IAffix getAffix() {
    return affix;
  }

  public static class OnBuild extends CustomAffixEventJS implements KubeStartupEvent {
    public OnBuild(IAffix affix) {
      super(affix);
    }
  }

}
