package top.theillusivec4.champions.world.entity.affix.provider;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;

import java.util.function.Supplier;

public final class AffixProviders {
  private static final DeferredRegister<MapCodec<? extends AffixProvider>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.AFFIX_PROVIDER_TYPE, ChampionsMod.MOD_ID);
  public static final Supplier<MapCodec<SingleAffix>> SINGLE = register("single", () -> SingleAffix.MAP_CODEC);
  public static final Supplier<MapCodec<AffixesByCost>> BY_COST = register("by_cost", () -> AffixesByCost.MAP_CODEC);
  public static final Supplier<MapCodec<AffixesByCostWithDifficulty>> BY_COST_DIFFICULTY = register("by_cost_difficulty", () -> AffixesByCostWithDifficulty.MAP_CODEC);

  private AffixProviders() {
  }

  private static <T extends AffixProvider> Supplier<MapCodec<T>> register(String name, Supplier<MapCodec<T>> supplier) {
    return DEFERRED_REGISTER.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
