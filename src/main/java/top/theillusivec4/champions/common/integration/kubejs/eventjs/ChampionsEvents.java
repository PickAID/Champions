package top.theillusivec4.champions.common.integration.kubejs.eventjs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface ChampionsEvents {
	EventGroup GROUP = EventGroup.of("ChampionsEvents");
	EventHandler REGISTER = GROUP.startup("register", () -> RegisterAffixEventJS.class);
}
