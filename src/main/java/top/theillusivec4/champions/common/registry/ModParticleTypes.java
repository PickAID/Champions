package top.theillusivec4.champions.common.registry;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.Champions;
public class ModParticleTypes {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Champions.MODID);
    public static final RegistryObject<BasicParticleType> RANK_PARTICLE_TYPE = PARTICLE_TYPE.register("rank", () -> new BasicParticleType(true));

    public static void register(IEventBus bus) {
        PARTICLE_TYPE.register(bus);
    }
}
