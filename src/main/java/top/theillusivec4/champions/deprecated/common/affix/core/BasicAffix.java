package top.theillusivec4.champions.deprecated.common.affix.core;

import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.api.data.AffixSetting;

@Deprecated
public class BasicAffix extends AbstractBasicAffix {

  public void tryRegisterEvents() {
    if (this.hasSubscriptions()) {
      NeoForge.EVENT_BUS.register(this);
      Champions.LOGGER.debug("Registered event bus for {}", this.getIdentifier());
    }
  }

  @Override
  public AffixSetting createDefaultSetting() {
    return AffixSetting.empty();
  }
}
