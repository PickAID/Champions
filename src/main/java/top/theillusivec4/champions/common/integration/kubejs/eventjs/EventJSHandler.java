package top.theillusivec4.champions.common.integration.kubejs.eventjs;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixRegistry;

@EventBusSubscriber(modid = Champions.MODID, bus = EventBusSubscriber.Bus.MOD)
public class EventJSHandler {

    @SubscribeEvent
    public static void registerAffix(RegisterEvent event) {
        event.register(AffixRegistry.AFFIX_REGISTRY_KEY, helper ->
          EventJSFactory.registerAffixTypes().forEach((key, value) -> helper.register(key, value.build()))
        );
    }
}
