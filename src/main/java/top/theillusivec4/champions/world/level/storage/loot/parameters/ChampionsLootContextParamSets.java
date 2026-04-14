package top.theillusivec4.champions.world.level.storage.loot.parameters;

import com.google.common.collect.BiMap;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import top.theillusivec4.champions.mixin.LootContextParamSetsAccessor;
import top.theillusivec4.champions.util.ChampionsUtil;
import top.theillusivec4.champions.world.level.storage.loot.ExtraLootParamsHelper;

import java.util.function.Consumer;

public final class ChampionsLootContextParamSets {
	public static final ContextKeySet AFFIXED_LOCATION = register(
			"affixed_location",
			builder -> builder
					.required(LootContextParams.THIS_ENTITY)
					.required(LootContextParams.ORIGIN)
					.required(ChampionsLootContextParams.AFFIX_LEVEL)
	);
	public static final ContextKeySet AFFIXED_DAMAGE = register(
			"affixed_damage",
			builder -> builder
					.required(LootContextParams.THIS_ENTITY)
					.required(LootContextParams.ORIGIN)
					.required(LootContextParams.DAMAGE_SOURCE)
					.required(ChampionsLootContextParams.AFFIX_LEVEL)
					.optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
					.optional(LootContextParams.ATTACKING_ENTITY)
	);
	public static final ContextKeySet AFFIXED_ENTITY = register("affixed_entity",
			builder -> builder
					.required(LootContextParams.THIS_ENTITY)
					.required(LootContextParams.ORIGIN)
					.required(ChampionsLootContextParams.AFFIX_LEVEL)
	);

	private ChampionsLootContextParamSets() {
	}

	private static ContextKeySet register(String name, Consumer<ContextKeySet.Builder> consumer) {
		BiMap<Identifier, ContextKeySet> registry = LootContextParamSetsAccessor.getRegistry();
		ContextKeySet.Builder builder = new ContextKeySet.Builder();
		consumer.accept(builder);
		ExtraLootParamsHelper.addParameters(builder);
		Identifier id = ChampionsUtil.id(name);
		ContextKeySet set = builder.build();
		ContextKeySet set1 = registry.put(id, set);
		if (set1 != null) {
			throw new IllegalStateException("Loot table parameter set " + id + " is already registered");
		} else {
			return set;
		}
	}

}
