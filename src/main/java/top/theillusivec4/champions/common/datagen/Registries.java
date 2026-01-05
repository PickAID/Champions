package top.theillusivec4.champions.common.datagen;

import net.minecraft.core.RegistrySetBuilder;
import top.theillusivec4.champions.api.affix.Affixes;

public final class Registries {
  public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
    .add(top.theillusivec4.champions.common.registries.Registries.AFFIX, Affixes::bootstrap);

  private Registries() {
  }
}
