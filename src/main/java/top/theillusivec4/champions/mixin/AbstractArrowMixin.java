package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.champions.world.entity.affix.AffixHelper;

@Mixin(value = AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {

  protected AbstractArrowMixin(EntityType<? extends Projectile> type, Level level) {
    super(type, level);
  }

  // 这里在26.1是个Bug: 错误的调用modifyDamage
  @ModifyVariable(method = "doKnockback", at = @At(value = "STORE"), name = "d0")
  private double champions$doKnockback(double knockback, @Local(argsOnly = true) LivingEntity livingEntity, @Local(argsOnly = true) DamageSource damageSource) {
    if (this.level() instanceof ServerLevel level) {
      return AffixHelper.modifyKnockback(level, livingEntity, damageSource, (float) knockback);
    }
    return knockback;
  }

}
