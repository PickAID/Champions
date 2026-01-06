package top.theillusivec4.champions.components;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

public class DataComponents {
  private static final DeferredRegister<DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Champions.MODID);

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> ENTITY_TAG_COMPONENT = register("entity_tag", DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemAffixes>> ITEM_AFFIXES = register("item_affixes", DataComponentType.<ItemAffixes>builder().persistent(ItemAffixes.CODEC).networkSynchronized(ItemAffixes.STREAM_CODEC));

  public static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, DataComponentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }
}
