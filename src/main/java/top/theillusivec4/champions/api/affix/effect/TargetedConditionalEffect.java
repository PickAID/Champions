package top.theillusivec4.champions.api.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Optional;

public record TargetedConditionalEffect<T>(AffixTarget enchanted, AffixTarget affected, T effect, Optional<LootItemCondition> requirements) implements Validatable {
  public static <T> Codec<TargetedConditionalEffect<T>> codec(Codec<T> effectCodec) {
    return RecordCodecBuilder.create(instance -> instance.group(
      AffixTarget.CODEC.fieldOf("enchanted").forGetter(TargetedConditionalEffect::enchanted),
      AffixTarget.CODEC.fieldOf("affected").forGetter(TargetedConditionalEffect::affected),
      effectCodec.fieldOf("effect").forGetter(TargetedConditionalEffect::effect),
      LootItemCondition.DIRECT_CODEC.optionalFieldOf("requirements").forGetter(TargetedConditionalEffect::requirements)
    ).apply(instance, TargetedConditionalEffect::new));
  }

  public static <T> TargetedConditionalEffect<T> create(AffixTarget enchanted, AffixTarget affected, T effect) {
    return new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.empty());
  }

  public static <T> TargetedConditionalEffect<T> create(AffixTarget enchanted, AffixTarget affected, T effect, LootItemCondition.Builder builder) {
    return new TargetedConditionalEffect<>(enchanted, affected, effect, Optional.of(builder.build()));
  }

  public static <T> Codec<TargetedConditionalEffect<T>> validatedCodec(Codec<T> effectCodec, ContextKeySet contextKeySet) {
    return codec(effectCodec).validate(Validatable.validatorForContext(contextKeySet));
  }

  public static <T> Codec<List<TargetedConditionalEffect<T>>> validatedListCodec(Codec<T> effectCodec, ContextKeySet contextKeySet) {
    return validatedCodec(effectCodec, contextKeySet).listOf();
  }

  @Override
  public void validate(ValidationContext context) {
    Validatable.validate(context, "requirements", this.requirements);
  }

  public boolean match(LootContext context) {
    return this.requirements.isEmpty() || this.requirements.get().test(context);
  }
}
