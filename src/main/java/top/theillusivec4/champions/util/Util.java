package top.theillusivec4.champions.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.Champions;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public final class Util {
  private Util() {
  }

  public static <K, V> Map<K, V> map(Consumer<ImmutableMap.Builder<K, V>> consumer) {
    ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
    consumer.accept(builder);
    return builder.build();
  }

  public static <T extends WeightedEntry> Optional<Holder<T>> getRandom(RandomSource random, List<? extends Holder<T>> entries) {
    return WeightedRandom.getRandomItem(random, entries.stream().map(HolderWithWeight::new).toList()).map(HolderWithWeight::holder);
  }

  public static ResourceLocation id(String name) {
    return ResourceLocation.fromNamespaceAndPath(Champions.MOD_ID, name);
  }

  public static String makeDescriptionId(String prefix, ResourceLocation id) {
    return makeDescriptionId(prefix, id, null);
  }

  public static String makeDescriptionId(String prefix, ResourceLocation id, @Nullable String suffix) {
    if (suffix != null) {
      return id.toLanguageKey(prefix, suffix);
    } else {
      return id.toLanguageKey(prefix);
    }
  }

  private record HolderWithWeight<T extends WeightedEntry>(Holder<T> holder) implements WeightedEntry {
    @Override
    public Weight getWeight() {
      return this.holder.value().getWeight();
    }
  }
}
