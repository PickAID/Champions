package top.theillusivec4.champions.common.mixin;

import com.google.common.collect.BiMap;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootContextParamSets.class)
public interface LootContextParamSetsAccessor {
  @Accessor("REGISTRY")
  static BiMap<Identifier, ContextKeySet> getRegistry() {
    throw new AssertionError();
  }
}
