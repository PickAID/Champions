package top.theillusivec4.champions.world.entity.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;

import java.util.function.Supplier;

public final class LevelBasedValues {
	private static final DeferredRegister<MapCodec<? extends LevelBasedValue>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.LEVEL_BASED_VALUE_TYPE, ChampionsMod.MOD_ID);
	public static final Supplier<MapCodec<Constant>> CONSTANT = register("constant", Constant.MAP_CODEC);
	public static final Supplier<MapCodec<Exponent>> EXPONENT = register("exponent", Exponent.MAP_CODEC);
	public static final Supplier<MapCodec<Fraction>> FRACTION = register("fraction", Fraction.MAP_CODEC);
	public static final Supplier<MapCodec<Summation>> SUMMATION = register("summation", Summation.MAP_CODEC);
	public static final Supplier<MapCodec<Product>> PRODUCT = register("product", Product.MAP_CODEC);
	public static final Supplier<MapCodec<Linear>> LINEAR = register("linear", Linear.MAP_CODEC);

	private LevelBasedValues() {
	}

	public static void register(IEventBus modEventBus) {
		DEFERRED_REGISTER.register(modEventBus);
	}

	private static <T extends LevelBasedValue> Supplier<MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
		return register(name, () -> mapCodec);
	}

	private static <T extends LevelBasedValue> DeferredHolder<MapCodec<? extends LevelBasedValue>, MapCodec<T>> register(String name, Supplier<MapCodec<T>> mapCodecSupplier) {
		return DEFERRED_REGISTER.register(name, mapCodecSupplier);
	}

	/**
	 * 常数
	 *
	 * @param value 值
	 */
	public record Constant(float value) implements LevelBasedValue {
		public static final Codec<Constant> CODEC = Codec.FLOAT.xmap(Constant::new, Constant::value);
		public static final MapCodec<Constant> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.FLOAT.fieldOf("value").forGetter(Constant::value)
		).apply(instance, Constant::new));

		@Override
		public float calculate(int level) {
			return value;
		}

		@Override
		public MapCodec<? extends LevelBasedValue> codec() {
			return MAP_CODEC;
		}
	}

	/**
	 * 指数
	 *
	 * @param base  底数
	 * @param power 指数
	 */
	public record Exponent(LevelBasedValue base, LevelBasedValue power) implements LevelBasedValue {
		public static final MapCodec<Exponent> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				LevelBasedValue.CODEC.fieldOf("base").forGetter(Exponent::base),
				LevelBasedValue.CODEC.fieldOf("power").forGetter(Exponent::power)
		).apply(instance, Exponent::new));

		@Override
		public float calculate(int level) {
			return (float) Math.pow(base.calculate(level), power.calculate(level));
		}

		@Override
		public MapCodec<? extends LevelBasedValue> codec() {
			return MAP_CODEC;
		}
	}

	/**
	 * 分数
	 *
	 * @param numerator   分子
	 * @param denominator 分母
	 */
	public record Fraction(LevelBasedValue numerator, LevelBasedValue denominator) implements LevelBasedValue {
		public static final MapCodec<Fraction> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				LevelBasedValue.CODEC.fieldOf("numerator").forGetter(Fraction::numerator),
				LevelBasedValue.CODEC.fieldOf("denominator").forGetter(Fraction::denominator)
		).apply(instance, Fraction::new));

		@Override
		public float calculate(int level) {
			float denominator = this.denominator.calculate(level);
			return denominator == 0.0F ? 0.0F : this.numerator.calculate(level) / denominator;
		}

		@Override
		public MapCodec<? extends LevelBasedValue> codec() {
			return MAP_CODEC;
		}
	}

	/**
	 * 和
	 *
	 * @param augend 被加数
	 * @param addend 加数
	 */
	public record Summation(LevelBasedValue augend, LevelBasedValue addend) implements LevelBasedValue {
		public static final MapCodec<Summation> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				LevelBasedValue.CODEC.fieldOf("augend").forGetter(Summation::augend),
				LevelBasedValue.CODEC.fieldOf("addend").forGetter(Summation::addend)
		).apply(instance, Summation::new));

		@Override
		public float calculate(int level) {
			return augend.calculate(level) + addend().calculate(level);
		}

		@Override
		public MapCodec<? extends LevelBasedValue> codec() {
			return MAP_CODEC;
		}
	}

	/**
	 * 积
	 *
	 * @param multiplicand 被乘数
	 * @param multiplier   乘数
	 */
	public record Product(LevelBasedValue multiplicand, LevelBasedValue multiplier) implements LevelBasedValue {
		public static final MapCodec<Product> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				LevelBasedValue.CODEC.fieldOf("multiplicand").forGetter(Product::multiplicand),
				LevelBasedValue.CODEC.fieldOf("multiplier").forGetter(Product::multiplier)
		).apply(instance, Product::new));

		@Override
		public float calculate(int level) {
			return multiplicand.calculate(level) * multiplier.calculate(level);
		}

		@Override
		public MapCodec<? extends LevelBasedValue> codec() {
			return MAP_CODEC;
		}
	}

	public record Linear(LevelBasedValue base, LevelBasedValue perLevelAboveFirst) implements LevelBasedValue {
		public static final MapCodec<Linear> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				LevelBasedValue.CODEC.fieldOf("base").forGetter(Linear::base),
				LevelBasedValue.CODEC.fieldOf("per_level_above_first").forGetter(Linear::perLevelAboveFirst)
		).apply(instance, Linear::new));

		@Override
		public float calculate(int level) {
			return base.calculate(level) + perLevelAboveFirst().calculate(level) * (level - 1);
		}

		@Override
		public MapCodec<? extends LevelBasedValue> codec() {
			return MAP_CODEC;
		}
	}
}
