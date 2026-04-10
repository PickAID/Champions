package top.theillusivec4.champions.client.config;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ChampionsClientConfig {
  public static final IConfigSpec SPEC;
  public static final ModConfigSpec.BooleanValue DISPLAY_HEALTH_OVERLAY;

  static {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    DISPLAY_HEALTH_OVERLAY = builder.define("display_health_overlay", false);
    SPEC = builder.build();
  }

  private ChampionsClientConfig() {
  }
}
