package top.theillusivec4.champions.champion.rank;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.util.Utils;

public interface Ranks {
  ResourceKey<Rank> COMMON = register("common");
  ResourceKey<Rank> SKILLED = register("skilled");
  ResourceKey<Rank> ELITE = register("elite");
  ResourceKey<Rank> LEGENDARY = register("legendary");
  ResourceKey<Rank> ULTIMATE = register("ultimate");

  static ResourceKey<Rank> register(String name) {
    return ResourceKey.create(Registries.RANK, Utils.id(name));
  }

  static void bootstrap(BootstrapContext<Rank> context) {
    register(
      context,
      COMMON,
      Rank.builder()
        .setLevel(MinMaxBounds.Ints.between(1, 2))
    );
    register(
      context,
      SKILLED,
      Rank.builder()
        .setLevel(MinMaxBounds.Ints.between(2, 3))
    );
    register(
      context,
      ELITE,
      Rank.builder()
        .setLevel(MinMaxBounds.Ints.between(3, 4))
    );
    register(
      context,
      LEGENDARY,
      Rank.builder()
        .setLevel(MinMaxBounds.Ints.exactly(4))
        .setWeight(3)
    );
    register(
      context,
      ULTIMATE,
      Rank.builder()
        .setLevel(MinMaxBounds.Ints.exactly(5))
        .setWeight(2)
    );
  }

  private static void register(BootstrapContext<Rank> context, ResourceKey<Rank> key, Rank.Builder builder) {
    context.register(key, builder.build(key.identifier()));
  }
}
