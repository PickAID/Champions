package top.theillusivec4.champions.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.world.item.champion.ChampionEggHelper;

import java.util.function.Consumer;

/**
 * 实现了使用刷怪蛋生成时从物品读取冠军数据的功能
 *
 * @param <T>
 */
@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity> implements EntityTypeTest<Entity, T>, FeatureElement {
  @Inject(method = "appendCustomEntityStackConfig", at = @At(value = "RETURN"), cancellable = true)
  private static <T extends Entity> void champions$appendCustomEntityStackConfig(Consumer<T> consumer, ServerLevel level, ItemStack stack, Player player, CallbackInfoReturnable<Consumer<T>> cir) {
    cir.setReturnValue(
      cir.getReturnValue().andThen(entity -> ChampionEggHelper.applyEntityConfig(stack, entity))
    );
  }

}
