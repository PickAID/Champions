package top.theillusivec4.champions.advancements.critereon;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;

import java.util.function.Supplier;

public final class ChampionsEntitySubPredicates {
  private static final DeferredRegister<MapCodec<? extends EntitySubPredicate>> DEFERRED_REGISTER = DeferredRegister.create(Registries.ENTITY_SUB_PREDICATE_TYPE, Champions.MOD_ID);
  public static final Supplier<MapCodec<ChampionPropertyPredicate>> CHAMPION_PROPERTY = register("champion_property", () -> ChampionPropertyPredicate.MAP_CODEC);

  private ChampionsEntitySubPredicates() {
  }

  private static <T extends EntitySubPredicate> Supplier<MapCodec<T>> register(String name, Supplier<MapCodec<T>> supplier) {
    return DEFERRED_REGISTER.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
