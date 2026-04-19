package top.theillusivec4.champions.world.entity.affix.effects;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.effect.AffixAttributeEffect;
import top.theillusivec4.champions.api.affix.effect.AffixLocationBasedEffect;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;

import java.util.function.Supplier;

public final class AffixLocationBasedEffects {
  private static final DeferredRegister<MapCodec<? extends AffixLocationBasedEffect>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.AFFIX_LOCATION_BASED_EFFECT_TYPE, Champions.MOD_ID);
  public static final DeferredHolder<MapCodec<? extends AffixLocationBasedEffect>, MapCodec<AffixAttributeEffect>> ATTRIBUTE = register("attribute", AffixAttributeEffect.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixLocationBasedEffect>, MapCodec<AffixEntityEffects.ApplyMobEffect>> APPLY_MOB_EFFECT = register("apply_mob_effect", AffixEntityEffects.ApplyMobEffect.MAP_CODEC);

  public static <T extends AffixLocationBasedEffect> DeferredHolder<MapCodec<? extends AffixLocationBasedEffect>, MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
    return DEFERRED_REGISTER.register(name, () -> mapCodec);
  }

  public static <T extends AffixLocationBasedEffect> DeferredHolder<MapCodec<? extends AffixLocationBasedEffect>, MapCodec<T>> register(String name, Supplier<MapCodec<T>> mapCodecSupplier) {
    return DEFERRED_REGISTER.register(name, mapCodecSupplier);
  }

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private AffixLocationBasedEffects() {
  }
}
