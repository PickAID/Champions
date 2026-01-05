package top.theillusivec4.champions.api.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootContextBasedValue;

public record AttributeEffect(
  Identifier id,
  Holder<Attribute> attribute,
  LootContextBasedValue amount,
  AttributeModifier.Operation operation
) implements Validatable {
  public static final MapCodec<AttributeEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Identifier.CODEC.fieldOf("id").forGetter(AttributeEffect::id),
    Attribute.CODEC.fieldOf("attribute").forGetter(AttributeEffect::attribute),
    LootContextBasedValue.CODEC.fieldOf("amount").forGetter(AttributeEffect::amount),
    AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AttributeEffect::operation)
  ).apply(instance, AttributeEffect::new));
  public static final Codec<AttributeEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Identifier.CODEC.fieldOf("id").forGetter(AttributeEffect::id),
    Attribute.CODEC.fieldOf("attribute").forGetter(AttributeEffect::attribute),
    LootContextBasedValue.CODEC.fieldOf("amount").forGetter(AttributeEffect::amount),
    AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AttributeEffect::operation)
  ).apply(instance, AttributeEffect::new));

  public AttributeModifier getModifier(LootContext context, int level) {
    return new AttributeModifier(id, amount.calculate(context, level), operation);
  }

  @Override
  public void validate(ValidationContext context) {
    Validatable.validate(context, "amount", this.amount);
  }
}
