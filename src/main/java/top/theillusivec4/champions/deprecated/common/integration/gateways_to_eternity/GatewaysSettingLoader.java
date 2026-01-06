//package top.theillusivec4.champions.deprecated.common.integration.gateways_to_eternity;
//
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import com.mojang.serialization.JsonOps;
//import net.minecraft.resources.Identifier;
//import net.minecraft.server.packs.resources.Resource;
//import net.minecraft.server.packs.resources.ResourceManager;
//import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
//import net.minecraft.util.profiling.ProfilerFiller;
//import top.theillusivec4.champions.Champions;
//
//import java.io.Reader;
//import java.util.HashMap;
//import java.util.Map;
//
//public class GatewaysSettingLoader extends SimplePreparableReloadListener<Map<Identifier, GatewaysSetting>> {
//    private static final String FOLDER = "gateway_setting";
//    private final Map<Identifier, GatewaysSetting> loadedData = new HashMap<>();
//
//    @Override
//    protected Map<Identifier, GatewaysSetting> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
//        return listResources(resourceManager, profilerFiller);
//    }
//
//    @Override
//    protected void apply(Map<Identifier, GatewaysSetting> affixSettingMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
//        affixSettingMap.putAll(loadedData);
//    }
//
//    public Map<Identifier, GatewaysSetting> listResources(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
//        pProfiler.startTick();
//        for (Map.Entry<Identifier, Resource> resource : pResourceManager.listResources(FOLDER, p -> p.getPath().endsWith(".json")).entrySet()) {
//            try (Reader reader = resource.getValue().openAsReader()) {
//                JsonElement element = JsonParser.parseReader(reader);
//                GatewaysSetting.CODEC.parse(JsonOps.INSTANCE, element)
//                        .resultOrPartial(error -> Champions.LOGGER.debug("Failed to parse gateways setting {}", error))
//                        .ifPresent(itemValues -> loadedData.put(resource.getKey(), itemValues));
//            } catch (Exception e) {
//                Champions.LOGGER.error("Failed to load custom data pack: {}", resource.getKey(), e);
//            }
//        }
//        pProfiler.endTick();
//        return loadedData;
//    }
//
//    public void cache(Map<Identifier, GatewaysSetting> affixSettings) {
//        loadedData.clear();
//        loadedData.putAll(affixSettings);
//    }
//
//    public Map<Identifier, GatewaysSetting> getLoadedData() {
//        return loadedData;
//    }
//}
