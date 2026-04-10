package top.theillusivec4.champions.affix.effects;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.function.Supplier;

public final class AffixLocationBasedEffects {
  private static final DeferredRegister<MapCodec<? extends AffixLocationBasedEffect>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.AFFIX_LOCATION_BASED_EFFECT_TYPE, ChampionsMod.MOD_ID);
  public static final Supplier<MapCodec<AffixAttributeEffect>> ATTRIBUTE = register("attribute", () -> AffixAttributeEffect.MAP_CODEC);

  private AffixLocationBasedEffects() {
  }

  private static <T extends AffixLocationBasedEffect> Supplier<MapCodec<T>> register(String name, Supplier<MapCodec<T>> supplier) {
    return DEFERRED_REGISTER.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
