package top.theillusivec4.champions.champion;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import top.theillusivec4.champions.registry.BuiltInRegistries;

import java.util.List;
import java.util.function.Function;

public interface DifficultyBasedValue {
  Codec<DifficultyBasedValue> CODEC = Codec.lazyInitialized(() -> {
    Codec<DifficultyBasedValue> dispatchCodec = BuiltInRegistries.DIFFICULTY_BASED_VALUE.byNameCodec().dispatch(DifficultyBasedValue::codec, Function.identity());
    return Codec.either(DifficultyBasedValues.Constant.CODEC, dispatchCodec)
      .xmap(
        either -> either.map(Function.identity(), Function.identity()),
        value -> value instanceof DifficultyBasedValues.Constant constant ? Either.left(constant) : Either.right(value)
      );
  });

  static DifficultyBasedValue constant(float value) {
    return new DifficultyBasedValues.Constant(value);
  }

  static DifficultyBasedValue clamped(DifficultyBasedValue value, float min, float max) {
    return new DifficultyBasedValues.Clamped(value, min, max);
  }

  static DifficultyBasedValue fraction(DifficultyBasedValue numerator, DifficultyBasedValue denominator) {
    return new DifficultyBasedValues.Fraction(numerator, denominator);
  }

  static DifficultyBasedValue difficultySquared(float added) {
    return new DifficultyBasedValues.DifficultySquared(added);
  }

  static DifficultyBasedValue linear(DifficultyBasedValue base, DifficultyBasedValue perDifficultAbovePeaceful) {
    return new DifficultyBasedValues.Linear(base, perDifficultAbovePeaceful);
  }

  static DifficultyBasedValue exponent(DifficultyBasedValue base, DifficultyBasedValue power) {
    return new DifficultyBasedValues.Exponent(base, power);
  }

  static DifficultyBasedValue lookup(List<Float> values, DifficultyBasedValue fallback) {
    return new DifficultyBasedValues.Lookup(values, fallback);
  }

  float calculate(Difficulty difficulty);

  MapCodec<? extends DifficultyBasedValue> codec();
}
