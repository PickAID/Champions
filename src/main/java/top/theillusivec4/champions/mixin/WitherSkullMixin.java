package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.world.entity.affix.AffixHelper;

@Mixin(value = WitherSkull.class)
public abstract class WitherSkullMixin extends AbstractHurtingProjectile {
	protected WitherSkullMixin(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
		super(type, level);
	}

	@Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
	private void champion$onHitEntity(EntityHitResult hitResult, CallbackInfo ci, @Local(name = "serverLevel") ServerLevel serverLevel, @Local(name = "damageSource") DamageSource damageSource) {
		Entity victim = hitResult.getEntity();
		AffixHelper.doPostAttackEffects(serverLevel, victim, damageSource);
	}
}
