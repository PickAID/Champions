package top.theillusivec4.champions.world.entity.champion.property.provider;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;

import java.util.function.Supplier;

public final class ChampionPropertyProviders {
  private static final DeferredRegister<MapCodec<? extends ChampionMobPropertyProvider>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.CHAMPION_MOB_PROPERTY_PROVIDER_TYPE, Champions.MOD_ID);
  public static final Supplier<MapCodec<ChampionMobPropertyByRank>> BY_RANK = register("by_rank", () -> ChampionMobPropertyByRank.MAP_CODEC);
  public static final Supplier<MapCodec<SingleChampionMobProperty>> SINGLE = register("single", () -> SingleChampionMobProperty.MAP_CODEC);

  private ChampionPropertyProviders() {
  }

  private static <T extends ChampionMobPropertyProvider> Supplier<MapCodec<T>> register(String name, Supplier<MapCodec<T>> supplier) {
    return DEFERRED_REGISTER.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }

}
