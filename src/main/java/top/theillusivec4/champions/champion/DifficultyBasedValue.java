package top.theillusivec4.champions.champion;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.DifficultyInstance;
import top.theillusivec4.champions.registry.BuiltInRegistries;

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

  float calculate(DifficultyInstance instance);

  MapCodec<? extends DifficultyBasedValue> codec();
}
