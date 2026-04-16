package top.theillusivec4.champions.deprecated.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

import java.util.HashMap;
import java.util.Map;

public class ModStats {

  private static final DeferredRegister<ResourceLocation> CHAMPIONS_STATS = DeferredRegister.create(BuiltInRegistries.CUSTOM_STAT, Champions.MOD_ID);
  private static final Map<ResourceLocation, StatFormatter> CUSTOM_STAT_FORMATTERS = new HashMap<>();
  public static final DeferredHolder<ResourceLocation, ResourceLocation> CHAMPION_MOBS_KILLED = makeCustomStat("champion_mobs_killed", StatFormatter.DEFAULT);

  public static void register(IEventBus bus) {
    CHAMPIONS_STATS.register(bus);
  }

  private static DeferredHolder<ResourceLocation, ResourceLocation> makeCustomStat(String key, StatFormatter formatter) {
    ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(CHAMPIONS_STATS.getNamespace(), key);
    var holder = CHAMPIONS_STATS.register(key, () -> resourceLocation);
    CUSTOM_STAT_FORMATTERS.put(resourceLocation, formatter);
    return holder;
  }

  public static void registerFormatter() {
    CUSTOM_STAT_FORMATTERS.forEach(Stats.CUSTOM::get);
  }

}
