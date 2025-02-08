package top.theillusivec4.champions.common.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import top.theillusivec4.champions.common.integration.kubejs.eventjs.ChampionsEvents;

public class ChampionsKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        ChampionsEvents.GROUP.register();
    }
}
