package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
  public LivingEntityMixin(EntityType<?> type, Level level) {
    super(type, level);
  }

  @Inject(method = "isInvulnerableTo", at = @At(value = "RETURN"), cancellable = true)
  private void champions$isInvulnerableTo(ServerLevel level, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
    if (!cir.getReturnValue()) {
      ChampionUtil.getHandler(this).ifPresent(handler -> cir.setReturnValue(handler.isImmuneToDamage(level, (LivingEntity) (Object) this, source)));
    }
  }

  @Inject(method = "getKnockback", at = @At(value = "RETURN"), cancellable = true)
  private void champion$getKnockback(Entity target, DamageSource damageSource, CallbackInfoReturnable<Float> cir) {
    if (this.level() instanceof ServerLevel serverLevel) {
      ChampionUtil.getHandler(this).ifPresent(handler -> {
        float knockback = (float) ((LivingEntity) (Object) this).getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        float value = handler.modifyKnockback(serverLevel, target, damageSource, knockback) / 2.0f + cir.getReturnValue();
        cir.setReturnValue(value);
      });
    }
  }

  @Inject(method = "stabAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private void champion$stabAttack(EquipmentSlot weaponSlot, Entity target, float baseDamage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts, CallbackInfoReturnable<Boolean> cir, @Local ServerLevel serverLevel, @Local DamageSource damageSource) {
    ChampionUtil.getHandler(target).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.VICTIM, target, damageSource));
    ChampionUtil.getHandler(this).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.VICTIM, target, damageSource));
  }
}
