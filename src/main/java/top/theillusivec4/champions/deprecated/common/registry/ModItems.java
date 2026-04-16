package top.theillusivec4.champions.deprecated.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.common.item.ChampionEggItem;

public class ModItems {

  private static final DeferredRegister<Item> ITEM = DeferredRegister.create(BuiltInRegistries.ITEM, Champions.MOD_ID);
  public static final DeferredHolder<Item, ChampionEggItem> CHAMPION_EGG_ITEM = ITEM.register("champion_egg", ChampionEggItem::new);

  public static void register(IEventBus bus) {
    ITEM.register(bus);
  }
}
