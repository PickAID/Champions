package top.theillusivec4.champions.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import top.theillusivec4.champions.Champions;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class AffixDataLoader extends SimpleJsonResourceReloadListener<AffixSetting> {
  private static final String FOLDER = "affix_setting";
  private final Map<Identifier, AffixSetting> loadedData = new HashMap<>();

  public AffixDataLoader() {
    super(AffixSetting.CODEC, FileToIdConverter.json(FOLDER));
  }

  @Override
  protected Map<Identifier, AffixSetting> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    return listResources(resourceManager, profilerFiller);
  }

  @Override
  protected void apply(Map<Identifier, AffixSetting> affixSettingMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    affixSettingMap.putAll(loadedData);
  }

  public Map<Identifier, AffixSetting> listResources(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    pProfiler.startTick();
    for (Map.Entry<Identifier, Resource> resource : pResourceManager.listResources(FOLDER, p -> p.getPath().endsWith(".json")).entrySet()) {
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

  public void cache(Map<Identifier, AffixSetting> affixSettings) {
    loadedData.clear();
    loadedData.putAll(affixSettings);
  }

  public Map<Identifier, AffixSetting> getLoadedData() {
    return loadedData;
  }
}
