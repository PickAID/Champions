package top.theillusivec4.champions.data;

import net.minecraft.core.RegistrySetBuilder;
import top.theillusivec4.champions.affix.Affixes;
import top.theillusivec4.champions.registries.Registries;

public final class ChampionsRegistries {
  public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
    .add(Registries.AFFIX, Affixes::bootstrap);

  private ChampionsRegistries() {
  }
}
