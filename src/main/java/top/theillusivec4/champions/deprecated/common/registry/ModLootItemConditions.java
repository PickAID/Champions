package top.theillusivec4.champions.deprecated.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.deprecated.common.loot.ChampionPropertyCondition;
import top.theillusivec4.champions.deprecated.common.loot.EntityIsChampion;

public class ModLootItemConditions {

  private static final DeferredRegister<LootItemConditionType> LOOT_ITEM_CONDITION_TYPE = DeferredRegister.create(BuiltInRegistries.LOOT_CONDITION_TYPE, ChampionsMod.MOD_ID);
  public static final DeferredHolder<LootItemConditionType, LootItemConditionType> CHAMPION_PROPERTIES = LOOT_ITEM_CONDITION_TYPE.register("champion_properties", () -> new LootItemConditionType(ChampionPropertyCondition.CODEC));
  public static final DeferredHolder<LootItemConditionType, LootItemConditionType> ENTITY_IS_CHAMPION = LOOT_ITEM_CONDITION_TYPE.register("entity_champion", () -> new LootItemConditionType(EntityIsChampion.CODEC));

  public static void register(IEventBus bus) {
    LOOT_ITEM_CONDITION_TYPE.register(bus);
  }
}
