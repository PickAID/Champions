package top.theillusivec4.champions.deprecated.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.deprecated.common.potion.ParalysisEffect;
import top.theillusivec4.champions.deprecated.common.potion.WoundEffect;

public class ModMobEffects {

  private static final DeferredRegister<MobEffect> MOB_EFFECT = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ChampionsMod.MOD_ID);

  public static DeferredHolder<MobEffect, ParalysisEffect> PARALYSIS_EFFECT_TYPE;
  public static DeferredHolder<MobEffect, WoundEffect> WOUND_EFFECT_TYPE;
  public static void register(IEventBus bus) {
    PARALYSIS_EFFECT_TYPE = MOB_EFFECT.register("paralysis", ParalysisEffect::new);
    WOUND_EFFECT_TYPE = MOB_EFFECT.register("wound", WoundEffect::new);
    MOB_EFFECT.register(bus);
  }
}
