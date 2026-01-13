package top.theillusivec4.champions.champion.value.based.lootcontext;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;

/**
 * 指数
 *
 * @param base  底数
 * @param power 指数
 */
public record Exponent(LootContextBasedValue base, LootContextBasedValue power) implements LootContextBasedValue {
  public static final MapCodec<Exponent> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LootContextBasedValue.CODEC.fieldOf("base").forGetter(Exponent::base),
    LootContextBasedValue.CODEC.fieldOf("power").forGetter(Exponent::power)
  ).apply(instance, Exponent::new));

  @Override
  public float calculate(LootContext context, int level) {
    return (float) Math.pow(base.calculate(context, level), power.calculate(context, level));
  }

  @Override
  public MapCodec<? extends LootContextBasedValue> codec() {
    return MAP_CODEC;
  }
}
