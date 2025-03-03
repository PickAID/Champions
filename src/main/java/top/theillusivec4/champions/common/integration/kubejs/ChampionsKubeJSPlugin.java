package top.theillusivec4.champions.common.integration.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import top.theillusivec4.champions.api.AffixRegistry;
import top.theillusivec4.champions.common.integration.kubejs.affixjs.CustomAffix;

public class ChampionsKubeJSPlugin implements KubeJSPlugin {

  @Override
  public void registerBuilderTypes(BuilderTypeRegistry registry) {
    registry.of(AffixRegistry.AFFIX_REGISTRY_KEY, reg -> reg.addDefault(CustomAffix.Builder.class, CustomAffix.Builder::new));
  }
}
