package top.theillusivec4.champions.champion.rank;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.util.Util;

public final class Ranks {
	public static final ResourceKey<Rank> COMMON = register("common");
	public static final ResourceKey<Rank> SKILLED = register("skilled");
	public static final ResourceKey<Rank> ELITE = register("elite");
	public static final ResourceKey<Rank> LEGENDARY = register("legendary");
	public static final ResourceKey<Rank> ULTIMATE = register("ultimate");

	private Ranks() {
	}

	public static ResourceKey<Rank> register(String name) {
    return ResourceKey.create(Registries.RANK, Util.id(name));
  }

	public static void bootstrap(BootstrapContext<Rank> context) {
    register(
      context,
      COMMON,
      Rank.builder()
        .setLevel(MinMaxBounds.Ints.exactly(1))
    );
    register(
      context,
      SKILLED,
      Rank.builder()
        .setLevel(MinMaxBounds.Ints.exactly(2))
    );
    register(
      context,
      ELITE,
      Rank.builder()
        .setLevel(MinMaxBounds.Ints.exactly(3))
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
        .setBoss(true)
    );
  }

  private static void register(BootstrapContext<Rank> context, ResourceKey<Rank> key, Rank.Builder builder) {
    context.register(key, builder.build(key.identifier()));
  }
}
