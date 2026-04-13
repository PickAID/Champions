package top.theillusivec4.champions.championmob.property.provider;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.function.Supplier;

public final class ChampionPropertyProviders {
  private static final DeferredRegister<MapCodec<? extends ChampionPropertyProvider>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.CHAMPION_PROPERTY_PROVIDER_TYPE, ChampionsMod.MOD_ID);
  public static final Supplier<MapCodec<ChampionPropertyByRank>> BY_RANK = register("by_rank", () -> ChampionPropertyByRank.MAP_CODEC);
  public static final Supplier<MapCodec<SingleChampionProperty>> SINGLE = register("single", () -> SingleChampionProperty.MAP_CODEC);

  private ChampionPropertyProviders() {
  }

  private static <T extends ChampionPropertyProvider> Supplier<MapCodec<T>> register(String name, Supplier<MapCodec<T>> supplier) {
    return DEFERRED_REGISTER.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }

}
