package top.theillusivec4.champions.common.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.common.integration.kubejs.affixjs.CustomAffix;

public class ChampionsKubeJSPlugin extends KubeJSPlugin {
    public static final RegistryInfo CUSTOM_AFFIX = RegistryInfo.of(AffixRegistry.AFFIX_REGISTRY_KEY).type(CustomAffix.class);

    @Override
    public void registerEvents() {
        CUSTOM_AFFIX.addType("affix", CustomAffix.Builder.class, CustomAffix.Builder::new, true);
    }

}
