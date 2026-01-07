package top.theillusivec4.champions.champion.affix.lootcontextbasedvalue;

import com.mojang.serialization.Codec;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;
import top.theillusivec4.champions.registries.BuiltInRegistries;

import java.util.function.Function;

public record FloatLootParamSource<T>(ContextKey<T> key, Function<T, Float> function, float defaultValue) implements LootParamSource<Float> {
  public static final Codec<FloatLootParamSource<?>> CODEC = Codec.lazyInitialized(BuiltInRegistries.LOOT_PARAM_FLOAT_SOURCE::byNameCodec);

  @Override
  public Float provide(LootContext context) {
    T value = context.getOptionalParameter(key);
    if (value != null) {
      return function.apply(value);
    }

    return defaultValue;
  }
}
