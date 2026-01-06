package top.theillusivec4.champions.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.affix.effect.AffixTarget;
import top.theillusivec4.champions.util.ChampionUtil;

import java.util.List;

@Mixin(value = EnderDragon.class)
public class EnderDragonMixin extends Mob {
  protected EnderDragonMixin(EntityType<? extends Mob> type, Level level) {
    super(type, level);
  }

  @Inject(method = "knockBack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private void champion$knockBack(ServerLevel level, List<Entity> entities, CallbackInfo ci, @Local LivingEntity livingTarget, @Local DamageSource damageSource) {
    ChampionUtil.getHandler(livingTarget).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.VICTIM, livingTarget, damageSource));
    ChampionUtil.getHandler(this).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.ATTACKER, livingTarget, damageSource));
  }

  @Inject(method = "hurt(Lnet/minecraft/server/level/ServerLevel;Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostAttackEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)V", shift = At.Shift.AFTER))
  private void champion$hurt(ServerLevel level, List<Entity> entities, CallbackInfo ci, @Local Entity entity, @Local DamageSource damageSource) {
    ChampionUtil.getHandler(entity).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.VICTIM, entity, damageSource));
    ChampionUtil.getHandler(this).ifPresent(handler -> handler.doPostAttackEffects(level, AffixTarget.ATTACKER, entity, damageSource));
  }
}
