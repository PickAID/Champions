package top.theillusivec4.champions.mixin;

import net.minecraft.core.TypedInstance;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.component.DataComponents;

@Mixin(value = ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder, TypedInstance<Item>, net.neoforged.neoforge.common.MutableDataComponentHolder, net.neoforged.neoforge.common.extensions.IItemStackExtension {
  @Inject(method = "getHoverName", at = @At(value = "RETURN"), cancellable = true)
  private void champions$getHoverName(CallbackInfoReturnable<Component> cir) {
    if (this.has(DataComponents.CUSTOM_NAME)) {
      cir.setReturnValue(this.getOrDefault(DataComponents.CUSTOM_NAME, cir.getReturnValue()));
    }
  }
}
