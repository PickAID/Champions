package top.theillusivec4.champions.api.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Affixable(int value) {
  public static final MapCodec<Affixable> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.INT.fieldOf("value").forGetter(Affixable::value)
  ).apply(instance, Affixable::new));
  public static final Codec<Affixable> CODEC = Codec.lazyInitialized(() -> {
    Codec<Affixable> directCodec = Codec.INT.xmap(Affixable::new, Affixable::value);
    return Codec.withAlternative(directCodec, MAP_CODEC.codec());
  });
}
