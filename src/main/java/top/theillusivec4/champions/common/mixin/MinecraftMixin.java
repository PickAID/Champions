package top.theillusivec4.champions.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.server.command.ChampionsCommand;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @ModifyVariable(
            method = "pickBlock", // 目标方法
            at = @At(value = "STORE", target = "Lnet/minecraft/world/entity/player/Inventory;setPickedItem(Lnet/minecraft/world/item/ItemStack;)V")
    )
    private ItemStack champions$modifyItemStack(ItemStack original) {
        var pickedEntity = Minecraft.getInstance().crosshairPickEntity;
        var player = Minecraft.getInstance().player;
        var gameMode = Minecraft.getInstance().gameMode;
        if (pickedEntity != null && player != null && gameMode != null) {
            var championOptional = ChampionCapability.getCapability(pickedEntity);
            if (championOptional.isPresent()) {
                var champion = championOptional.orElseThrow(IllegalStateException::new);
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
