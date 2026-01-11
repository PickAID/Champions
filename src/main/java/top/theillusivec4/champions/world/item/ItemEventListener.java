package top.theillusivec4.champions.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import top.theillusivec4.champions.champion.ChampionUtil;

import java.util.function.Consumer;

public final class ItemEventListener {
  public static void register() {
    NeoForge.EVENT_BUS.register(new ItemEventListener());
  }

  private ItemEventListener() {
  }

  @SubscribeEvent
  public void onItemTooltip(ItemTooltipEvent event) {
    ItemStack itemStack = event.getItemStack();
    Item.TooltipContext tooltipContext = event.getContext();
    TooltipFlag tooltipFlag = event.getFlags();

    Consumer<Component> consumer = component -> event.getToolTip().add(component);
    ChampionUtil.getHandler(itemStack)
      .ifPresent(handlerItem -> handlerItem.addToTooltip(tooltipContext, consumer, tooltipFlag, itemStack.getComponents()));
//    List<Component> list = event.getToolTip();
//    ItemStack itemStack = event.getItemStack();
//    Level level = event.getContext().level();
//    if (level == null) return;
//
//    HolderGetter<Rank> ranks = level.holderLookup(Registries.RANK);
//
//    ChampionUtil.getHandler(itemStack).ifPresent(handler -> {
//      if (handler.isDisplayTooltip()) {
//        // Rank
//        Holder<Rank> rank = handler.getRank().orElse(ranks.getOrThrow(Ranks.EMPTY));
//        list.add(
//          Component.translatable(LanguageKeys.TOOLTIP_RANK_KEY)
//            .withStyle(ChatFormatting.GRAY)
//            .append(rank.value().description())
//        );
//        int lvl = handler.getLevel();
//        int color = handler.getColor();
//        Component lvlMessage = LanguageUtil.getLevelComponent(lvl).withColor(color);
//
//        list.add(
//          Component.translatable(LanguageKeys.TOOLTIP_LEVEL_KEY).withStyle(ChatFormatting.GRAY)
//            .append(lvlMessage)
//        );
//        list.add(
//          Component.translatable(LanguageKeys.TOOLTIP_COLOR_KEY).withStyle(ChatFormatting.GRAY)
//            .append(LanguageUtil.getColorComponent(color))
//        );
//        Component prefixName = handler.getPrefixName()
//          .map(component -> ComponentUtils.mergeStyles(component, component.getStyle().withColor(color)))
//          .orElse(Component.empty());
//        list.add(
//          Component.translatable(LanguageKeys.TOOLTIP_PREFIX_NAME_KEY).withStyle(ChatFormatting.GRAY)
//            .append(prefixName)
//        );
//        list.add(
//          Component.translatable(LanguageKeys.TOOLTIP_BOSS_KEY).withStyle(ChatFormatting.GRAY)
//            .append(handler.isBoss() ? Component.translatable(LanguageKeys.TOOLTIP_IS_BOSS_KEY) : Component.translatable(LanguageKeys.TOOLTIP_NOT_BOSS_KEY))
//        );
//        Affixes affixes = handler.getAffixes();
//        list.add(Component.translatable(LanguageKeys.TOOLTIP_AFFIXES_KEY).withStyle(ChatFormatting.GRAY));
//        for (Holder<Affix> affix : affixes.getAffixes()) {
//          list.add(CommonComponents.space().append(affix.value().description()));
//        }
//      }
//    });

//    if (itemStack.getOrDefault(DataComponents.DISPLAY, false)) {
//      // Rank
//      Holder<Rank> rank = itemStack.get(DataComponents.RANK);
//      list.add(
//        Component.translatable(LanguageKeys.TOOLTIP_RANK_KEY)
//          .withStyle(ChatFormatting.GRAY)
//          .append(rank != null ? rank.value().description() : Component.empty())
//      );
//
//      // Level
//      int level = itemStack.has(DataComponents.LEVEL) ? Objects.requireNonNull(itemStack.get(DataComponents.LEVEL)) : rank != null ? rank.value().level() : 1;
//      list.add(
//        Component.translatable(LanguageKeys.TOOLTIP_LEVEL_KEY).withStyle(ChatFormatting.GRAY)
//          .append(LanguageKeys.getLevelName(level))
//      );
//
//      // Color
//      int color = itemStack.has(DataComponents.COLOR) ? Objects.requireNonNull(itemStack.get(DataComponents.COLOR)) : rank != null ? rank.value().color() : -1;
//      list.add(
//        Component.translatable(LanguageKeys.TOOLTIP_COLOR_KEY).withStyle(ChatFormatting.GRAY)
//          .append(LanguageKeys.getColorName(color))
//      );
//
//      Component prefixName = itemStack.has(DataComponents.PREFIX_NAME) ? Objects.requireNonNull(itemStack.get(DataComponents.PREFIX_NAME)) : rank != null ? rank.value().description() : Component.empty();
//      list.add(
//        Component.translatable(LanguageKeys.TOOLTIP_PREFIX_NAME_KEY).withStyle(ChatFormatting.GRAY)
//          .append(prefixName)
//      );
//
//      // Affix
//      Affixes affixes = itemStack.get(DataComponents.AFFIXES);
//      if (affixes != null) {
//        list.add(Component.translatable(LanguageKeys.TOOLTIP_AFFIXES_KEY).withStyle(ChatFormatting.GRAY));
//        for (Holder<Affix> affix : affixes.getAffixes()) {
//          list.add(CommonComponents.space().append(affix.value().description()));
//        }
//      }
//    }

  }

}
