package top.theillusivec4.champions.common.registry;

import net.neoforged.bus.api.IEventBus;


public class ChampionsRegistry {

  public static void register(IEventBus bus) {
    ModItems.register(bus);
    ModParticleTypes.register(bus);
    ModMobEffects.register(bus);
    ModEntityTypes.register(bus);
    ModLootModifiers.register(bus);
    ModLootItemConditions.register(bus);
    ModArgumentTypes.register(bus);
    ModStats.register(bus);
    ModAttachments.register(bus);
    ModDataComponents.register(bus);
    ModEntitySubProviders.register(bus);
    AffixTypes.register(bus);
  }

}
