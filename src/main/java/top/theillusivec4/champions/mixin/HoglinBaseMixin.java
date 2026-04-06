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
import top.theillusivec4.champions.champion.ChampionHelper;

@Mixin(value = HoglinBase.class)
public interface HoglinBaseMixin {
  @Inject(method = "hurtAndThrowTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private static void champion$hurtAndThrowTarget(ServerLevel level, LivingEntity body, LivingEntity target, CallbackInfoReturnable<Boolean> cir, @Local DamageSource damageSource) {
//    ChampionUtil.getHandler(target).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.VICTIM, target, damageSource));
//    ChampionUtil.getHandler(body).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.VICTIM, target, damageSource));
	  ChampionHelper.doPostAttackEffects(level, target, damageSource);
  }
}
