package top.theillusivec4.champions.world.level.storage.loot;

import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootParams;
import top.theillusivec4.champions.api.championmob.ChampionMobPropertyHelper;
import top.theillusivec4.champions.world.entity.damagetracker.DamageTrackerHelper;
import top.theillusivec4.champions.world.level.storage.loot.parameters.ChampionsLootContextParams;

public final class ExtraLootParamsHelper {
	private ExtraLootParamsHelper() {
	}

	public static void addParameters(ContextKeySet.Builder builder) {
		builder
				.required(ChampionsLootContextParams.CHAMPION_MOB_PROPERTY)
				.required(ChampionsLootContextParams.DAMAGE_TRACKER);
	}

	public static void withParameters(Entity entity, LootParams.Builder builder) {
		builder
				.withParameter(ChampionsLootContextParams.CHAMPION_MOB_PROPERTY, ChampionMobPropertyHelper.get(entity))
				.withParameter(ChampionsLootContextParams.DAMAGE_TRACKER, DamageTrackerHelper.get(entity));
	}

}
