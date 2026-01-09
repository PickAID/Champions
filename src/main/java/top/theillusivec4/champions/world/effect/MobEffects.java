package top.theillusivec4.champions.world.effect;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

import java.util.function.Supplier;

public class MobEffects {
  private static final DeferredRegister<MobEffect> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Champions.MODID);

  public static DeferredHolder<MobEffect, ParalysisEffect> PARALYSIS_EFFECT_TYPE = register("paralysis", ParalysisEffect::new);
  public static DeferredHolder<MobEffect, WoundEffect> WOUND_EFFECT_TYPE = register("wound", WoundEffect::new);

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T extends MobEffect> DeferredHolder<MobEffect, T> register(String name, Supplier<T> supplier) {
    return DEFERRED_REGISTER.register(name, supplier);
  }
}
