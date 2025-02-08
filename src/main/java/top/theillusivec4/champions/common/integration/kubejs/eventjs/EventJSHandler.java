package top.theillusivec4.champions.common.integration.kubejs.eventjs;

import net.minecraftforge.registries.RegisterEvent;
import top.theillusivec4.champions.api.AffixRegistry;

public class EventJSHandler {
    public static void registerAffix(RegisterEvent event) {
        event.register(AffixRegistry.AFFIXES_REGISTRY_KEY, helper -> {
            EventJSFactory.registerAffixTypes().forEach((key, value) -> helper.register(key, value.build()));
        });
    }
}
