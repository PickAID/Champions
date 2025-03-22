package top.theillusivec4.champions.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.server.command.ChampionsCommand;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
  @ModifyArg(
    method = "handlePickItemFromEntity",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;tryPickItem(Lnet/minecraft/world/item/ItemStack;)V")
  )
  private ItemStack modifyItemStack(ItemStack original) {
    var pickedEntity = Minecraft.getInstance().crosshairPickEntity;
    var player = Minecraft.getInstance().player;
    var gameMode = Minecraft.getInstance().gameMode;
    if (pickedEntity != null && player != null && gameMode != null) {
      var championOptional = ChampionAttachment.getAttachment(pickedEntity);
      if (championOptional.isPresent()) {
        var champion = championOptional.get();
        var clientChampion = champion.getClient();

        if (ChampionHelper.isValidChampion(clientChampion)) {
          var type = champion.getLivingEntity().getType();
          var tier = clientChampion.getRank().map(Tuple::getA).orElseThrow();
          var affixes = clientChampion.getAffixes();
          return ChampionsCommand.createEgg(type, tier, affixes);
        }
      }
    }
    return original;
  }
}
