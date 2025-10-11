package top.theillusivec4.champions.common.integration.gateways_to_eternity;

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

public class GatewaysSettingLoader extends ReloadListener<Map<ResourceLocation, GatewaysSetting>> {
    private static final String FOLDER = "gateway_setting";
    private final Map<ResourceLocation, GatewaysSetting> loadedData = new HashMap<>();

    @Override
    protected Map<ResourceLocation, GatewaysSetting> prepare(IResourceManager resourceManager, IProfiler profilerFiller) {
        return listResources(resourceManager, profilerFiller);
    }

    @Override
    protected void apply(Map<ResourceLocation, GatewaysSetting> affixSettingMap, IResourceManager resourceManager, IProfiler profilerFiller) {
        affixSettingMap.putAll(loadedData);
    }

    public Map<ResourceLocation, GatewaysSetting> listResources(IResourceManager pResourceManager, IProfiler pProfiler) {
        pProfiler.startTick();
	    for (ResourceLocation loc : pResourceManager.listResources(FOLDER, p -> p.endsWith(".json"))) {
		    try {
			    IResource resource = pResourceManager.getResource(loc);

	            try (Reader reader = Utils.openAsReader(resource)) {
	                JsonElement element = new JsonParser().parse(reader);
	                GatewaysSetting.CODEC.parse(JsonOps.INSTANCE, element)
	                        .resultOrPartial(error -> Champions.LOGGER.debug("Failed to parse gateways setting {}", error))
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

    public void cache(Map<ResourceLocation, GatewaysSetting> affixSettings) {
        loadedData.clear();
        loadedData.putAll(affixSettings);
    }

    public Map<ResourceLocation, GatewaysSetting> getLoadedData() {
        return loadedData;
    }
}
