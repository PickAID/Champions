package top.theillusivec4.champions.mixin;

import net.minecraft.core.TypedInstance;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.data.lang.LanguageKeys;

/**
 * 在物品上实现“普通 烈焰人 强敌蛋”的名称显示
 */
@Mixin(value = ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder, TypedInstance<Item>, net.neoforged.neoforge.common.MutableDataComponentHolder, net.neoforged.neoforge.common.extensions.IItemStackExtension {

  @Shadow
  @Final
  private PatchedDataComponentMap components;

  @Inject(method = "getItemName", at = @At(value = "HEAD"), cancellable = true)
  private void champions$getHoverName(CallbackInfoReturnable<Component> cir) {
    ChampionUtil.getHandler((ItemStack) (Object) this).ifPresent(handlerItem -> {
      if (handlerItem.isDisplayHoverName()) {
        Component base = cir.getReturnValue();
        // 普通 烈焰人强敌蛋
        if (this.components.has(DataComponents.ENTITY_DATA)) {
          TypedEntityData<EntityType<?>> typeTypedEntityData = this.components.get(DataComponents.ENTITY_DATA);
          assert typeTypedEntityData != null;
          EntityType<?> entityType = typeTypedEntityData.type();
          base = entityType.getDescription();
        }
        // 高效的复制到临时变量
        Component finalBase = base;
        handlerItem.getPrefixName().ifPresentOrElse(component -> {
          Component component1 = component.copy().append(CommonComponents.space()).append(finalBase).append(Component.translatable(LanguageKeys.SUFFIX_ITEM_CHAMPION_SPAWN_EGG_KEY));
          cir.setReturnValue(component1);
        }, () -> {
          // 烈焰人强敌蛋
          Component component1 = finalBase.copy().append(Component.translatable(LanguageKeys.SUFFIX_ITEM_CHAMPION_SPAWN_EGG_KEY));
          cir.setReturnValue(component1);
        });
      }


    });
//    if (this.has(DataComponents.CUSTOM_NAME)) {
//      cir.setReturnValue(this.getOrDefault(DataComponents.CUSTOM_NAME, cir.getReturnValue()));
//    }
  }
}
