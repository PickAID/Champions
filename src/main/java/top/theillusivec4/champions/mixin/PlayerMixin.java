package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.affix.effect.AffixTarget;
import top.theillusivec4.champions.util.ChampionUtil;

@Mixin(value = Player.class)
public abstract class PlayerMixin extends Avatar {
  protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
    super(type, level);
  }

  @Inject(method = "itemAttackInteraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffectsWithItemSource(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/item/ItemStack;)V", shift = At.Shift.AFTER))
  private void champion$itemAttackInteraction(Entity entity, ItemStack attackingItemStack, DamageSource damageSource, boolean applyToTarget, CallbackInfo ci, @Local ServerLevel serverLevel) {
    ChampionUtil.getHandler(entity).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.VICTIM, entity, damageSource));
    ChampionUtil.getHandler(this).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.ATTACKER, entity, damageSource));
  }

  @Inject(method = "doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;FLnet/minecraft/world/phys/AABB;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private void champion$doSweepAttack(Entity entity, float baseDamage, DamageSource damageSource, float attackStrengthScale, AABB sweepHitBox, CallbackInfo ci, @Local ServerLevel serverLevel, @Local LivingEntity nearby) {
    ChampionUtil.getHandler(nearby).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.VICTIM, nearby, damageSource));
    ChampionUtil.getHandler(this).ifPresent(handler -> handler.doPostAttackEffects(serverLevel, AffixTarget.ATTACKER, entity, damageSource));
  }
}
