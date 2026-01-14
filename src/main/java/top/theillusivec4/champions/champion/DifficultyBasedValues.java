package top.theillusivec4.champions.champion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.registry.Registries;

import java.util.List;
import java.util.function.Supplier;

public final class DifficultyBasedValues {
  private static final DeferredRegister<MapCodec<? extends DifficultyBasedValue>> DEFERRED_REGISTER = DeferredRegister.create(Registries.DIFFICULTY_BASED_VALUE, Champions.MODID);
  public static final Supplier<MapCodec<Constant>> CONSTANT = register("constant", Constant.MAP_CODEC);
  public static final Supplier<MapCodec<Clamped>> CLAMPED = register("clamped", Clamped.MAP_CODEC);
  public static final Supplier<MapCodec<Fraction>> FRACTION = register("fraction", Fraction.MAP_CODEC);
  public static final Supplier<MapCodec<DifficultySquared>> DIFFICULTY_SQUARED = register("difficulty_squared", DifficultySquared.MAP_CODEC);
  public static final Supplier<MapCodec<Linear>> LINEAR = register("linear", Linear.MAP_CODEC);
  public static final Supplier<MapCodec<Exponent>> EXPONENT = register("exponent", Exponent.MAP_CODEC);
  public static final Supplier<MapCodec<Lookup>> LOOKUP = register("lookup", Lookup.MAP_CODEC);

  public static void register(IEventBus modBusEvent) {
    DEFERRED_REGISTER.register(modBusEvent);
  }

  private static <T extends DifficultyBasedValue> Supplier<MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
    return DEFERRED_REGISTER.register(name, () -> mapCodec);
  }

  private DifficultyBasedValues() {
  }

  public record Constant(float value) implements DifficultyBasedValue {
    public static final Codec<Constant> CODEC = Codec.FLOAT.xmap(Constant::new, Constant::value);
    public static final MapCodec<Constant> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.FLOAT.fieldOf("value").forGetter(Constant::value)
    ).apply(instance, Constant::new));

    @Override
    public float calculate(DifficultyInstance instance) {
      return this.value;
    }

    @Override
    public MapCodec<? extends DifficultyBasedValue> codec() {
      return MAP_CODEC;
    }
  }

  public record Clamped(DifficultyBasedValue value, float min, float max) implements DifficultyBasedValue {
    public static final MapCodec<Clamped> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      DifficultyBasedValue.CODEC.fieldOf("value").forGetter(Clamped::value),
      Codec.FLOAT.fieldOf("min").forGetter(Clamped::min),
      Codec.FLOAT.fieldOf("max").forGetter(Clamped::max)
    ).apply(instance, Clamped::new));

    @Override
    public float calculate(DifficultyInstance instance) {
      return Mth.clamp(this.value.calculate(instance), this.min, this.max);
    }

    @Override
    public MapCodec<? extends DifficultyBasedValue> codec() {
      return MAP_CODEC;
    }
  }

  public record Fraction(DifficultyBasedValue numerator, DifficultyBasedValue denominator) implements DifficultyBasedValue {
    public static final MapCodec<Fraction> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      DifficultyBasedValue.CODEC.fieldOf("numerator").forGetter(Fraction::numerator),
      DifficultyBasedValue.CODEC.fieldOf("denominator").forGetter(Fraction::denominator)
    ).apply(instance, Fraction::new));

    @Override
    public float calculate(DifficultyInstance instance) {
      float denominator = this.denominator.calculate(instance);
      return denominator == 0.0F ? 0.0F : this.numerator.calculate(instance) / denominator;
    }

    @Override
    public MapCodec<? extends DifficultyBasedValue> codec() {
      return MAP_CODEC;
    }
  }

  public record DifficultySquared(float added) implements DifficultyBasedValue {
    public static final MapCodec<DifficultySquared> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.FLOAT.fieldOf("added").forGetter(DifficultySquared::added)
    ).apply(instance, DifficultySquared::new));

    @Override
    public float calculate(DifficultyInstance instance) {
      return Mth.sqrt(instance.getDifficulty().getId()) + this.added;
    }

    @Override
    public MapCodec<? extends DifficultyBasedValue> codec() {
      return MAP_CODEC;
    }
  }

  public record Linear(float base, float perEffectiveDifficultyAboveEasy) implements DifficultyBasedValue {
    public static final MapCodec<Linear> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.FLOAT.fieldOf("base").forGetter(Linear::base),
      Codec.FLOAT.fieldOf("per_effective_difficulty_above_easy").forGetter(Linear::perEffectiveDifficultyAboveEasy)
    ).apply(instance, Linear::new));

    @Override
    public float calculate(DifficultyInstance instance) {
      return this.base + this.perEffectiveDifficultyAboveEasy * Math.max(instance.getEffectiveDifficulty() - 1.5f, 0.0f);
    }

    @Override
    public MapCodec<? extends DifficultyBasedValue> codec() {
      return MAP_CODEC;
    }
  }

  public record Exponent(DifficultyBasedValue base, DifficultyBasedValue power) implements DifficultyBasedValue {
    public static final MapCodec<Exponent> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      DifficultyBasedValue.CODEC.fieldOf("base").forGetter(Exponent::base),
      DifficultyBasedValue.CODEC.fieldOf("power").forGetter(Exponent::power)
    ).apply(instance, Exponent::new));

    @Override
    public float calculate(DifficultyInstance instance) {
      return (float) Math.pow(this.base.calculate(instance), this.power.calculate(instance));
    }

    @Override
    public MapCodec<? extends DifficultyBasedValue> codec() {
      return MAP_CODEC;
    }
  }

  public record Lookup(List<Float> values, DifficultyBasedValue fallback) implements DifficultyBasedValue {
    public static final MapCodec<Lookup> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.FLOAT.listOf().fieldOf("values").forGetter(Lookup::values),
      DifficultyBasedValue.CODEC.fieldOf("fallback").forGetter(Lookup::fallback)
    ).apply(instance, Lookup::new));

    @Override
    public float calculate(DifficultyInstance instance) {
      int id = instance.getDifficulty().getId();
      return id <= this.values.size() ? this.values.get(id - 1) : this.fallback.calculate(instance);
    }

    @Override
    public MapCodec<? extends DifficultyBasedValue> codec() {
      return MAP_CODEC;
    }
  }
}
