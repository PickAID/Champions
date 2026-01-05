package top.theillusivec4.champions.api.affix.lootcontextbasedvalue;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;

/**
 * 积
 *
 * @param multiplicand 被乘数
 * @param multiplier   乘数
 */
public record Product(LootContextBasedValue multiplicand, LootContextBasedValue multiplier) implements LootContextBasedValue {
  public static final MapCodec<Product> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LootContextBasedValue.CODEC.fieldOf("multiplicand").forGetter(Product::multiplicand),
    LootContextBasedValue.CODEC.fieldOf("multiplier").forGetter(Product::multiplier)
  ).apply(instance, Product::new));

  @Override
  public float calculate(LootContext context, int level) {
    return multiplicand.calculate(context, level) * multiplier.calculate(context, level);
  }

  @Override
  public MapCodec<? extends LootContextBasedValue> codec() {
    return MAP_CODEC;
  }
}
