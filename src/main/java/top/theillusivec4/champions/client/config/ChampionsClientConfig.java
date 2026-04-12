package top.theillusivec4.champions.client.config;

import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ChampionsClientConfig {
  public static final IConfigSpec SPEC;
  public static final ModConfigSpec.BooleanValue DISPLAY_HEALTH_OVERLAY_ON_TARGET;

  static {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    DISPLAY_HEALTH_OVERLAY_ON_TARGET = builder.define("display_health_overlay_on_target", false);
    SPEC = builder.build();
  }

  private ChampionsClientConfig() {
  }
}
