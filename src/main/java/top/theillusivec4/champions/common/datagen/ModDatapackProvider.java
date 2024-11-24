package top.theillusivec4.champions.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.registry.ModDamageTypes;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {
  private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder();

  public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, BUILDER, Set.of(Champions.MODID));
    addDamageType();
  }

  private void addDamageType() {
    BUILDER.add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);
  }

}
