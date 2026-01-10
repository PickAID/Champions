package top.theillusivec4.champions.champion.affix.provider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.registry.BuiltInRegistries;

import java.util.function.Function;

public interface AffixProvider {
  Codec<AffixProvider> CODEC = Codec.lazyInitialized(() -> {
    Codec<AffixProvider> typedCodec = BuiltInRegistries.AFFIX_PROVIDE_TYPE.byNameCodec().dispatch(AffixProvider::codec, Function.identity());
    return Codec.either(SingleAffix.CODEC, typedCodec)
      .xmap(
        either -> either.map(Function.identity(), Function.identity()),
        affixProvider -> affixProvider instanceof SingleAffix singleAffix ? Either.left(singleAffix) : Either.right(affixProvider)
      );
  });

  void apply(Affixes.Mutable affixes);

  MapCodec<? extends AffixProvider> codec();
}
