package top.theillusivec4.champions.server;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ChampionsServerConfig {
  public static final IConfigSpec SPEC;
  public static final ModConfigSpec.DoubleValue DIFFICULTY_THRESHOLD;
  public static final ModConfigSpec.IntValue DEFAULT_AFFIXABLE;

  static {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    DIFFICULTY_THRESHOLD = builder.defineInRange("difficulty_threshold", 3.00, 0.00, 6.75);
    DEFAULT_AFFIXABLE = builder.defineInRange("default_affixable", 20, 0, 1024);
    SPEC = builder.build();
  }

  public ChampionsServerConfig() {
  }
}
