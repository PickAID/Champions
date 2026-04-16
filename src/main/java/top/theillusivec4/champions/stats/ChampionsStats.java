package top.theillusivec4.champions.stats;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

import java.util.function.Supplier;

public final class ChampionsStats {
  private static final DeferredRegister<ResourceLocation> DEFERRED_REGISTER = DeferredRegister.create(Registries.CUSTOM_STAT, Champions.MOD_ID);

  private ChampionsStats() {
  }

  private static Supplier<ResourceLocation> register(String name, StatFormatter formatter) {
    return DEFERRED_REGISTER.register(name, id -> {
      Stats.CUSTOM.get(id, formatter);
      return id;
    });
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
