package top.theillusivec4.champions.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.util.Utils;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class AffixDataLoader extends ReloadListener<Map<ResourceLocation, AffixSetting>> {
	public static final JsonParser JSON_PARSER = new JsonParser();
	private static final String FOLDER = "affix_setting";
	private final Map<ResourceLocation, AffixSetting> loadedData = new HashMap<>();

	@Override
	protected Map<ResourceLocation, AffixSetting> prepare(IResourceManager resourceManager, IProfiler profilerFiller) {
		return listResources(resourceManager, profilerFiller);
	}

	@Override
	protected void apply(Map<ResourceLocation, AffixSetting> affixSettingMap, IResourceManager resourceManager, IProfiler profilerFiller) {
		affixSettingMap.putAll(loadedData);
	}

	public Map<ResourceLocation, AffixSetting> listResources(IResourceManager pResourceManager, IProfiler pProfiler) {
		pProfiler.startTick();
		for (ResourceLocation loc : pResourceManager.listResources(FOLDER, p -> p.endsWith(".json"))) {
			try {
				IResource resource = pResourceManager.getResource(loc);
				try (Reader reader = Utils.openAsReader(resource)) {
					JsonElement element = JSON_PARSER.parse(reader);
					AffixSetting.CODEC.parse(JsonOps.INSTANCE, element)
							.resultOrPartial(error -> Champions.LOGGER.debug("Failed to parse affix setting {}", error))
							.ifPresent(itemValues -> loadedData.put(loc, itemValues));
				} catch (Exception e) {
					Champions.LOGGER.error("Failed to load custom data pack: {}", loc, e);
				}
			} catch (Exception e) {
				Champions.LOGGER.error("Failed to load resource: {}", loc, e);
			}

		}
		pProfiler.endTick();
		return loadedData;
	}

	public void cache(Map<ResourceLocation, AffixSetting> affixSettings) {
		loadedData.clear();
		loadedData.putAll(affixSettings);
	}

	public Map<ResourceLocation, AffixSetting> getLoadedData() {
		return loadedData;
	}
}
