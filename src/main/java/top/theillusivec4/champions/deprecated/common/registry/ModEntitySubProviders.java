package top.theillusivec4.champions.deprecated.common.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.deprecated.common.loot.ChampionPropertyCondition;
import top.theillusivec4.champions.deprecated.common.loot.EntityIsChampion;

public class ModEntitySubProviders {
  public static final DeferredRegister<MapCodec<? extends EntitySubPredicate>> ENTITY_SUB_PREDICATE_TYPES = DeferredRegister.create(Registries.ENTITY_SUB_PREDICATE_TYPE, ChampionsMod.MOD_ID);

  public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>, MapCodec<ChampionPropertyCondition>> CHAMPION_PROPERTIES = ENTITY_SUB_PREDICATE_TYPES.register("champion_properties", () -> ChampionPropertyCondition.CODEC);
  public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>, MapCodec<EntityIsChampion>> ENTITY_IS_CHAMPION = ENTITY_SUB_PREDICATE_TYPES.register("entity_is_champion", () -> EntityIsChampion.CODEC);

  public static void register(IEventBus bus) {
    ENTITY_SUB_PREDICATE_TYPES.register(bus);
  }
}
