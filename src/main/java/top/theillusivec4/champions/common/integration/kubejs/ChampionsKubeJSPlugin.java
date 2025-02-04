package top.theillusivec4.champions.common.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.advancements.critereon.MinMaxBounds;
import top.theillusivec4.champions.common.integration.kubejs.eventjs.ChampionsEvents;
import top.theillusivec4.champions.common.integration.kubejs.wrapper.MinMaxBoundsIntsJS;

public class ChampionsKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        ChampionsEvents.GROUP.register();
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(MinMaxBounds.Ints.class, MinMaxBoundsIntsJS::of);
    }
}
