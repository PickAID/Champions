package top.theillusivec4.champions.affix.provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.affix.AffixHelper;
import top.theillusivec4.champions.affix.AffixInstance;

import java.util.stream.Stream;

public record AffixesByCost(HolderSet<Affix> affixes, IntProvider cost) implements AffixProvider {
  public static final MapCodec<AffixesByCost> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Affix.LIST_CODEC.fieldOf("affixes").forGetter(AffixesByCost::affixes),
    IntProvider.CODEC.fieldOf("cost").forGetter(AffixesByCost::cost)
  ).apply(instance, AffixesByCost::new));

  @Override
  public Stream<AffixInstance> get(EntityType<?> entity, RandomSource random, DifficultyInstance difficulty) {
    Stream.Builder<AffixInstance> builder = Stream.builder();
    for (AffixInstance instance : AffixHelper.selectAffixByCost(
      random, entity, this.cost.sample(random), this.affixes.stream()
    )) {
      builder.accept(instance);
    }
    return builder.build();
  }

  @Override
  public MapCodec<? extends AffixProvider> codec() {
    return MAP_CODEC;
  }
}
