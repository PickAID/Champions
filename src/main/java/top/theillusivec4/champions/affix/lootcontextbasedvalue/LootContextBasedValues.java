package top.theillusivec4.champions.affix.lootcontextbasedvalue;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.common.registries.Registries;

import java.util.function.Supplier;

public final class LootContextBasedValues {
  private static final DeferredRegister<MapCodec<? extends LootContextBasedValue>> DEFERRED_REGISTER = DeferredRegister.create(Registries.LOOT_CONTEXT_BASED_VALUE_TYPE, Champions.MODID);
  public static final DeferredHolder<MapCodec<? extends LootContextBasedValue>, MapCodec<Constant>> CONSTANT = register("constant", Constant.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends LootContextBasedValue>, MapCodec<Exponent>> EXPONENT = register("exponent", Exponent.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends LootContextBasedValue>, MapCodec<Fraction>> FRACTION = register("fraction", Fraction.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends LootContextBasedValue>, MapCodec<Summation>> SUMMATION = register("summation", Summation.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends LootContextBasedValue>, MapCodec<Product>> PRODUCT = register("product", Product.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends LootContextBasedValue>, MapCodec<LootParam>> LOOT_PARAM = register("loot_param", LootParam.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends LootContextBasedValue>, MapCodec<Linear>> LINEAR = register("linear", Linear.MAP_CODEC);

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  public static LootContextBasedValue constant(float value) {
    return new Constant(value);
  }

  public static LootContextBasedValue exponent(LootContextBasedValue base, LootContextBasedValue power) {
    return new Exponent(base, power);
  }

  public static LootContextBasedValue fraction(LootContextBasedValue numerator, LootContextBasedValue denominator) {
    return new Fraction(numerator, denominator);
  }

  public static LootContextBasedValue summation(LootContextBasedValue base, LootContextBasedValue addend) {
    return new Summation(base, addend);
  }

  public static LootContextBasedValue product(LootContextBasedValue multiplicand, LootContextBasedValue multiplier) {
    return new Product(multiplicand, multiplier);
  }

  public static <T> LootContextBasedValue lootParam(Supplier<FloatLootParamSource<T>> sourceSupplier) {
    return lootParam(sourceSupplier.get());
  }

  public static <T> LootContextBasedValue lootParam(FloatLootParamSource<T> source) {
    return new LootParam(source);
  }

  public static Linear linear(LootContextBasedValue base, LootContextBasedValue perLevelAboveFirst) {
    return new Linear(base, perLevelAboveFirst);
  }

  private static <T extends LootContextBasedValue> DeferredHolder<MapCodec<? extends LootContextBasedValue>, MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
    return register(name, () -> mapCodec);
  }

  private static <T extends LootContextBasedValue> DeferredHolder<MapCodec<? extends LootContextBasedValue>, MapCodec<T>> register(String name, Supplier<MapCodec<T>> mapCodecSupplier) {
    return DEFERRED_REGISTER.register(name, mapCodecSupplier);
  }

  private LootContextBasedValues() {
  }

}
