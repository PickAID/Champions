package top.theillusivec4.champions.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import top.theillusivec4.champions.Champions;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class AffixDataLoader extends SimplePreparableReloadListener<Map<ResourceLocation, AffixSetting>> {
    private static final String FOLDER = "affix_setting";
    private final Map<ResourceLocation, AffixSetting> loadedData = new HashMap<>();

    @Override
    protected Map<ResourceLocation, AffixSetting> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        return listResources(resourceManager, profilerFiller);
    }

    @Override
    protected void apply(Map<ResourceLocation, AffixSetting> affixSettingMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        affixSettingMap.putAll(loadedData);
    }

    public Map<ResourceLocation, AffixSetting> listResources(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        pProfiler.startTick();
        for (Map.Entry<ResourceLocation, Resource> resource : pResourceManager.listResources(FOLDER, p -> p.getPath().endsWith(".json")).entrySet()) {
            try (Reader reader = resource.getValue().openAsReader()) {
                JsonElement element = JsonParser.parseReader(reader);
                AffixSetting.CODEC.parse(JsonOps.INSTANCE, element)
                        .resultOrPartial(error -> Champions.LOGGER.debug("Failed to parse affix setting {}", error))
                        .ifPresent(itemValues -> loadedData.put(resource.getKey(), itemValues));
            } catch (Exception e) {
                Champions.LOGGER.error("Failed to load custom data pack: {}", resource.getKey(), e);
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
