package top.theillusivec4.champions.api.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootContextBasedValue;

public record AttributeEffect(
  Identifier id,
  Holder<Attribute> attribute,
  LootContextBasedValue amount,
  AttributeModifier.Operation operation
) implements AffixLocationBasedEffect {
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

  @Override
  public void onChangedBlock(LootContext context, int level, Entity entity, Vec3 origin, boolean becameActive) {
    if (entity instanceof LivingEntity livingEntity) {
      AttributeMap attributeMap = livingEntity.getAttributes();
      AttributeInstance attributeInstance = attributeMap.getInstance(this.attribute);
      if (attributeInstance != null) {
        AttributeModifier attributeModifier = this.getModifier(context, level);
        attributeInstance.addTransientModifier(attributeModifier);
      }
    }
  }

  @Override
  public MapCodec<? extends AffixLocationBasedEffect> codec() {
    return MAP_CODEC;
  }

  @Override
  public void onDeactivated(LootContext context, int level, Entity entity, Vec3 origin) {
    if (entity instanceof LivingEntity livingEntity) {
      AttributeMap attributeMap = livingEntity.getAttributes();
      AttributeInstance attributeInstance = attributeMap.getInstance(this.attribute);
      if (attributeInstance != null) {
        attributeInstance.removeModifier(this.id);
      }
    }
  }
}
