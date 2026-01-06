package top.theillusivec4.champions.affix.lootcontextbasedvalue;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;

/**
 * 分数
 * @param numerator 分子
 * @param denominator 分母
 */
public record Fraction(LootContextBasedValue numerator, LootContextBasedValue denominator) implements LootContextBasedValue {
  public static final MapCodec<Fraction> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LootContextBasedValue.CODEC.fieldOf("numerator").forGetter(Fraction::numerator),
    LootContextBasedValue.CODEC.fieldOf("denominator").forGetter(Fraction::denominator)
  ).apply(instance, Fraction::new));

  @Override
  public float calculate(LootContext context, int level) {
    float denominator = this.denominator.calculate(context, level);
    return denominator == 0.0F ? 0.0F : this.numerator.calculate(context, level) / denominator;
  }

  @Override
  public MapCodec<? extends LootContextBasedValue> codec() {
    return MAP_CODEC;
  }
}
