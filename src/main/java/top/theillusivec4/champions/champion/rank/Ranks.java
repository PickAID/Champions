package top.theillusivec4.champions.champion.rank;

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
        .setLevel(1)
        .setColor("#FFFFFF")
    );
    register(
      context,
      SKILLED,
      Rank.builder()
        .setLevel(2)
        .setColor("#FFFF00")
    );
    register(
      context,
      ELITE,
      Rank.builder()
        .setLevel(3)
        .setColor("#FF9900")
    );
    register(
      context,
      LEGENDARY,
      Rank.builder()
        .setLevel(4)
        .setColor("#66FFFF")
    );
    register(
      context,
      ULTIMATE,
      Rank.builder()
        .setLevel(5)
        .setColor("#CC33FF")
    );
  }

  private static void register(BootstrapContext<Rank> context, ResourceKey<Rank> key, Rank.Builder builder) {
    context.register(key, builder.build(key.identifier()));
  }
}
