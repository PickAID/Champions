package top.theillusivec4.champions.server.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ServerConfig {
  private final ModConfigSpec configSpec;

  public ServerConfig() {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

    this.configSpec = builder.build();
  }

  public ModConfigSpec getConfigSpec() {
    return configSpec;
  }
}
