package top.theillusivec4.champions.core.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.world.entity.affix.EntityAffixes;
import top.theillusivec4.champions.world.entity.champion.property.ChampionMobProperty;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ChampionsDataComponents {
  private static final DeferredRegister<DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, ChampionsMod.MOD_ID);
  public static final Supplier<DataComponentType<EntityAffixes>> STORED_AFFIXES = register("stored_affixes", builder -> builder.persistent(EntityAffixes.MAP_CODEC.codec()).networkSynchronized(EntityAffixes.STREAM_CODEC));
  public static final Supplier<DataComponentType<ChampionMobProperty>> STORED_CHAMPION_MOB_PROPERTY = register("stored_champion_mob_property", builder -> builder.persistent(ChampionMobProperty.MAP_CODEC.codec()).networkSynchronized(ChampionMobProperty.STREAM_CODEC));

  private ChampionsDataComponents() {
  }

  private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
    return DEFERRED_REGISTER.register(name, () -> builder.apply(new DataComponentType.Builder<>()).build());
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
