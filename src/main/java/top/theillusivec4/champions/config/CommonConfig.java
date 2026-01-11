package top.theillusivec4.champions.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
  private final ModConfigSpec configSpec;

  public CommonConfig() {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    this.configSpec = builder.build();
  }

  public ModConfigSpec getConfigSpec() {
    return configSpec;
  }
}
