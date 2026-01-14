package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.champions.champion.ChampionUtil;

@Mixin(value = AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
  @Shadow
  private @Nullable ItemStack firedFromWeapon;

  protected AbstractArrowMixin(EntityType<? extends Projectile> type, Level level) {
    super(type, level);
  }

  @ModifyVariable(method = "doKnockback", at = @At(value = "STORE"))
  private double champion$doKnockback(double knockback, @Local(argsOnly = true) LivingEntity mob, @Local(argsOnly = true) DamageSource damageSource) {
    if (this.firedFromWeapon != null && this.level() instanceof ServerLevel serverLevel) {
      return ChampionUtil.getHandler(this)
        .map(handler -> (double) (handler.modifyKnockback(serverLevel, mob, damageSource, (float) knockback)))
        .orElse(knockback);
    }
    return knockback;
  }

}
