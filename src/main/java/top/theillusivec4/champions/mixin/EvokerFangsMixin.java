package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.champion.ChampionHelper;

@Mixin(value = EvokerFangs.class)
public abstract class EvokerFangsMixin extends Entity {

	public EvokerFangsMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Inject(method = "dealDamageTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
	private void champion$dealDamageTo(LivingEntity entity, CallbackInfo ci, @Local ServerLevel serverLevel, @Local DamageSource damageSource) {
//    ChampionUtil.getHandler(entity).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.VICTIM, entity, damageSource));
//    ChampionUtil.getHandler(this).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.VICTIM, entity, damageSource));
		ChampionHelper.doPostAttackEffects(serverLevel, entity, damageSource);
	}
}
