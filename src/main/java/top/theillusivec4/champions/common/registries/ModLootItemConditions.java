package top.theillusivec4.champions.common.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.loot.ChampionPropertyCondition;
import top.theillusivec4.champions.common.loot.EntityIsChampion;

@Deprecated
public class ModLootItemConditions {
  private static final DeferredRegister<MapCodec<? extends LootItemCondition>> LOOT_ITEM_CONDITION_TYPE = DeferredRegister.create(BuiltInRegistries.LOOT_CONDITION_TYPE, Champions.MODID);
  public static final DeferredHolder<MapCodec<? extends LootItemCondition>, MapCodec<ChampionPropertyCondition>> CHAMPION_PROPERTIES = LOOT_ITEM_CONDITION_TYPE.register("champion_properties", () -> ChampionPropertyCondition.CODEC);
  public static final DeferredHolder<MapCodec<? extends LootItemCondition>, MapCodec<EntityIsChampion>> ENTITY_IS_CHAMPION = LOOT_ITEM_CONDITION_TYPE.register("entity_champion", () -> EntityIsChampion.CODEC);

  public static void register(IEventBus bus) {
    LOOT_ITEM_CONDITION_TYPE.register(bus);
  }
}
