package top.theillusivec4.champions.items;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.data.lang.LanguageKeys;

public final class CreativeModeTabs {
  private static final RandomSource RANDOM = RandomSource.create();
  private static final DeferredRegister<CreativeModeTab> DEFERRED_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Champions.MODID);
  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CHAMPION_SPAWN_EGGS = register(
    "champion_spawn_eggs",
    CreativeModeTab.builder()
      .title(Component.translatable(LanguageKeys.ITEM_GROUP_CHAMPION_SPAWN_EGGS))
      .icon(() -> new ItemStack(Items.ZOMBIE_SPAWN_EGG))
  );

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static DeferredHolder<CreativeModeTab, CreativeModeTab> register(String name, CreativeModeTab.Builder builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  private CreativeModeTabs() {
  }
}
