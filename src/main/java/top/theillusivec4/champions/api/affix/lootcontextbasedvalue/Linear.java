package top.theillusivec4.champions.api.affix.lootcontextbasedvalue;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;

public record Linear(LootContextBasedValue base, LootContextBasedValue perLevelAboveFirst) implements LootContextBasedValue {
  public static final MapCodec<Linear> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LootContextBasedValue.CODEC.fieldOf("base").forGetter(Linear::base),
    LootContextBasedValue.CODEC.fieldOf("perLevelAboveFirst").forGetter(Linear::perLevelAboveFirst)
  ).apply(instance, Linear::new));

  @Override
  public float calculate(LootContext context, int level) {
    return base.calculate(context, level) + perLevelAboveFirst().calculate(context, level) * (level - 1);
  }

  @Override
  public MapCodec<? extends LootContextBasedValue> codec() {
    return MAP_CODEC;
  }
}
