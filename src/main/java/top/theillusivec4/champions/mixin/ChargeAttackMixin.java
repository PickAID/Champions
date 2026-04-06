package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.ChargeAttack;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.champion.ChampionHelper;

@Mixin(value = ChargeAttack.class)
public abstract class ChargeAttackMixin {
  @Inject(method = "dealDamageToTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private void champion$dealDamageToTarget(ServerLevel level, Animal body, LivingEntity target, CallbackInfo ci, @Local DamageSource damageSource) {
//    ChampionUtil.getHandler(target).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.VICTIM, target, damageSource));
//    ChampionUtil.getHandler(body).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.ATTACKER, target, damageSource));
	  ChampionHelper.doPostAttackEffects(level, target, damageSource);
  }
}
