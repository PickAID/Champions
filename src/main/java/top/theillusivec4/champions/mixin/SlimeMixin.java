package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.world.entity.affix.AffixHelper;

@Mixin(value = Slime.class)
public abstract class SlimeMixin extends Mob {
  protected SlimeMixin(EntityType<? extends Mob> type, Level level) {
    super(type, level);
  }

  @Inject(method = "dealDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private void champion$dealDamage(LivingEntity livingEntity, CallbackInfo ci, @Local ServerLevel level, @Local DamageSource damageSource) {
    AffixHelper.doPostAttackEffects(level, livingEntity, damageSource);
  }
}
