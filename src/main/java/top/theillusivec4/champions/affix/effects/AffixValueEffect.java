package top.theillusivec4.champions.affix.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import top.theillusivec4.champions.affix.LevelBasedValue;
import top.theillusivec4.champions.registries.ChampionsBuiltInRegistries;

import java.util.Arrays;
import java.util.function.Function;

public interface AffixValueEffect {
  Codec<AffixValueEffect> CODEC = Codec.lazyInitialized(() -> ChampionsBuiltInRegistries.AFFIX_VALUE_EFFECT_TYPE.byNameCodec().dispatch(AffixValueEffect::codec, Function.identity()));

  static AffixValueEffect allOf(AffixValueEffect... effects) {
    return new AllOf.ValueEffects(Arrays.stream(effects).toList());
  }

  static AffixValueEffect add(LevelBasedValue value) {
    return new AffixValueEffects.AddValue(value);
  }

  static AffixValueEffect multiply(LevelBasedValue value) {
    return new AffixValueEffects.MultiplyValue(value);
  }

  static AffixValueEffect set(LevelBasedValue value) {
    return new AffixValueEffects.SetValue(value);
  }

  static AffixValueEffect subtract(LevelBasedValue value) {
    return new AffixValueEffects.SubtractValue(value);
  }

  static AffixValueEffect removeBinomial(LevelBasedValue chance) {
    return new AffixValueEffects.RemoveBinomial(chance);
  }

  static AffixValueEffect exponential(LevelBasedValue base, LevelBasedValue exponent) {
    return new AffixValueEffects.ScaleExponentially(base, exponent);
  }

  float process(int affixLevel, RandomSource random, float inputValue);

  MapCodec<? extends AffixValueEffect> codec();
}
