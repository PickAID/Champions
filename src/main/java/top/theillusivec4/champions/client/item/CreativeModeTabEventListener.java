package top.theillusivec4.champions.client.item;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import top.theillusivec4.champions.capability.Capabilities;
import top.theillusivec4.champions.champion.ChampionDefaultConfigs;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.world.item.CreativeModeTabs;

@Deprecated
public final class CreativeModeTabEventListener {
  public static void register(IEventBus modEventBus) {
    modEventBus.register(new CreativeModeTabEventListener());
  }

  private CreativeModeTabEventListener() {
  }

  /**
   * 展示实现了冠军能力接口的实体类型刷怪蛋
   */
  @SubscribeEvent
  public void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
    if (event.getTab() == CreativeModeTabs.CHAMPION_SPAWN_EGGS.get()) {
      Level level = Minecraft.getInstance().level;
      if (level != null) {

//        for (Item item : Capabilities.ChampionHandlers.getImplementedItems()) {
//          ItemStack itemStack = new ItemStack(item);
//          ChampionUtil.getHandler(itemStack)
//            .ifPresent(handler -> handler.setLevel(ChampionDefaultConfigs.MIN_LEVEL));
//
//          event.accept(itemStack);
//        }

      }
    }
  }
}
