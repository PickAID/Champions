package top.theillusivec4.champions.world.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.util.Util;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ChampionsCreativeModeTabs {
  private static final DeferredRegister<CreativeModeTab> DEFERRED_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Champions.MOD_ID);
  public static final Supplier<CreativeModeTab> CHAMPION_EGG = register("champion_egg", builder -> builder.icon(() -> new ItemStack(Items.HUSK_SPAWN_EGG)));

  private ChampionsCreativeModeTabs() {
  }

  private static Supplier<CreativeModeTab> register(String name, UnaryOperator<CreativeModeTab.Builder> operator) {
    return DEFERRED_REGISTER.register(name, (id) -> operator.apply(
        CreativeModeTab.builder()
          .title(Component.translatable(Util.makeDescriptionId("itemGroup", id))))
      .build()
    );
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
