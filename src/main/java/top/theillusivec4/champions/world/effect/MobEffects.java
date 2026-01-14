package top.theillusivec4.champions.world.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

import java.util.function.Supplier;

public class MobEffects {
  private static final DeferredRegister<MobEffect> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Champions.MODID);

  public static Holder<MobEffect> PARALYSIS = register("paralysis", ParalysisEffect::new);
  public static Holder<MobEffect> WOUND = register("wound", WoundEffect::new);
  public static Holder<MobEffect> SHIELD = register("shield", ShieldEffect::new);

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static Holder<MobEffect> register(String name, Supplier<MobEffect> mobEffect) {
    return DEFERRED_REGISTER.register(name, mobEffect);
  }
}
