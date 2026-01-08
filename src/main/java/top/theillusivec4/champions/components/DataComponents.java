package top.theillusivec4.champions.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.rank.Rank;

public class DataComponents {
  private static final DeferredRegister<DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Champions.MODID);
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> ENTITY_TAG_COMPONENT = register("entity_tag", DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> DISPLAY = register("display", DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Affixes>> AFFIXES = register("affixes", DataComponentType.<Affixes>builder().persistent(Affixes.CODEC).networkSynchronized(Affixes.STREAM_CODEC));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Holder<Rank>>> RANK = register("rank", DataComponentType.<Holder<Rank>>builder().persistent(Rank.REFERENCE_CODEC).networkSynchronized(Rank.STREAM_CODEC));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> LEVEL = register("level", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> COLOR = register("color", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Component>> PREFIX_NAME = register("prefix_name", DataComponentType.<Component>builder().persistent(ComponentSerialization.CODEC).networkSynchronized(ComponentSerialization.STREAM_CODEC));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<Component>> CUSTOM_NAME = register("custom_name", DataComponentType.<Component>builder().persistent(ComponentSerialization.CODEC).networkSynchronized(ComponentSerialization.STREAM_CODEC));

  public static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, DataComponentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }
}
