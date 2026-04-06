package top.theillusivec4.champions.champion.value.based.lootcontext;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import top.theillusivec4.champions.registries.ChampionsBuiltInRegistries;

import java.util.function.Function;

public interface LevelBasedValue {
  Codec<LevelBasedValue> CODEC = Codec.lazyInitialized(() -> {
    Codec<LevelBasedValue> dispatchCodec = ChampionsBuiltInRegistries.LOOT_CONTEXT_BASED_VALUE_TYPE.byNameCodec().dispatch(LevelBasedValue::codec, Function.identity());
    return Codec.either(LevelBasedValues.Constant.CODEC, dispatchCodec).xmap(
      either -> either.map(Function.identity(), Function.identity()),
      value -> value instanceof LevelBasedValues.Constant constant ? Either.left(constant) : Either.right(value)
    );
  });

  static LevelBasedValue constant(float value) {
    return new LevelBasedValues.Constant(value);
  }

  static LevelBasedValue exponent(LevelBasedValue base, LevelBasedValue power) {
    return new LevelBasedValues.Exponent(base, power);
  }

  static LevelBasedValue fraction(LevelBasedValue numerator, LevelBasedValue denominator) {
    return new LevelBasedValues.Fraction(numerator, denominator);
  }

  static LevelBasedValue summation(LevelBasedValue base, LevelBasedValue addend) {
    return new LevelBasedValues.Summation(base, addend);
  }

  static LevelBasedValue product(LevelBasedValue multiplicand, LevelBasedValue multiplier) {
    return new LevelBasedValues.Product(multiplicand, multiplier);
  }

  static LevelBasedValues.Linear linear(LevelBasedValue base, LevelBasedValue perLevelAboveFirst) {
    return new LevelBasedValues.Linear(base, perLevelAboveFirst);
  }

  float calculate(int level);

  MapCodec<? extends LevelBasedValue> codec();
}
