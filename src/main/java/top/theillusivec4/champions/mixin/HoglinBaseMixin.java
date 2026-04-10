package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.affix.AffixHelper;

@Mixin(value = HoglinBase.class)
public interface HoglinBaseMixin {
  @Inject(method = "hurtAndThrowTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private static void champions$hurtAndThrowTarget(LivingEntity hoglin, LivingEntity target, CallbackInfoReturnable<Boolean> cir, @Local DamageSource damageSource) {
    if (hoglin.level() instanceof ServerLevel level) {
      AffixHelper.doPostAttackEffects(level, target, damageSource);
    }
  }
}
