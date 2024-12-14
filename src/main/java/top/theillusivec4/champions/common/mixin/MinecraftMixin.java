package top.theillusivec4.champions.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.server.command.ChampionsCommand;

@Mixin(Minecraft.class)
public class MinecraftMixin {

  @ModifyVariable(
    method = "pickBlock", // 目标方法
    at = @At(value = "STORE", target = "Lnet/minecraft/world/entity/player/Inventory;setPickedItem(Lnet/minecraft/world/item/ItemStack;)V")
  )
  private ItemStack modifyItemStack(ItemStack original) {
    var pickedEntity = Minecraft.getInstance().crosshairPickEntity;
    var player = Minecraft.getInstance().player;
    var gameMode = Minecraft.getInstance().gameMode;
    if (pickedEntity != null) {
      var championOptional = ChampionAttachment.getAttachment(pickedEntity);
      if (championOptional.isPresent()) {
        var champion = championOptional.get();
        var type = champion.getLivingEntity().getType();

        IChampion.Client clientChampion = champion.getClient();
        if (player != null && gameMode != null && ChampionHelper.isValidChampion(clientChampion)) {
          var tier = clientChampion.getRank().map(Tuple::getA).orElseThrow();
          var affixes = clientChampion.getAffixes();
          return ChampionsCommand.createEgg(type, tier, affixes);
        }
      }
    }
    return original;
  }
}
