package top.theillusivec4.champions.common.integration.kubejs.eventjs;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.AffixRegistry;

@Mod.EventBusSubscriber(modid = Champions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventJSHandler {

    @SubscribeEvent
    public static void registerAffix(RegisterEvent event) {
        event.register(AffixRegistry.AFFIXES_REGISTRY_KEY, helper -> {
            EventJSFactory.registerAffixTypes().forEach((key, value) -> helper.register(key, value.build()));
        });
    }
}
