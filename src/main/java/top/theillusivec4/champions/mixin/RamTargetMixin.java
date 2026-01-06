package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.RamTarget;
import net.minecraft.world.entity.animal.goat.Goat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.affix.effect.AffixTarget;
import top.theillusivec4.champions.util.ChampionUtil;

@Mixin(value = RamTarget.class)
public abstract class RamTargetMixin {
  @Inject(method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/goat/Goat;J)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private void champion$tick(ServerLevel level, Goat body, long timestamp, CallbackInfo ci, @Local DamageSource damageSource, @Local LivingEntity ramTarget) {
    ChampionUtil.getHandler(ramTarget).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.VICTIM, ramTarget, damageSource));
    ChampionUtil.getHandler(body).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.ATTACKER, ramTarget, damageSource));
  }
}
