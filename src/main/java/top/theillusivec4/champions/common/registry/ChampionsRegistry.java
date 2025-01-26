package top.theillusivec4.champions.common.registry;

import net.neoforged.bus.api.IEventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ChampionsRegistry {
    private static final List<Consumer<IEventBus>> REGISTRATION_CALLBACKS = new ArrayList<>();

    // Static initializer to populate registration callbacks
    static {
        // Add all registration methods to the callback list
        REGISTRATION_CALLBACKS.addAll(Arrays.asList(
            ModItems::register,
            ModParticleTypes::register,
            ModMobEffects::register,
            ModEntityTypes::register,
            ModLootModifiers::register,
            ModLootItemConditions::register,
            ModArgumentTypes::register,
            ModStats::register,
            ModAttachments::register,
            ModDataComponents::register,
            ModEntitySubProviders::register,
            AffixTypes::register
        ));
    }

    /**
     * Registers all mod components using a flexible callback mechanism
     *
     * @param bus The event bus to use for registration
     */
    public static void register(IEventBus bus) {
        // Iterate through all registered callbacks and execute them
        REGISTRATION_CALLBACKS.forEach(callback -> callback.accept(bus));
    }

    /**
     * Allows dynamic addition of registration callbacks at runtime
     *
     * @param registrationCallback The callback to add for registration
     */
    public static void addRegistrationCallback(Consumer<IEventBus> registrationCallback) {
        REGISTRATION_CALLBACKS.add(registrationCallback);
    }
}
