package top.theillusivec4.champions.common.affix.core;

import net.neoforged.neoforge.common.NeoForge;

public class BasicAffix extends AbstractBasicAffix {
  public BasicAffix() {

    if (hasSubscriptions()) {
      NeoForge.EVENT_BUS.register(this);
    }
  }
}
