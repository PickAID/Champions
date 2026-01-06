package top.theillusivec4.champions.affix.effect.value;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.affix.effect.AllOf;
import top.theillusivec4.champions.affix.lootcontextbasedvalue.LootContextBasedValue;
import top.theillusivec4.champions.deprecated.common.registries.Registries;

import java.util.Arrays;
import java.util.function.Supplier;

public final class AffixValueEffects {
  private static final DeferredRegister<MapCodec<? extends AffixValueEffect>> DEFERRED_REGISTER = DeferredRegister.create(Registries.AFFIX_VALUE_EFFECT_TYPE, Champions.MODID);
  public static final DeferredHolder<MapCodec<? extends AffixValueEffect>, MapCodec<AllOf.ValueEffects>> ALL_OF = register("all_of", AllOf.ValueEffects.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixValueEffect>, MapCodec<AddValue>> ADD = register("add", AddValue.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixValueEffect>, MapCodec<MultiplyValue>> MULTIPLY = register("multiply", MultiplyValue.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixValueEffect>, MapCodec<SetValue>> SET = register("set", SetValue.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixValueEffect>, MapCodec<SubtractValue>> SUBTRACT = register("subtract", SubtractValue.MAP_CODEC);

  public static AffixValueEffect allOf(AffixValueEffect... effects) {
    return new AllOf.ValueEffects(Arrays.stream(effects).toList());
  }

  public static AffixValueEffect add(LootContextBasedValue value) {
    return new AddValue(value);
  }

  public static AffixValueEffect multiply(LootContextBasedValue value) {
    return new MultiplyValue(value);
  }

  public static AffixValueEffect set(LootContextBasedValue value) {
    return new SetValue(value);
  }

  public static AffixValueEffect subtract(LootContextBasedValue value) {
    return new SubtractValue(value);
  }

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T extends AffixValueEffect> DeferredHolder<MapCodec<? extends AffixValueEffect>, MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
    return register(name, () -> mapCodec);
  }

  private static <T extends AffixValueEffect> DeferredHolder<MapCodec<? extends AffixValueEffect>, MapCodec<T>> register(String name, Supplier<MapCodec<T>> mapCodecSupplier) {
    return DEFERRED_REGISTER.register(name, mapCodecSupplier);
  }

  private AffixValueEffects() {
  }

}
