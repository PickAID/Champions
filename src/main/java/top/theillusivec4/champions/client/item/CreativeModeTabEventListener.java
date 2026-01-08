package top.theillusivec4.champions.client.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import top.theillusivec4.champions.capabilities.Capabilities;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.components.DataComponents;
import top.theillusivec4.champions.data.lang.LanguageKeys;
import top.theillusivec4.champions.items.CreativeModeTabs;
import top.theillusivec4.champions.registries.Registries;
import top.theillusivec4.champions.tags.RankTags;

public final class CreativeModeTabEventListener {
  public static void register(IEventBus modEventBus) {
    modEventBus.register(new CreativeModeTabEventListener());
  }

  private CreativeModeTabEventListener() {
  }

  @SubscribeEvent
  public void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
    if (event.getTab() == CreativeModeTabs.CHAMPION_SPAWN_EGGS.get()) {
      Level level = Minecraft.getInstance().level;
      if (level != null) {
        Registry<Rank> ranks = level.registryAccess().lookupOrThrow(Registries.RANK);

        HolderSet.Named<Rank> holders = ranks.getOrThrow(RankTags.ORDER);
        Holder<Rank> rank = holders.get(ranks.size() - 1);
        for (Item item : Capabilities.ChampionHandlers.getImplementedItems()) {
          ItemStack itemStack = new ItemStack(item);
          ChampionUtil.getHandler(itemStack, level).ifPresent(handler -> {
            itemStack.set(
              DataComponents.CUSTOM_NAME,
              Component.translatable(LanguageKeys.PREFIX_NAME_ITEM_CHAMPION_SPAWN_EGG)
                .append(item.getName())
            );
            handler.setDisplay(true);
            handler.setRank(rank);
          });

          event.accept(itemStack);
        }
//        for (ChampionSpawnEgg championSpawnEgg : level.registryAccess().lookupOrThrow(Registries.CHAMPION_SPAWN_EGG)) {
//          ItemStack itemStack = championSpawnEgg.getSpawnEgg(level);
//          event.accept(itemStack);
//        }
      }
    }
  }
}
