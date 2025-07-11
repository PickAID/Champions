package top.theillusivec4.champions.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.loot.EntityIsChampion;
import top.theillusivec4.champions.common.loot.LootItemChampionPropertyCondition;

public class ModLootItemConditions {

	private static final DeferredRegister<LootItemConditionType> LOOT_ITEM_CONDITION_TYPE = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, Champions.MODID);
	public static final RegistryObject<LootItemConditionType> CHAMPION_PROPERTIES = LOOT_ITEM_CONDITION_TYPE.register("champion_properties", () -> new LootItemConditionType(new LootItemChampionPropertyCondition.ChampionConditionSerializer()));
	public static final RegistryObject<LootItemConditionType> ENTITY_IS_CHAMPION = LOOT_ITEM_CONDITION_TYPE.register("entity_champion", () -> new LootItemConditionType(new EntityIsChampion.Serializer()));

	public static void register(IEventBus bus) {
		LOOT_ITEM_CONDITION_TYPE.register(bus);
	}
}
