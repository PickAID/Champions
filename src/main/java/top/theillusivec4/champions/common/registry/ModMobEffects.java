package top.theillusivec4.champions.common.registry;

import net.minecraft.potion.Effect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.potion.ParalysisEffect;
import top.theillusivec4.champions.common.potion.WoundEffect;

public class ModMobEffects {

    private static final DeferredRegister<Effect> MOB_EFFECT = DeferredRegister.create(ForgeRegistries.POTIONS, Champions.MODID);
    public static final RegistryObject<ParalysisEffect> PARALYSIS_EFFECT_TYPE = MOB_EFFECT.register("paralysis", ParalysisEffect::new);
    public static final RegistryObject<WoundEffect> WOUND_EFFECT_TYPE = MOB_EFFECT.register("wound", WoundEffect::new);

    public static void register(IEventBus bus) {
        MOB_EFFECT.register(bus);
    }
}
