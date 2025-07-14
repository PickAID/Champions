package top.theillusivec4.champions.common.affix.core;

import net.minecraftforge.common.MinecraftForge;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.data.AffixSetting;

public class BasicAffix extends AbstractBasicAffix {

    public void tryRegisterEvents() {
        if (this.hasSubscriptions()) {
            MinecraftForge.EVENT_BUS.register(this);
            Champions.LOGGER.debug("Registered event bus for {}", this.getIdentifier());
        }
    }

    @Override
    public AffixSetting createDefaultSetting() {
        // return default setting(emptySetting)
        return this.setting;
    }
}
