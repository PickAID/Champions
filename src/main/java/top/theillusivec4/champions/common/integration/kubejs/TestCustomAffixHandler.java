package top.theillusivec4.champions.common.integration.kubejs;

import top.theillusivec4.champions.Champions;

public class TestCustomAffixHandler {

  public static void testOnCustomAffixBuild(CustomAffixEvent.OnBuild event) {
    Champions.LOGGER.info(event.getAffix());
  }
}
