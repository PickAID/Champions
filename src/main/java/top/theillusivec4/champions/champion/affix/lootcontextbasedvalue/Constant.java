package top.theillusivec4.champions.champion.affix.lootcontextbasedvalue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;

/**
 * 常数
 * @param value 值
 */
public record Constant(float value) implements LootContextBasedValue {
  public static final Codec<Constant> CODEC = Codec.FLOAT.xmap(Constant::new, Constant::value);
  public static final MapCodec<Constant> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.FLOAT.fieldOf("value").forGetter(Constant::value)
  ).apply(instance, Constant::new));

  @Override
  public float calculate(LootContext context, int level) {
    return value;
  }

  @Override
  public MapCodec<? extends LootContextBasedValue> codec() {
    return MAP_CODEC;
  }
}
