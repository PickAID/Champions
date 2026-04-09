package top.theillusivec4.champions.deprecated.common.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

public class ModParticleTypes {

  private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Champions.MODID);
  public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RANK_PARTICLE_TYPE = PARTICLE_TYPE.register("rank", () -> new SimpleParticleType(true));

  public static void register(IEventBus bus) {
    PARTICLE_TYPE.register(bus);
  }
}
