package top.theillusivec4.champions.world.entity.affix.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class AllOf {
	private AllOf() {
	}

	public static <T, A extends T> MapCodec<A> codec(Codec<T> codec, Function<List<T>, A> constructor, Function<A, List<T>> accessor) {
		return RecordCodecBuilder.mapCodec(instance -> instance.group(
				codec.listOf().fieldOf("effects").forGetter(accessor)
		).apply(instance, constructor));
	}

	public static AffixEntityEffect entityEffects(AffixEntityEffect... effects) {
		return new AllOf.EntityEffects(Arrays.stream(effects).toList());
	}

	public static AffixValueEffect valueEffects(AffixValueEffect... effects) {
		return new AllOf.ValueEffects(Arrays.stream(effects).toList());
	}

	public record ValueEffects(List<AffixValueEffect> effects) implements AffixValueEffect {
		public static final MapCodec<ValueEffects> MAP_CODEC = AllOf.codec(AffixValueEffect.CODEC, ValueEffects::new, ValueEffects::effects);

		@Override
		public float process(int affixLevel, RandomSource random, float inputValue) {
			for (AffixValueEffect effect : this.effects) {
				inputValue = effect.process(affixLevel, random, inputValue);
			}

			return inputValue;
		}

		@Override
		public MapCodec<? extends AffixValueEffect> codec() {
			return MAP_CODEC;
		}

	}

	public record EntityEffects(List<AffixEntityEffect> effects) implements AffixEntityEffect {
		public static final MapCodec<EntityEffects> MAP_CODEC = AllOf.codec(AffixEntityEffect.CODEC, EntityEffects::new, EntityEffects::effects);

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			for (AffixEntityEffect effect : this.effects) {
				effect.apply(level, affixLevel, source, target, position);
			}
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return MAP_CODEC;
		}

	}
}
