package top.theillusivec4.champions.affix.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.function.Function;

public interface AffixValueEffect {
  Codec<AffixValueEffect> CODEC = Codec.lazyInitialized(() -> ChampionsRegistries.AFFIX_VALUE_EFFECT_TYPE.byNameCodec().dispatch(AffixValueEffect::codec, Function.identity()));

  float process(int affixLevel, RandomSource random, float inputValue);

  MapCodec<? extends AffixValueEffect> codec();
}
