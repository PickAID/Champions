package top.theillusivec4.champions.champion.affix.effect.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import top.theillusivec4.champions.champion.affix.lootcontextbasedvalue.LootContextBasedValue;

public record AddValue(LootContextBasedValue value) implements AffixValueEffect {
  public static final MapCodec<AddValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LootContextBasedValue.CODEC.fieldOf("value").forGetter(AddValue::value)
  ).apply(instance, AddValue::new));

  @Override
  public float process(LootContext context, int level, float inputValue) {
    return inputValue + value.calculate(context, level);
  }

  @Override
  public MapCodec<? extends AffixValueEffect> codec() {
    return MAP_CODEC;
  }

  @Override
  public void validate(ValidationContext context) {
    Validatable.validate(context, "value", this.value);
  }

}
