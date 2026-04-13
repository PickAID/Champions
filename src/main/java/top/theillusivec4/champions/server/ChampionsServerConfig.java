package top.theillusivec4.champions.server;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ChampionsServerConfig {
  public static final IConfigSpec SPEC;
  public static final ModConfigSpec.DoubleValue CHAMPION_SPAWN_DIFFICULTY_THRESHOLD;
  public static final ModConfigSpec.IntValue DEFAULT_AFFIXABLE;
  public static final ModConfigSpec.DoubleValue AFFIXABLE_FACTOR;
  public static final ModConfigSpec.DoubleValue RANDOM_FACTOR;
  public static final ModConfigSpec.IntValue MIN_AFFIX_COST;
  public static final ModConfigSpec.IntValue MAX_AFFIX_COST;

  static {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    CHAMPION_SPAWN_DIFFICULTY_THRESHOLD = builder.defineInRange("champion_spawn_difficulty_threshold", 3.00, 0.00, 6.75);
    DEFAULT_AFFIXABLE = builder.defineInRange("default_affixable", 20, 0, 1024);
    AFFIXABLE_FACTOR = builder.defineInRange("affixable_factor", 0.25, 0.0, 1.0);
    RANDOM_FACTOR = builder.defineInRange("random_variration_factor", 0.15, 0.0, 1.0);
    MIN_AFFIX_COST = builder.defineInRange("min_affix_cost", 1, 0, 1024);
    MAX_AFFIX_COST = builder.defineInRange("max_affix_cost", 1024, 0, 1024);
    SPEC = builder.build();
  }

  public ChampionsServerConfig() {
  }
}
