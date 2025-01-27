package top.theillusivec4.champions.common.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public class ChampionsRegistry {
    public static void register(IEventBus bus) {
        ModItems.register(bus);
        ModParticleTypes.register(bus);
        ModMobEffects.register(bus);
        ModEntityTypes.register(bus);
        ModLootModifiers.register(bus);
        ModArgumentTypes.register(bus);
        AffixTypes.register(bus);
    }
}
