package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.champions.champion.ChampionUtil;

@Mixin(value = AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
  @Shadow
  private @Nullable ItemStack firedFromWeapon;

  @Shadow
  private int life;

  protected AbstractArrowMixin(EntityType<? extends Projectile> type, Level level) {
    super(type, level);
  }

  //  @WrapOperation(method = "doKnockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;modifyKnockback(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;F)F"))
//  private float champion$doKnockback(ServerLevel serverLevel, ItemStack itemStack, Entity victim, DamageSource damageSource, float knockback, Operation<Float> original) {
//    float value = original.call(serverLevel, itemStack, victim, damageSource, knockback);
//
//    return value;
//  }
  @ModifyVariable(method = "doKnockback", at = @At(value = "STORE"))
  private double champion$doKnockback(double knockback, @Local(argsOnly = true) DamageSource damageSource) {
    if (this.firedFromWeapon != null && this.level() instanceof ServerLevel serverLevel) {
      return ChampionUtil.getHandler(this)
        .map(handler -> (double) (handler.modifyKnockback(serverLevel, damageSource, (float) knockback)))
        .orElse(knockback);
    }
    return knockback;
  }

}
