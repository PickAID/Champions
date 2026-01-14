package top.theillusivec4.champions.champion.item;

import net.minecraft.world.item.ItemStack;

/**
 * 物品的通用实现
 * @param itemStack 物品
 */
public record CommonItem(ItemStack itemStack) implements ChampionHandlerItem {

}
