package top.theillusivec4.champions.affix;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.function.Function;

public interface LevelBasedValue {
  Codec<LevelBasedValue> CODEC = Codec.lazyInitialized(() -> {
    Codec<LevelBasedValue> codec = ChampionsRegistries.LEVEL_BASED_VALUE_TYPE.byNameCodec().dispatch(LevelBasedValue::codec, Function.identity());
    return Codec.either(LevelBasedValues.Constant.DIRECT_CODEC, codec)
      .xmap(
        either -> either.map(Function.identity(), Function.identity()),
        value -> value instanceof LevelBasedValues.Constant constant ? Either.left(constant) : Either.right(value)
      )
      ;
  });

  float calculate(int level);

  MapCodec<? extends LevelBasedValue> codec();
}
