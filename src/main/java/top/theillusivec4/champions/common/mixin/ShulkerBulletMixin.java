package top.theillusivec4.champions.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.api.affix.effect.AffixTarget;
import top.theillusivec4.champions.common.util.Utils;

@Mixin(value = ShulkerBullet.class)
public abstract class ShulkerBulletMixin extends Projectile {
  protected ShulkerBulletMixin(EntityType<? extends Projectile> type, Level level) {
    super(type, level);
  }

  @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private void champion$onHitEntity(EntityHitResult hitResult, CallbackInfo ci, @Local ServerLevel serverLevel, @Local DamageSource damageSource) {
    Entity victim = hitResult.getEntity();
    Utils.getChampionHandler(victim).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.VICTIM, victim, damageSource));
    Entity attacker = this.getOwner();
    if (attacker != null) {
      Utils.getChampionHandler(attacker).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.ATTACKER, victim, damageSource));
    }
    Utils.getChampionHandler(this).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.DAMAGING_ENTITY, victim, damageSource));
  }
}
