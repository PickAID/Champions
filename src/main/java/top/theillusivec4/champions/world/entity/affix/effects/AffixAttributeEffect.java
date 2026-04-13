package top.theillusivec4.champions.world.entity.affix.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.world.entity.affix.LevelBasedValue;

public record AffixAttributeEffect(
  ResourceLocation id,
  Holder<Attribute> attribute,
  LevelBasedValue amount,
  AttributeModifier.Operation operation
) implements AffixLocationBasedEffect {
  public static final MapCodec<AffixAttributeEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    ResourceLocation.CODEC.fieldOf("id").forGetter(AffixAttributeEffect::id),
    Attribute.CODEC.fieldOf("attribute").forGetter(AffixAttributeEffect::attribute),
    LevelBasedValue.CODEC.fieldOf("amount").forGetter(AffixAttributeEffect::amount),
    AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AffixAttributeEffect::operation)
  ).apply(instance, AffixAttributeEffect::new));
  public static final Codec<AffixAttributeEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ResourceLocation.CODEC.fieldOf("id").forGetter(AffixAttributeEffect::id),
    Attribute.CODEC.fieldOf("attribute").forGetter(AffixAttributeEffect::attribute),
    LevelBasedValue.CODEC.fieldOf("amount").forGetter(AffixAttributeEffect::amount),
    AttributeModifier.Operation.CODEC.fieldOf("operation").forGetter(AffixAttributeEffect::operation)
  ).apply(instance, AffixAttributeEffect::new));

  public AttributeModifier getModifier(int level) {
    return new AttributeModifier(id, amount.calculate(level), operation);
  }

  @Override
  public void onChangedBlock(ServerLevel level, int affixLevel, Entity entity, Vec3 origin, boolean becameActive) {
    if (entity instanceof LivingEntity livingEntity) {
      AttributeMap attributeMap = livingEntity.getAttributes();
      AttributeInstance attributeInstance = attributeMap.getInstance(this.attribute);
      if (attributeInstance != null) {
        AttributeModifier attributeModifier = this.getModifier(affixLevel);
        attributeInstance.addTransientModifier(attributeModifier);
      }
    }
  }

  @Override
  public MapCodec<? extends AffixLocationBasedEffect> codec() {
    return MAP_CODEC;
  }

  @Override
  public void onDeactivated(ServerLevel level, int affixLevel, Entity entity, Vec3 origin) {
    if (entity instanceof LivingEntity livingEntity) {
      AttributeMap attributeMap = livingEntity.getAttributes();
      AttributeInstance attributeInstance = attributeMap.getInstance(this.attribute);
      if (attributeInstance != null) {
        attributeInstance.removeModifier(this.id);
      }
    }
  }
}
