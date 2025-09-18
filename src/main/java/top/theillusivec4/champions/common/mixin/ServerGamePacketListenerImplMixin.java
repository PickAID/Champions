package top.theillusivec4.champions.common.mixin;

import net.minecraft.network.protocol.game.ServerboundPickItemFromEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.server.command.ChampionsCommand;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
  @Shadow
  public ServerPlayer player;

  @Shadow
  protected abstract void tryPickItem(ItemStack stack);

  @Inject(
    method = "handlePickItemFromEntity",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;tryPickItem(Lnet/minecraft/world/item/ItemStack;)V"),
    cancellable = true
  )
  private void modifyItemStack(ServerboundPickItemFromEntityPacket pickItemFromEntityPacket, CallbackInfo ci) {
    var level = this.player.level();
    if (!(level instanceof ServerLevel serverLevel)) return;
    var pickedEntity = serverLevel.getEntity(pickItemFromEntityPacket.id());
    if (pickedEntity != null && this.player != null) {
      var championOptional = ChampionAttachment.getAttachment(pickedEntity);
      if (championOptional.isPresent()) {
        var champion = championOptional.get();
        var serverChampion = champion.getServer();

        if (ChampionHelper.isValidChampion(serverChampion)) {
          var type = champion.getLivingEntity().getType();
          var tier = serverChampion.getRank().map(Rank::getTier).orElseThrow();
          var affixes = serverChampion.getAffixes();
          var createdEgg = ChampionsCommand.createEgg(type, tier, affixes);
          // overwrite original tryPickItem invoke
          this.tryPickItem(createdEgg);
          ci.cancel();
        }
      }
    }
  }
}
