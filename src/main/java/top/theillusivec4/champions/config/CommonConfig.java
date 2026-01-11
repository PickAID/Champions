package top.theillusivec4.champions.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
  private final ModConfigSpec configSpec;
  private final ModConfigSpec.ConfigValue<String> demo;

  public CommonConfig() {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    this.demo = builder
      .comment("平平无奇的配置选项，用于测试")
      .define("demo", "demo");

    this.configSpec = builder.build();
  }

  public ModConfigSpec getConfigSpec() {
    return configSpec;
  }
}
