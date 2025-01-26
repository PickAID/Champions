package top.theillusivec4.champions.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.util.ChampionBuilder;

@Mixin(EndDragonFight.class)
public class EndDragonFightMixin {

  @Inject(method = "createNewDragon", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;setDragonFight(Lnet/minecraft/world/level/dimension/end/EndDragonFight;)V"))
  private void createNewDragon(CallbackInfoReturnable<EnderDragon> cir, @Local EnderDragon enderDragon) {
    if (enderDragon.level() instanceof ServerLevel) {
      ChampionAttachment.getAttachment(enderDragon).ifPresent(
        champion -> {
          champion.getServer().setRank(RankManager.getLowestRank());
          ChampionBuilder.spawn(champion);
        }
      );
    }
  }
}
