package top.theillusivec4.champions.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.server.command.ChampionsCommand;

import java.util.List;

@Mixin(ForgeHooks.class)
public abstract class ForgeHooksMixin {

    @ModifyArg(
            method = "onPickBlock", // 目标方法
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setPickedItem(Lnet/minecraft/item/ItemStack;)V")
    )
    private static ItemStack champions$modifyItemStack(ItemStack original) {
        Entity pickedEntity = Minecraft.getInstance().crosshairPickEntity;
        ClientPlayerEntity player = Minecraft.getInstance().player;
        PlayerController gameMode = Minecraft.getInstance().gameMode;
        if (pickedEntity != null && player != null && gameMode != null) {
            LazyOptional<IChampion> championOptional = ChampionCapability.getCapability(pickedEntity);
            if (championOptional.isPresent()) {
                IChampion champion = championOptional.orElseThrow(IllegalStateException::new);
                IChampion.Client clientChampion = champion.getClient();

                if (ChampionHelper.isValidChampion(clientChampion)) {
                    EntityType<?> type = champion.getLivingEntity().getType();
                    Integer tier = clientChampion.getRank().map(Tuple::getA).orElseThrow(IllegalStateException::new);
                    List<IAffix> affixes = clientChampion.getAffixes();
                    return ChampionsCommand.createEgg(type, tier, affixes);
                }
            }
        }
        return original;
    }
}
