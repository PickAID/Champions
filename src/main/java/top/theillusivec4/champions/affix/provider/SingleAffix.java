package top.theillusivec4.champions.affix.provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.affix.AffixInstance;

import java.util.stream.Stream;

public record SingleAffix(Holder<Affix> affix, IntProvider level) implements AffixProvider {
  public static final MapCodec<SingleAffix> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Affix.REFERENCE_CODEC.fieldOf("affix").forGetter(SingleAffix::affix),
    IntProvider.CODEC.fieldOf("level").forGetter(SingleAffix::level)
  ).apply(instance, SingleAffix::new));

  @Override
  public Stream<AffixInstance> provide(Entity entity, RandomSource random, DifficultyInstance difficulty) {
    return Stream.of(new AffixInstance(this.affix, this.level.sample(random)));
  }

  @Override
  public MapCodec<? extends AffixProvider> codec() {
    return MAP_CODEC;
  }
}
