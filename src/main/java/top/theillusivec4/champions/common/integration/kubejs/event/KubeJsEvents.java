package top.theillusivec4.champions.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface KubeJsEvents {
  EventGroup GROUP = EventGroup.of("AffixEvents");
  EventHandler ON_BUILD = GROUP.startup("onBuild", () -> CustomAffixEventJS.OnBuild.class);
}
