package top.theillusivec4.champions.champion.item;

import net.minecraft.world.item.ItemStack;
import top.theillusivec4.champions.champion.ChampionHandler;

public interface ChampionHandlerItem extends ChampionHandler {
  ItemStack getItem();
}
