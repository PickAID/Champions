package top.theillusivec4.champions.world.entity.affix.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;
import java.util.function.Consumer;

public record ConditionalEffect<T>(T effect, Optional<LootItemCondition> requirements) {
  public static Codec<LootItemCondition> conditionCodec(LootContextParamSet params) {
    return LootItemCondition.DIRECT_CODEC
      .validate(
        condition -> {
          ProblemReporter.Collector problemreporter$collector = new ProblemReporter.Collector();
          ValidationContext validationcontext = new ValidationContext(problemreporter$collector, params);
          condition.validate(validationcontext);
          return problemreporter$collector.getReport()
            .map(msg -> DataResult.<LootItemCondition>error(() -> "Validation error in affix effect condition: " + msg))
            .orElseGet(() -> DataResult.success(condition));
        }
      );
  }

  public static <T> ConditionalEffect<T> create(T effect) {
    return new ConditionalEffect<>(effect, Optional.empty());
  }

  public static <T> ConditionalEffect<T> create(T effect, LootItemCondition.Builder builder) {
    return new ConditionalEffect<>(effect, Optional.of(builder.build()));
  }

  public static <T> Codec<ConditionalEffect<T>> codec(Codec<T> effectCodec, LootContextParamSet params) {
    return RecordCodecBuilder.create(instance -> instance.group(
      effectCodec.fieldOf("effect").forGetter(ConditionalEffect::effect),
      conditionCodec(params).optionalFieldOf("requirements").forGetter(ConditionalEffect::requirements)
    ).apply(instance, ConditionalEffect::new));
  }

  public void apply(LootContext context, Consumer<T> consumer) {
    if (this.matches(context)) {
      consumer.accept(effect);
    }
  }

  public boolean matches(LootContext context) {
    return this.requirements.isEmpty() || this.requirements.get().test(context);
  }
}
