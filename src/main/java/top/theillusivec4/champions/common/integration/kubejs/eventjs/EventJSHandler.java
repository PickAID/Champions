package top.theillusivec4.champions.common.integration.kubejs.eventjs;

import net.neoforged.neoforge.registries.RegisterEvent;
import top.theillusivec4.champions.api.AffixRegistry;

public class EventJSHandler {
  public static void registerAffix(RegisterEvent event) {
    event.register(AffixRegistry.AFFIX_REGISTRY_KEY, helper ->
      EventJSFactory.registerAffixTypes().forEach((key, value) -> helper.register(key, value.build()))
    );
  }
}
