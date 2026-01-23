package top.theillusivec4.champions.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.rank.Rank;

import java.util.function.Supplier;

public class DataComponents {
  private static final DeferredRegister<DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Champions.MODID);
  public static final Supplier<DataComponentType<CompoundTag>> ENTITY_TAG_COMPONENT = register("entity_tag", DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC));
  // Champion Common
  public static final Supplier<DataComponentType<Affixes>> AFFIXES = register("affixes", DataComponentType.<Affixes>builder().persistent(Affixes.CODEC).networkSynchronized(Affixes.STREAM_CODEC));
  public static final Supplier<DataComponentType<Holder<Rank>>> RANK = register("rank", DataComponentType.<Holder<Rank>>builder().persistent(Rank.REFERENCE_CODEC).networkSynchronized(Rank.STREAM_CODEC));
  public static final Supplier<DataComponentType<Integer>> LEVEL = register("level", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
  public static final Supplier<DataComponentType<Integer>> COLOR = register("color", DataComponentType.<Integer>builder().persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
  public static final Supplier<DataComponentType<Component>> PREFIX = register("prefix_name", DataComponentType.<Component>builder().persistent(ComponentSerialization.CODEC).networkSynchronized(ComponentSerialization.STREAM_CODEC));
  public static final Supplier<DataComponentType<Boolean>> BOSS = register("boss", DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

  public static <T> Supplier<DataComponentType<T>> register(String name, DataComponentType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, builder::build);
  }

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }
}
