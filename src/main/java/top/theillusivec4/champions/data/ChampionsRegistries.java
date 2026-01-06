package top.theillusivec4.champions.data;

import net.minecraft.core.RegistrySetBuilder;
import top.theillusivec4.champions.affix.Affixes;

public final class ChampionsRegistries {
  public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
    .add(top.theillusivec4.champions.deprecated.common.registries.Registries.AFFIX, Affixes::bootstrap);

  private ChampionsRegistries() {
  }
}
