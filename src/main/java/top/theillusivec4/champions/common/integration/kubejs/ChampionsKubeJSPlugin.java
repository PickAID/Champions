package top.theillusivec4.champions.common.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import top.theillusivec4.champions.common.integration.kubejs.eventjs.ChampionsEvents;

public class ChampionsKubeJSPlugin implements KubeJSPlugin {

  @Override
  public void registerEvents(EventGroupRegistry registry) {
    registry.register(ChampionsEvents.GROUP);
  }

}
