package top.theillusivec4.champions.common.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import net.minecraft.advancements.critereon.MinMaxBounds;
import top.theillusivec4.champions.common.integration.kubejs.eventjs.ChampionsEvents;
import top.theillusivec4.champions.common.integration.kubejs.wrapper.MinMaxBoundsIntsJS;

public class ChampionsKubeJSPlugin implements KubeJSPlugin {

  @Override
  public void registerEvents(EventGroupRegistry registry) {
    registry.register(ChampionsEvents.GROUP);
  }

  @Override
  public void registerTypeWrappers(TypeWrapperRegistry registry) {
    registry.register(MinMaxBounds.Ints.class, MinMaxBoundsIntsJS::of);
  }

}
