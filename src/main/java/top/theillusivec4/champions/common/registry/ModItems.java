package top.theillusivec4.champions.common.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.item.ChampionEggItem;

public class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Champions.MODID);
    public static final RegistryObject<ChampionEggItem> CHAMPION_EGG_ITEM = ITEMS.register("champion_egg", ChampionEggItem::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
