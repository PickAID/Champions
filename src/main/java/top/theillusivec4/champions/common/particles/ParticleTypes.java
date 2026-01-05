package top.theillusivec4.champions.common.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

public class ParticleTypes {
  private static final DeferredRegister<ParticleType<?>> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Champions.MODID);
  public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RANK_PARTICLE_TYPE = DEFERRED_REGISTER.register("rank", () -> new SimpleParticleType(true));

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }
}
