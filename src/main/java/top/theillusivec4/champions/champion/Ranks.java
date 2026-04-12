package top.theillusivec4.champions.champion;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class Ranks {
  public static final ResourceKey<Rank> COMMON = register("common");
  public static final ResourceKey<Rank> SKILLED = register("skilled");
  public static final ResourceKey<Rank> ELITE = register("elite");
  public static final ResourceKey<Rank> LEGENDARY = register("legendary");
  public static final ResourceKey<Rank> ULTIMATE = register("ultimate");

  private Ranks() {
  }

  private static ResourceKey<Rank> register(String name) {
    return ResourceKey.create(ChampionsRegistries.RANK, ChampionsUtil.id(name));
  }

  public static void bootstrap(BootstrapContext<Rank> context) {
    register(
      context,
      COMMON,
      Rank.builder()
        .tier(1)
        .color(TextColor.fromRgb(0xffffff))
    );
    register(
      context,
      SKILLED,
      Rank.builder()
        .tier(2)
        .color(TextColor.fromRgb(0xffff00))
    );
    register(
      context,
      ELITE,
      Rank.builder()
        .tier(3)
        .color(TextColor.fromRgb(0xff9900))
    );
    register(
      context,
      LEGENDARY,
      Rank.builder()
        .tier(4)
        .weight(3)
        .color(TextColor.fromRgb(0x66ffff))
    );
    register(
      context,
      ULTIMATE,
      Rank.builder()
        .tier(5)
        .weight(2)
        .boss(true)
        .color(TextColor.fromRgb(0xcc33ff))
    );
  }

  private static void register(BootstrapContext<Rank> context, ResourceKey<Rank> key, Rank.Builder builder) {
    context.register(
      key,
      builder.build(key.location())
    );
  }
}
