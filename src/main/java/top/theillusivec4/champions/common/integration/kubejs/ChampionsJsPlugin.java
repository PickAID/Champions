package top.theillusivec4.champions.common.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.common.integration.kubejs.event.KubeJsEvents;

public class ChampionsJsPlugin implements KubeJSPlugin {
  @Override
  public void init() {
    KubeJSPlugin.super.init();
  }

  @Override
  public void registerEvents(EventGroupRegistry registry) {
    registry.register(KubeJsEvents.GROUP);
  }

  @Override
  public void registerBuilderTypes(BuilderTypeRegistry registry) {
    registry.addDefault(AffixRegistry.AFFIX_REGISTRY_KEY, AffixBuilderJS.class, AffixBuilderJS::new);
  }
}
