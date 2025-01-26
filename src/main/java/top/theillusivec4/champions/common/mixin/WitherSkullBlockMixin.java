package top.theillusivec4.champions.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.util.ChampionBuilder;

@Mixin(WitherSkullBlock.class)
public abstract class WitherSkullBlockMixin {

  @Inject(method = "checkSpawn(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/SkullBlockEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CarvedPumpkinBlock;clearPatternBlocks(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/pattern/BlockPattern$BlockPatternMatch;)V", shift = At.Shift.AFTER))
  private static void champions$checkSpawn(Level level, BlockPos pos, SkullBlockEntity blockEntity, CallbackInfo ci, @Local WitherBoss witherboss) {
    if (level instanceof ServerLevel) {
      ChampionAttachment.getAttachment(witherboss).ifPresent(
        champion -> {
          champion.getServer().setRank(RankManager.getLowestRank()); // check trial spawner
          ChampionBuilder.spawn(champion);
        }
      );
    }
  }
}
