package top.theillusivec4.champions.affix.provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.affix.AffixHelper;
import top.theillusivec4.champions.affix.AffixInstance;

import java.util.stream.Stream;

public record AffixesByCostWithDifficulty(HolderSet<Affix> affixes, int minCost, int maxCostSpan) implements AffixProvider {
  public static final MapCodec<AffixesByCostWithDifficulty> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Affix.LIST_CODEC.fieldOf("affixes").forGetter(AffixesByCostWithDifficulty::affixes),
    ExtraCodecs.intRange(1, 10000).fieldOf("min_cost").forGetter(AffixesByCostWithDifficulty::minCost),
    ExtraCodecs.intRange(0, 10000).fieldOf("max_cost_span").forGetter(AffixesByCostWithDifficulty::maxCostSpan)
  ).apply(instance, AffixesByCostWithDifficulty::new));

  @Override
  public Stream<AffixInstance> provide(Entity entity, RandomSource random, DifficultyInstance difficulty) {
    float f = difficulty.getSpecialMultiplier();
    int cost = Mth.randomBetweenInclusive(random, this.minCost, this.minCost + (int) (f * (float) this.maxCostSpan));
    AffixHelper.selectAffixByCost(random, entity, cost, this.affixes.stream());
    return Stream.empty();
  }

  @Override
  public MapCodec<? extends AffixProvider> codec() {
    return null;
  }
}
