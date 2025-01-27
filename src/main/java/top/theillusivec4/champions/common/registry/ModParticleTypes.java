package top.theillusivec4.champions.common.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.champions.Champions;

public class ModParticleTypes {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Champions.MODID);
    public static final RegistryObject<SimpleParticleType> RANK_PARTICLE_TYPE = PARTICLE_TYPE.register("rank", () -> new SimpleParticleType(true));

    public static void register(IEventBus bus) {
        PARTICLE_TYPE.register(bus);
    }
}
