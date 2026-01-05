package top.theillusivec4.champions.api.affix.lootcontextbasedvalue;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;

/**
 * 和
 * @param augend 被加数
 * @param addend 加数
 */
public record Summation(LootContextBasedValue augend, LootContextBasedValue addend) implements LootContextBasedValue {
  public static final MapCodec<Summation> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LootContextBasedValue.CODEC.fieldOf("augend").forGetter(Summation::augend),
    LootContextBasedValue.CODEC.fieldOf("addend").forGetter(Summation::addend)
  ).apply(instance, Summation::new));

  @Override
  public float calculate(LootContext context, int level) {
    return augend.calculate(context, level) + addend().calculate(context, level);
  }

  @Override
  public MapCodec<? extends LootContextBasedValue> codec() {
    return MAP_CODEC;
  }
}
