package top.theillusivec4.champions.world.entity.affix.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.effect.AffixValueEffect;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;

import java.util.function.Supplier;

public final class AffixValueEffects {
  private static final DeferredRegister<MapCodec<? extends AffixValueEffect>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsBuiltInRegistries.AFFIX_VALUE_EFFECT_TYPE, Champions.MOD_ID);
  public static final Supplier<MapCodec<AddValue>> ADD = register("add", () -> AddValue.MAP_CODEC);
  public static final Supplier<MapCodec<AllOf.ValueEffects>> ALL_OF = register("all_of", () -> AllOf.ValueEffects.MAP_CODEC);
  public static final Supplier<MapCodec<MultiplyValue>> MULTIPLY = register("multiply", () -> MultiplyValue.MAP_CODEC);
  public static final Supplier<MapCodec<RemoveBinomial>> REMOVE_BINOMIAL = register("remove_binomial", () -> RemoveBinomial.MAP_CODEC);
  public static final Supplier<MapCodec<SetValue>> SET = register("set", () -> SetValue.MAP_CODEC);

  private AffixValueEffects() {
  }

  private static <T extends AffixValueEffect> Supplier<MapCodec<T>> register(String name, Supplier<MapCodec<T>> supplier) {
    return DEFERRED_REGISTER.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }

  public record AddValue(LevelBasedValue value) implements AffixValueEffect {
    public static final MapCodec<AddValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      LevelBasedValue.CODEC.fieldOf("value").forGetter(AddValue::value)
    ).apply(instance, AddValue::new));

    @Override
    public float process(int affixLevel, RandomSource random, float inputValue) {
      return inputValue + value.calculate(affixLevel);
    }

    @Override
    public MapCodec<? extends AffixValueEffect> codec() {
      return MAP_CODEC;
    }

  }

  public record MultiplyValue(LevelBasedValue factor) implements AffixValueEffect {
    public static final MapCodec<MultiplyValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      LevelBasedValue.CODEC.fieldOf("factor").forGetter(MultiplyValue::factor)
    ).apply(instance, MultiplyValue::new));

    @Override
    public float process(int affixLevel, RandomSource random, float inputValue) {
      return inputValue * factor.calculate(affixLevel);
    }

    @Override
    public MapCodec<? extends AffixValueEffect> codec() {
      return MAP_CODEC;
    }

  }

  public record RemoveBinomial(LevelBasedValue chance) implements AffixValueEffect {
    public static final MapCodec<RemoveBinomial> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      LevelBasedValue.CODEC.fieldOf("chance").forGetter(RemoveBinomial::chance)
    ).apply(instance, RemoveBinomial::new));

    @Override
    public float process(int affixLevel, RandomSource random, float inputValue) {
      float p = this.chance.calculate(affixLevel);
      int drop = 0;
      if (!(inputValue <= 128.0F) && !(inputValue * p < 20.0F) && !(inputValue * (1.0F - p) < 20.0F)) {
        double miu = Math.floor(inputValue * p);
        double sigma = Math.sqrt(inputValue * p * (1.0F - p));
        drop = (int) Math.round(miu + random.nextGaussian() * sigma);
        drop = Math.clamp(drop, 0, (int) inputValue);
      } else {
        for (int y = 0; y < inputValue; y++) {
          if (random.nextFloat() < p) {
            drop++;
          }
        }
      }

      return inputValue - drop;
    }

    @Override
    public MapCodec<? extends AffixValueEffect> codec() {
      return MAP_CODEC;
    }
  }

  public record SetValue(LevelBasedValue value) implements AffixValueEffect {
    public static final MapCodec<SetValue> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      LevelBasedValue.CODEC.fieldOf("value").forGetter(SetValue::value)
    ).apply(instance, SetValue::new));

    @Override
    public float process(int affixLevel, RandomSource random, float inputValue) {
      return value.calculate(affixLevel);
    }

    @Override
    public MapCodec<? extends AffixValueEffect> codec() {
      return MAP_CODEC;
    }

  }
}
