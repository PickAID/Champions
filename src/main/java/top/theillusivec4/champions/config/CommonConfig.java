package top.theillusivec4.champions.config;

import net.minecraft.world.entity.EntitySpawnReason;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonConfig {
  private final ModConfigSpec configSpec;
  private final ModConfigSpec.ConfigValue<List<? extends String>> skipFinalizeSpawnReasons;

  public CommonConfig() {
    ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
    this.skipFinalizeSpawnReasons = builder
      .defineListAllowEmpty("skip_finalize_spawn_reasons", new ArrayList<>(), () -> "NATURAL", object -> Arrays.stream(EntitySpawnReason.values()).anyMatch(entitySpawnReason -> object == entitySpawnReason));

    this.configSpec = builder.build();
  }

  public ModConfigSpec getConfigSpec() {
    return configSpec;
  }
}
