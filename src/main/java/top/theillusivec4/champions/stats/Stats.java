package top.theillusivec4.champions.stats;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.Champions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Stats {
  private static final DeferredRegister<Identifier> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.CUSTOM_STAT, Champions.MODID);
  private static @Nullable Map<Identifier, StatFormatter> customs = new HashMap<>();
  public static final DeferredHolder<Identifier, Identifier> CHAMPION_MOBS_KILLED = register("champion_mobs_killed", StatFormatter.DEFAULT);

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
    modEventBus.addListener(FMLCommonSetupEvent.class, event -> {
      event.enqueueWork(() -> {
        Objects.requireNonNull(customs).forEach(net.minecraft.stats.Stats.CUSTOM::get);
        customs = null;
      });
    });
  }

  private static DeferredHolder<Identifier, Identifier> register(String name, StatFormatter statFormatter) {
    return DEFERRED_REGISTER.register(name, identifier -> {
      customs.put(identifier, statFormatter);
      return identifier;
    });
  }

  private Stats() {
  }
}
