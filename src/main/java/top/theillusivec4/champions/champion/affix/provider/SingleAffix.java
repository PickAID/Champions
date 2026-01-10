package top.theillusivec4.champions.champion.affix.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.affix.Affix;

public record SingleAffix(Holder<Affix> affix) implements AffixProvider {
  public static final Codec<SingleAffix> CODEC = Affix.REFERENCE_CODEC.xmap(SingleAffix::new, SingleAffix::affix);
  public static final MapCodec<SingleAffix> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Affix.REFERENCE_CODEC.fieldOf("affix").forGetter(SingleAffix::affix)
  ).apply(instance, SingleAffix::new));

  @Override
  public void apply(Affixes.Mutable affixes) {
    affixes.add(affix);
  }

  @Override
  public MapCodec<? extends AffixProvider> codec() {
    return MAP_CODEC;
  }
}
