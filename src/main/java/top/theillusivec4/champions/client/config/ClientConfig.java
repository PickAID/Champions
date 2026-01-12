package top.theillusivec4.champions.client.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import top.theillusivec4.champions.data.lang.LanguageKeys;

public class ClientConfig {
  private final ModConfigSpec configSpec;
  private final ModConfigSpec.ConfigValue<Boolean> displayOverlay;

  public ClientConfig() {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    this.displayOverlay = builder
      .translation(LanguageKeys.CONFIG_DISPLAY_CHAMPION_OVERLAY_KEY)
      .define("display_champion_overlay", true);
    this.configSpec = builder.build();
  }

  public boolean displayHealthOverlay() {
    return this.displayOverlay.get();
  }

  public ModConfigSpec getConfigSpec() {
    return configSpec;
  }
}
