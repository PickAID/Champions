package top.theillusivec4.champions.util;

import net.minecraft.resources.Identifier;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.io.FileUtils;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.deprecated.api.affix.IAffix;
import top.theillusivec4.champions.deprecated.api.affix.IAffixLifecycle;
import top.theillusivec4.champions.deprecated.common.affix.core.CombatAffix;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Utils {
  private static Boolean scalingHealthLoaded = null;
  private static Boolean gameStagesLoaded = null;
  private static Boolean gateways = null;
  private static Boolean kubejs = null;

  public static boolean isGameStagesLoaded() {
    if (gameStagesLoaded == null) {
      gameStagesLoaded = ModList.get().isLoaded("gamestages");
    }
    return gameStagesLoaded;
  }

  public static boolean isGatewaysLoaded() {
    if (gateways == null) {
      gateways = ModList.get().isLoaded("gateways");
    }
    return gateways;
  }


  public static boolean isScalingHealthLoaded() {
    if (scalingHealthLoaded == null) {
      scalingHealthLoaded = ModList.get().isLoaded("scalinghealth");
    }
    return scalingHealthLoaded;
  }

  public static boolean isKubejsLoaded() {
    if (kubejs == null) {
      kubejs = ModList.get().isLoaded("kubejs");
    }
    return kubejs;
  }

  public static void createServerConfig(ModConfigSpec spec, String suffix) {
//    String fileName = "champions-" + suffix + ".toml";
//    Champions.getInstance().getModContainer().registerConfig(ModConfig.Type.SERVER, spec, fileName);
//    File defaults = FMLPaths.GAMEDIR.get().resolve("defaultconfigs").resolve(fileName).toFile();
//
//    if (!defaults.exists()) {
//      try {
//        FileUtils.copyInputStreamToFile(Objects.requireNonNull(Champions.class.getClassLoader().getResourceAsStream(fileName)), defaults);
//      } catch (IOException e) {
//        Champions.LOGGER.error("Error creating default config for {}", fileName);
//      }
//    }
  }

  public static Identifier id(final String path) {
    return Identifier.fromNamespaceAndPath(Champions.MODID, path);
  }

  public static Set<Identifier> getLocationSet(final String... path) {
    Set<Identifier> locations = new HashSet<>();
    for (String s : path) {
      locations.add(id(s));
    }
    return locations;
  }

  public static void consumeIfLifeCycle(List<IAffix> affixes, Consumer<IAffixLifecycle> consumer) {
    affixes.forEach(iAffix -> {
      if (iAffix instanceof IAffixLifecycle lifecycle) {
        consumer.accept(lifecycle);
      }
    });
  }

  public static void consumeIfCombat(List<IAffix> affixes, Consumer<CombatAffix> consumer) {
    affixes.forEach(iAffix -> {
      if (iAffix instanceof CombatAffix lifecycle) {
        consumer.accept(lifecycle);
      }
    });
  }

}
