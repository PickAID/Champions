package top.theillusivec4.champions.common.affix.core;

import net.minecraftforge.common.MinecraftForge;

public class BasicAffix extends AbstractBasicAffix {
    public BasicAffix() {

        if (hasSubscriptions()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }
}
