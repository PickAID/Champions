package top.theillusivec4.champions.common.mixin;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(BlockModelWrapper.class)
public interface BlockModelWrapperMixin {
  @Accessor("tints")
  List<ItemTintSource> getTints();
}
