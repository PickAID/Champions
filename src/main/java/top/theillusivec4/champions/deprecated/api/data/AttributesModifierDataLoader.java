package top.theillusivec4.champions.deprecated.api.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import top.theillusivec4.champions.Champions;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class AttributesModifierDataLoader extends SimplePreparableReloadListener<Map<Identifier, ModifierSetting>> {
  private static final String FOLDER = "modifier_setting";
  private final Map<Identifier, ModifierSetting> loadedData = new HashMap<>();

  public static String getFolder() {
    return FOLDER;
  }

  @Override
  protected Map<Identifier, ModifierSetting> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    return listResources(resourceManager, profilerFiller);
  }

  @Override
  protected void apply(Map<Identifier, ModifierSetting> attributeModifierMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
    attributeModifierMap.putAll(loadedData);
  }

  public Map<Identifier, ModifierSetting> listResources(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    pProfiler.startTick();
    for (Map.Entry<Identifier, Resource> resource : pResourceManager.listResources(FOLDER, p -> p.getPath().endsWith(".json")).entrySet()) {
      try (Reader reader = resource.getValue().openAsReader()) {
        JsonElement element = JsonParser.parseReader(reader);
        ModifierSetting.MAP_CODEC.codec().parse(JsonOps.INSTANCE, element)
          .resultOrPartial(error -> Champions.LOGGER.debug("Failed to parse Attributes Modifier setting {}", error))
          .ifPresent(itemValues -> loadedData.put(resource.getKey(), itemValues));
      } catch (Exception e) {
        Champions.LOGGER.error("Failed to load custom data pack: {}", resource.getKey(), e);
      }
    }
    pProfiler.endTick();
    return loadedData;
  }

  public void cache(Map<Identifier, ModifierSetting> attributeModifierMap) {
    loadedData.clear();
    loadedData.putAll(attributeModifierMap);
  }

  public Map<Identifier, ModifierSetting> getLoadedData() {
    return loadedData;
  }
}
