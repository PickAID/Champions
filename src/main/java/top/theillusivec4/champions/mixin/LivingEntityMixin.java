package top.theillusivec4.champions.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.affix.AffixHelper;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
  public LivingEntityMixin(EntityType<?> type, Level level) {
    super(type, level);
  }

  @Shadow
  public abstract void knockback(double power, double xd, double zd);

  @Inject(method = "isInvulnerableTo", at = @At(value = "RETURN"), cancellable = true)
  private void champions$isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
    if (!cir.getReturnValue() && this.level() instanceof ServerLevel level) {
      boolean result = AffixHelper.isImmuneToDamage(level, this, source);
      cir.setReturnValue(result);
    }
  }

  @Inject(method = "getKnockback", at = @At(value = "RETURN"), cancellable = true)
  private void champion$getKnockback(Entity attacker, DamageSource damageSource, CallbackInfoReturnable<Float> cir) {
    if (this.level() instanceof ServerLevel level) {
      float knockback = (float) ((LivingEntity) (Object) this).getAttributeValue(Attributes.ATTACK_KNOCKBACK);
      float value = AffixHelper.modifyKnockback(level, attacker, damageSource, knockback) / 2.0f + cir.getReturnValue();
      cir.setReturnValue(value);
    }
  }
}
