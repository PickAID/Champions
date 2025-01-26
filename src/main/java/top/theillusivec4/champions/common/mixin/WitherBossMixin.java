package top.theillusivec4.champions.common.mixin;

import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WitherBoss.class)
public class WitherBossMixin {

  @ModifyArg(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/wither/WitherBoss;heal(F)V", ordinal = 0))
  private float customServerAiStep(float originalHealAmount) {
    WitherBoss witherBoss = (WitherBoss) (Object) this;
    // 当前无敌帧
    int currentInvulnerableTicks = witherBoss.getInvulnerableTicks();
    // 最大生命值
    float maxHealth = witherBoss.getMaxHealth();

    // only modify powered wither boss
    if (currentInvulnerableTicks <= 10 && maxHealth != 300) {
      return maxHealth - witherBoss.getHealth();
    }

    return originalHealAmount;
  }
}
