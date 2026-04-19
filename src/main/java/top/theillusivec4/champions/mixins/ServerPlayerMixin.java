package top.theillusivec4.champions.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.api.affix.AffixHelper;

@Mixin(value = ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
  public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
    super(level, pos, yRot, gameProfile);
  }

  @Inject(method = "setGameMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;runLocationChangedEffects(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V", shift = At.Shift.AFTER))
  private void champions$setGameMode$runLocationChangedEffects(GameType gameMode, CallbackInfoReturnable<Boolean> cir) {
    if (this.level() instanceof ServerLevel level) {
      AffixHelper.runLocationChangedEffects(level, this);
    }
  }

  @Inject(method = "setGameMode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;stopLocationBasedEffects(Lnet/minecraft/world/entity/LivingEntity;)V", shift = At.Shift.AFTER))
  private void champions$setGameMode$stopLocationChangedEffects(GameType gameMode, CallbackInfoReturnable<Boolean> cir) {
    if (this.level() instanceof ServerLevel level) {
      AffixHelper.stopLocationChangedEffects(level, this);
    }
  }
}
