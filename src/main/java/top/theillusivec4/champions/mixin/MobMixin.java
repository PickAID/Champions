package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.champion.ChampionHelper;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.ChampionUtil;

@Mixin(value = Mob.class)
public abstract class MobMixin extends LivingEntity {
  protected MobMixin(EntityType<? extends LivingEntity> type, Level level) {
    super(type, level);
  }

  @Inject(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private void champion$doHurtTarget(ServerLevel level, Entity target, CallbackInfoReturnable<Boolean> cir, @Local DamageSource damageSource) {
//    ChampionUtil.getHandler(target).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.VICTIM, target, damageSource));
//    ChampionUtil.getHandler(this).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.ATTACKER, target, damageSource));
	  ChampionHelper.doPostAttackEffects(level, target, damageSource);
  }
}
