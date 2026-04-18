package top.theillusivec4.champions.api.affix;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.world.entity.affix.LevelBasedValues;

import java.util.List;
import java.util.function.Function;

public interface LevelBasedValue {
  Codec<LevelBasedValue> CODEC = Codec.lazyInitialized(() -> {
    Codec<LevelBasedValue> codec = ChampionsBuiltInRegistries.LEVEL_BASED_VALUE_TYPE.byNameCodec().dispatch(LevelBasedValue::codec, Function.identity());
    return Codec.either(LevelBasedValues.Constant.DIRECT_CODEC, codec)
      .xmap(
        either -> either.map(Function.identity(), Function.identity()),
        value -> value instanceof LevelBasedValues.Constant constant ? Either.left(constant) : Either.right(value)
      )
      ;
  });

  static LevelBasedValue constant(float value) {
    return new LevelBasedValues.Constant(value);
  }

  static LevelBasedValue clamped(LevelBasedValue value, float min, float max) {
    return new LevelBasedValues.Clamped(value, min, max);
  }

  static LevelBasedValue fraction(LevelBasedValue numerator, LevelBasedValue denominator) {
    return new LevelBasedValues.Fraction(numerator, denominator);
  }

  static LevelBasedValue levelsSquared(float added) {
    return new LevelBasedValues.LevelsSquared(added);
  }

  static LevelBasedValue linear(LevelBasedValue base, LevelBasedValue perLevelAboveFirst) {
    return new LevelBasedValues.Linear(base, perLevelAboveFirst);
  }

  static LevelBasedValue lookup(List<Float> values, LevelBasedValue fallback) {
    return new LevelBasedValues.Lookup(values, fallback);
  }

  float calculate(int level);

  MapCodec<? extends LevelBasedValue> codec();
}
