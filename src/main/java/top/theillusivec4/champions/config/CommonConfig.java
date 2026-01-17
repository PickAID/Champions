package top.theillusivec4.champions.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
  private final ModConfigSpec configSpec;
  private final ModConfigSpec.ConfigValue<Double> maxDamageProtection;

  private static boolean validDamageProtection(Object damageProtection) {
    return damageProtection instanceof Double d && d >= 0.0;
  }

  public CommonConfig() {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    this.maxDamageProtection = builder.define("max_damage_protection", 0.8, CommonConfig::validDamageProtection);

    this.configSpec = builder.build();
  }

  public float getMaxDamageProtection() {
    return maxDamageProtection.get().floatValue();
  }

  public ModConfigSpec getConfigSpec() {
    return configSpec;
  }
}
