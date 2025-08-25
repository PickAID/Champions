package top.theillusivec4.champions.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.util.Utils;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class AttributesModifierDataLoader extends SimplePreparableReloadListener<Map<ResourceLocation, ModifierSetting>> {
    private static final String FOLDER = "modifier_setting";
    private final Map<ResourceLocation, ModifierSetting> loadedData = new HashMap<>();

    public static String getFolder() {
        return FOLDER;
    }

    @Override
    protected Map<ResourceLocation, ModifierSetting> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        return listResources(resourceManager, profilerFiller);
    }

    @Override
    protected void apply(Map<ResourceLocation, ModifierSetting> attributeModifierMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        attributeModifierMap.putAll(loadedData);
    }

	public Map<ResourceLocation, ModifierSetting> listResources(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
		pProfiler.startTick();
		for (ResourceLocation loc : pResourceManager.listResources(FOLDER, p -> p.endsWith(".json"))) {
			try {
				var resource = pResourceManager.getResource(loc);
				try (Reader reader = Utils.openAsReader(resource)) {
					JsonElement element = JsonParser.parseReader(reader);
					ModifierSetting.MAP_CODEC.codec()
							.parse(JsonOps.INSTANCE, element)
							.resultOrPartial(error -> Champions.LOGGER.debug("Failed to parse Attributes Modifier setting {}", error))
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

    public void cache(Map<ResourceLocation, ModifierSetting> attributeModifierMap) {
        loadedData.clear();
        loadedData.putAll(attributeModifierMap);
    }

    public Map<ResourceLocation, ModifierSetting> getLoadedData() {
        return loadedData;
    }
}
