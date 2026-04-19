package top.theillusivec4.champions.api.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;
import java.util.function.Consumer;

public record ConditionalEffect<T>(T effect, Optional<LootItemCondition> requirements) implements Validatable {

	public static <T> Codec<ConditionalEffect<T>> codec(Codec<T> effectCodec, ContextKeySet set) {
		return RecordCodecBuilder.<ConditionalEffect<T>>create(instance -> instance.group(
				effectCodec.fieldOf("effect").forGetter(ConditionalEffect::effect),
				LootItemCondition.DIRECT_CODEC.optionalFieldOf("requirements").forGetter(ConditionalEffect::requirements)
		).apply(instance, ConditionalEffect::new)).validate(Validatable.validatorForContext(set));
	}

	public static <T> ConditionalEffect<T> create(T effect) {
		return new ConditionalEffect<>(effect, Optional.empty());
	}

	public static <T> ConditionalEffect<T> create(T effect, LootItemCondition.Builder builder) {
		return new ConditionalEffect<>(effect, Optional.of(builder.build()));
	}

	public boolean matches(LootContext context) {
		return this.requirements.isEmpty() || this.requirements.get().test(context);
	}

	public void apply(LootContext context, Consumer<T> consumer) {
		if (this.matches(context)) {
			consumer.accept(effect);
		}
	}

	@Override
	public void validate(ValidationContext context) {
		Validatable.validate(context, "requirements", this.requirements);
	}
}
