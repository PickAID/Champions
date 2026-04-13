package top.theillusivec4.champions.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.theillusivec4.champions.championegg.ChampionMobEggHelper;

import java.util.List;

@EventBusSubscriber
public final class ItemEventHandler {
  private ItemEventHandler() {
  }

  @SubscribeEvent
  private static void addTooltips(ItemTooltipEvent event) {
    ItemStack itemStack = event.getItemStack();
    Item.TooltipContext context = event.getContext();
    List<Component> list = event.getToolTip();
    TooltipFlag flags = event.getFlags();
    ChampionMobEggHelper.addToTooltip(itemStack, context, list::add, flags);
  }
}
