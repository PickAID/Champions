package top.theillusivec4.champions.world.entity.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;

import java.util.List;
import java.util.function.Supplier;

public final class LevelBasedValues {
  private static final DeferredRegister<MapCodec<? extends LevelBasedValue>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.LEVEL_BASED_VALUE_TYPE, Champions.MOD_ID);
  public static final Supplier<MapCodec<Constant>> CONSTANT = register("constant", () -> Constant.MAP_CODEC);
  public static final Supplier<MapCodec<Clamped>> CLAMPED = register("clamped", () -> Clamped.MAP_CODEC);
  public static final Supplier<MapCodec<Fraction>> FRACTION = register("fraction", () -> Fraction.MAP_CODEC);
  public static final Supplier<MapCodec<LevelsSquared>> LEVELS_SQUARED = register("levels_squared", () -> LevelsSquared.MAP_CODEC);
  public static final Supplier<MapCodec<Linear>> LINEAR = register("linear", () -> Linear.MAP_CODEC);
  public static final Supplier<MapCodec<Lookup>> LOOKUP = register("lookup", () -> Lookup.MAP_CODEC);

  private LevelBasedValues() {
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }

  private static <T extends LevelBasedValue> Supplier<MapCodec<T>> register(String name, Supplier<MapCodec<T>> supplier) {
    return DEFERRED_REGISTER.register(name, supplier);
  }

  public record Constant(float value) implements LevelBasedValue {
    public static final Codec<Constant> DIRECT_CODEC = Codec.FLOAT.xmap(Constant::new, Constant::value);
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

  public record Clamped(LevelBasedValue value, float min, float max) implements LevelBasedValue {
    public static final MapCodec<Clamped> MAP_CODEC = RecordCodecBuilder.<Clamped>mapCodec(
      instance -> instance.group(
        LevelBasedValue.CODEC.fieldOf("value").forGetter(Clamped::value),
        Codec.FLOAT.fieldOf("min").forGetter(Clamped::min),
        Codec.FLOAT.fieldOf("max").forGetter(Clamped::max)
      ).apply(instance, Clamped::new)).validate(
      clamped -> clamped.max <= clamped.min
        ? DataResult.error(() -> "Max must be larger than min, min: " + clamped.min + ", max: " + clamped.max)
        : DataResult.success(clamped)
    );

    @Override
    public float calculate(int level) {
      return Math.clamp(this.value.calculate(level), this.min, this.max);
    }

    @Override
    public MapCodec<? extends LevelBasedValue> codec() {
      return MAP_CODEC;
    }
  }

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

  public record LevelsSquared(float added) implements LevelBasedValue {
    public static final MapCodec<LevelsSquared> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(Codec.FLOAT.fieldOf("added").forGetter(LevelsSquared::added))
        .apply(instance, LevelsSquared::new)
    );

    @Override
    public float calculate(int level) {
      return (float) Mth.square(level) + this.added;
    }

    @Override
    public MapCodec<LevelsSquared> codec() {
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

  public record Lookup(List<Float> values, LevelBasedValue fallback) implements LevelBasedValue {
    public static final MapCodec<Lookup> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
        Codec.FLOAT.listOf().fieldOf("values").forGetter(Lookup::values),
        LevelBasedValue.CODEC.fieldOf("fallback").forGetter(Lookup::fallback)
      ).apply(instance, Lookup::new));

    @Override
    public float calculate(int level) {
      return level <= this.values.size() ? this.values.get(level - 1) : this.fallback.calculate(level);
    }

    @Override
    public MapCodec<Lookup> codec() {
      return MAP_CODEC;
    }
  }
}
