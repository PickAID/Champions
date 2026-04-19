package top.theillusivec4.champions.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.api.affix.AffixHelper;

@Mixin(value = LlamaSpit.class)
public abstract class LlamaSpitMixin extends Projectile {
  protected LlamaSpitMixin(EntityType<? extends Projectile> type, Level level) {
    super(type, level);
  }

  @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private void champions$onHitEntity(EntityHitResult hitResult, CallbackInfo ci, @Local DamageSource damageSource, @Local ServerLevel serverLevel) {
    Entity victim = hitResult.getEntity();
    AffixHelper.doPostAttackEffects(serverLevel, victim, damageSource);
  }
}
