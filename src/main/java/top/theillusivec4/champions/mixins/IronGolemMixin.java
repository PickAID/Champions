package top.theillusivec4.champions.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.api.affix.AffixHelper;

@Mixin(value = IronGolem.class)
public class IronGolemMixin extends AbstractGolem {
	protected IronGolemMixin(EntityType<? extends AbstractGolem> type, Level level) {
		super(type, level);
	}

	@Inject(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
	private void champion$doHurtTarget(ServerLevel level, Entity target, CallbackInfoReturnable<Boolean> cir, @Local(name = "damageSource") DamageSource damageSource) {
		AffixHelper.doPostAttackEffects(level, target, damageSource);
	}
}
