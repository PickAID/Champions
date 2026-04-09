package top.theillusivec4.champions.affix;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class Affixes {
  private Affixes() {
  }

  private static ResourceKey<Affix> register(String name) {
    return ResourceKey.create(ChampionsRegistries.Keys.AFFIX, ChampionsUtil.id(name));
  }

  public static void bootstrap(BootstrapContext<Affix> context) {

  }
}
