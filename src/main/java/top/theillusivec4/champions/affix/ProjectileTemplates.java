package top.theillusivec4.champions.affix;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.function.Supplier;

public final class ProjectileTemplates {
  private static final DeferredRegister<MapCodec<? extends ProjectileTemplate>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.PROJECTILE_TEMPLATE_TYPE, Champions.MODID);

  private ProjectileTemplates() {
  }

  private static <T extends ProjectileTemplate> Supplier<MapCodec<T>> register(String name, Supplier<MapCodec<T>> supplier) {
    return DEFERRED_REGISTER.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
