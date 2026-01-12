package top.theillusivec4.champions.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.world.loot.modifier.ChampionLootModifier;

import java.util.concurrent.CompletableFuture;

public class LootModifierProvider extends GlobalLootModifierProvider {
  private static final String ID = "champion";

  public LootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, Champions.MODID);
  }

  @Override
  protected void start() {
    this.add(ID, ChampionLootModifier.INSTANCE);
  }
}
