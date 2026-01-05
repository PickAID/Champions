package top.theillusivec4.champions.common.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.common.item.DataComponentTypes;
import top.theillusivec4.champions.common.item.components.ItemAffixes;
import top.theillusivec4.champions.common.util.Utils;

import java.util.function.Consumer;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity> implements EntityTypeTest<Entity, T>, FeatureElement {
  @Inject(method = "appendCustomEntityStackConfig", at = @At(value = "RETURN"), cancellable = true)
  private static <T extends Entity> void champion$appendCustomEntityStackConfig(Consumer<T> initialConfig, Level level, ItemStack itemStack, @Nullable LivingEntity user, CallbackInfoReturnable<Consumer<T>> cir) {
    ItemAffixes itemAffixes = itemStack.get(DataComponentTypes.ITEM_AFFIXES);
    if (itemAffixes != null) {
      cir.setReturnValue(champion$appendAffixesConfig(itemAffixes, cir.getReturnValue()));
    }
  }

  @Unique
  private static <T extends Entity> Consumer<T> champion$appendAffixesConfig(ItemAffixes itemAffixes, Consumer<T> consumer) {
    return consumer.andThen(entity -> Utils.getChampionHandler(entity).ifPresent(handler -> {
      handler.setChampionLevel(itemAffixes.level());
      handler.updateAffixes(mutable -> {
        for (Holder<Affix> affix : itemAffixes.affixes()) {
          mutable.add(affix);
        }
      });
    }));
  }
}
