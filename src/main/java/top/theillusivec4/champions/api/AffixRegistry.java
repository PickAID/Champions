package top.theillusivec4.champions.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.util.Utils;

import java.util.function.Supplier;

public class AffixRegistry {
    public static final ResourceKey<Registry<IAffix>> AFFIX_REGISTRY_KEY = ResourceKey.createRegistryKey(Utils.getLocation("affix"));
    public static final ResourceLocation EMPTY = Utils.getLocation("empty");

    public static final DeferredRegister<IAffix> AFFIXES = DeferredRegister.create(AFFIX_REGISTRY_KEY, Champions.MODID);
    private static final Supplier<IForgeRegistry<IAffix>> AFFIX_REGISTRY = AFFIXES.makeRegistry(() -> new RegistryBuilder<IAffix>()
            .setName(AFFIX_REGISTRY_KEY.location())
            .setMaxID(2048)
            .setDefaultKey(EMPTY)
            .add((owner, id, key, resourceLocation, obj) -> {
                Champions.LOGGER.info("Affix added to registry: {}", resourceLocation);
            })
    );

    public static IForgeRegistry<IAffix> getRegistry() {
        return AFFIX_REGISTRY.get();
    }
}
