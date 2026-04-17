package top.theillusivec4.champions.world.entity.affix.providers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import top.theillusivec4.champions.api.affix.provider.AffixProvider;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.AffixInstance;

import java.util.stream.Stream;

public record SingleAffix(Holder<Affix> affix, IntProvider level) implements AffixProvider {
  public static final MapCodec<SingleAffix> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Affix.REFERENCE_CODEC.fieldOf("affix").forGetter(SingleAffix::affix),
    IntProvider.CODEC.fieldOf("level").forGetter(SingleAffix::level)
  ).apply(instance, SingleAffix::new));

  @Override
  public Stream<AffixInstance> get(EntityType<?> entity, RandomSource random, DifficultyInstance difficulty) {
    return Stream.of(new AffixInstance(
        this.affix,
        Math.clamp(
          this.level.sample(random),
          this.affix.value().getMinLevel(),
          this.affix.value().getMaxLevel()
        )
      )
    );
  }

  @Override
  public MapCodec<? extends AffixProvider> codec() {
    return MAP_CODEC;
  }
}
