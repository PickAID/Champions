package top.theillusivec4.champions.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.api.affix.IAffixLifecycle;
import top.theillusivec4.champions.common.affix.core.CombatAffix;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class Utils {
	private static Boolean scalingHealthLoaded = null;
	private static Boolean gameStagesLoaded = null;
	private static Boolean gateways = null;
	private static Boolean oldForge = null;
	private static Boolean kubejs = null;

	@SuppressWarnings("removal")
	public static boolean isOldForge() {
		// 确保只创建一次oldForge对象
		if (oldForge == null) {
			// 尝试使用新版forge api
			// 如果找不到则回退到旧版的oldForge
			try {
				var testUsage = ResourceLocation.fromNamespaceAndPath(Champions.MODID, "for_test_usage");
				Champions.LOGGER.info("Use Newer forge ResourceLocation method {}", testUsage);
				oldForge = true;
				return oldForge;
			} catch (NoClassDefFoundError e) {
				var testUsage = new ResourceLocation(Champions.MODID, "for_test_usage");
				oldForge = false;
				Champions.LOGGER.info("Use Old forge ResourceLocation method {}", testUsage);
			}
		}
		return oldForge;
	}

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

	public static void createServerConfig(ForgeConfigSpec spec, String suffix) {
		String fileName = "champions-" + suffix + ".toml";
		Champions.getInstance().modContext.registerConfig(ModConfig.Type.SERVER, spec, fileName);
		File defaults = FMLPaths.GAMEDIR.get().resolve("defaultconfigs").resolve(fileName).toFile();

		if (!defaults.exists()) {
			try {
				FileUtils.copyInputStreamToFile(Objects.requireNonNull(Champions.class.getClassLoader().getResourceAsStream(fileName)), defaults);
			} catch (IOException e) {
				Champions.LOGGER.error("Error creating default config for {}", fileName);
			}
		}
	}

	@SuppressWarnings("removal")
	public static ResourceLocation getLocation(final String path) {
		return isOldForge() ? new ResourceLocation(Champions.MODID, path) : ResourceLocation.fromNamespaceAndPath(Champions.MODID, path);
	}

	public static Set<ResourceLocation> getLocationSet(final String... path) {
		Set<ResourceLocation> locations = new HashSet<>();
		for (String s : path) {
			locations.add(getLocation(s));
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
