package top.theillusivec4.champions.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.components.DataComponents;
import top.theillusivec4.champions.components.ItemAffixes;
import top.theillusivec4.champions.data.LanguageKeys;

import java.util.List;
import java.util.Objects;

public final class ItemEventListener {

  public static void register() {
    NeoForge.EVENT_BUS.register(new ItemEventListener());
  }

  private ItemEventListener() {
  }

  @SubscribeEvent
  public void onItemTooltip(ItemTooltipEvent event) {
    ItemStack itemStack = event.getItemStack();
    List<Component> list = event.getToolTip();
    if (itemStack.getOrDefault(DataComponents.SHOW, false)) {
      // Rank
      Holder<Rank> rank = itemStack.get(DataComponents.RANK);
      list.add(
        Component.translatable(LanguageKeys.RANK_TOOLTIP_KEY)
          .withStyle(ChatFormatting.GRAY)
          .append(rank != null ? rank.value().description() : Component.empty())
      );

      // Level
      int level = itemStack.has(DataComponents.LEVEL) ? Objects.requireNonNull(itemStack.get(DataComponents.LEVEL)) : rank != null ? rank.value().level() : 1;
      list.add(
        Component.translatable(LanguageKeys.LEVEL_TOOLTIP_KEY).withStyle(ChatFormatting.GRAY)
          .append(LanguageKeys.getLevelName(level))
      );

      // Color
      int color = itemStack.has(DataComponents.COLOR) ? Objects.requireNonNull(itemStack.get(DataComponents.COLOR)) : rank != null ? rank.value().color() : -1;
      list.add(
        Component.translatable(LanguageKeys.COLOR_TOOLTIP_KEY).withStyle(ChatFormatting.GRAY)
          .append(LanguageKeys.getColorName(color))
      );

      Component prefixName = itemStack.has(DataComponents.PREFIX_NAME) ? Objects.requireNonNull(itemStack.get(DataComponents.PREFIX_NAME)) : rank != null ? rank.value().description() : Component.empty();
      list.add(
        Component.translatable(LanguageKeys.PREFIX_NAME_TOOLTIP_KEY).withStyle(ChatFormatting.GRAY)
          .append(prefixName)
      );

      // Affix
      ItemAffixes itemAffixes = itemStack.get(DataComponents.ITEM_AFFIXES);
      if (itemAffixes != null) {
        list.add(Component.translatable(LanguageKeys.AFFIXES_TOOLTIP_KEY).withStyle(ChatFormatting.GRAY));
        for (Holder<Affix> affix : itemAffixes.affixes()) {
          list.add(CommonComponents.space().append(affix.value().description()));
        }
      }
    }

  }
}
