package top.theillusivec4.champions.common.registry;

import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import top.theillusivec4.champions.common.loot.EntityIsChampion;
import top.theillusivec4.champions.common.loot.LootItemChampionPropertyCondition;

public class ModLootItemConditions {

	public static final LootConditionType CHAMPION_PROPERTIES = register("champions:champion_properties", new LootConditionType(new LootItemChampionPropertyCondition.ChampionConditionSerializer()));
	public static final LootConditionType ENTITY_IS_CHAMPION = register("champions:entity_champion", new LootConditionType(new EntityIsChampion.Serializer()));

	private static LootConditionType register(String id, LootConditionType type) {
		return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(id), type);
	}

	public static void init() {
	}
}
