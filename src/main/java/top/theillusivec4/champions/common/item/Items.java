package top.theillusivec4.champions.common.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

public class Items {
  private static final DeferredRegister<Item> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, Champions.MODID);
  public static final DeferredHolder<Item, ChampionEggItem> CHAMPION_EGG_ITEM = DEFERRED_REGISTER.register("champion_egg", ChampionEggItem::new);

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }
}
