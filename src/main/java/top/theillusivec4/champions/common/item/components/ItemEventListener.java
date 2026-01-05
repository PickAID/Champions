package top.theillusivec4.champions.common.item.components;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.common.datagen.LanguageProviders;
import top.theillusivec4.champions.common.item.DataComponentTypes;

import java.util.List;

public final class ItemEventListener {

  public static void register() {
    NeoForge.EVENT_BUS.register(new ItemEventListener());
  }

  private ItemEventListener() {
  }

  @SubscribeEvent
  public void onItemTooltip(ItemTooltipEvent event) {
    ItemStack itemStack = event.getItemStack();
    ItemAffixes itemAffixes = itemStack.get(DataComponentTypes.ITEM_AFFIXES);
    List<Component> list = event.getToolTip();
    if (itemAffixes != null) {
      list.add(
        Component.translatable(LanguageProviders.LEVEL_TOOLTIP_KEY).withStyle(ChatFormatting.GRAY)
          .append(Component.translatable(LanguageProviders.LEVEL_PREFIX + "." + itemAffixes.level()))
      );
      list.add(Component.translatable(LanguageProviders.AFFIXES_TOOLTIP_KEY, itemAffixes.level()).withStyle(ChatFormatting.GRAY));
      for (Holder<Affix> affix : itemAffixes.affixes()) {
        list.add(CommonComponents.space().append(affix.value().description()));
      }
    }
  }
}
