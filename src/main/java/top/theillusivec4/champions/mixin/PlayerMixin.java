package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.affix.AffixHelper;

@Mixin(value = Player.class)
public abstract class PlayerMixin extends LivingEntity {
  protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
    super(type, level);
  }

  @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER, ordinal = 0))
  private void champion$attack(Entity target, CallbackInfo ci, @Local ServerLevel serverLevel, @Local DamageSource damagesource) {
    AffixHelper.doPostAttackEffects(serverLevel, target, damagesource);
  }
}
