package top.theillusivec4.champions.server.command.argument;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

@Deprecated
public class ArgumentTypes {
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create(BuiltInRegistries.COMMAND_ARGUMENT_TYPE, Champions.MODID);

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, AffixArgumentInfo> AFFIX_ARGUMENT_TYPE = ARGUMENT_TYPES.register("affixes", () -> ArgumentTypeInfos.registerByClass(AffixArgumentType.class, new AffixArgumentInfo()));

    public static void register(IEventBus bus) {
        ARGUMENT_TYPES.register(bus);
    }
}
