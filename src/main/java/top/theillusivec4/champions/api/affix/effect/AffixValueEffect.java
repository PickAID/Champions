package top.theillusivec4.champions.api.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.world.entity.affix.effects.AffixValueEffects;
import top.theillusivec4.champions.world.entity.affix.effects.AllOf;

import java.util.Arrays;
import java.util.function.Function;

public interface AffixValueEffect {
  Codec<AffixValueEffect> CODEC = Codec.lazyInitialized(() -> ChampionsBuiltInRegistries.AFFIX_VALUE_EFFECT_TYPE.byNameCodec().dispatch(AffixValueEffect::codec, Function.identity()));

  static AffixValueEffect add(LevelBasedValue value) {
    return new AffixValueEffects.AddValue(value);
  }

  static AffixValueEffect allOf(AffixValueEffect... effects) {
    return new AllOf.ValueEffects(Arrays.stream(effects).toList());
  }

  static AffixValueEffect multiply(LevelBasedValue factor) {
    return new AffixValueEffects.MultiplyValue(factor);
  }

  static AffixValueEffect set(LevelBasedValue value) {
    return new AffixValueEffects.SetValue(value);
  }

  static AffixValueEffect removeBinomial(LevelBasedValue chance) {
    return new AffixValueEffects.RemoveBinomial(chance);
  }

  float process(int affixLevel, RandomSource random, float inputValue);

  MapCodec<? extends AffixValueEffect> codec();
}
