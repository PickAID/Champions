package top.theillusivec4.champions.common.loot;


import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.entity.Entity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.registry.ModLootItemConditions;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class LootItemChampionPropertyCondition
		implements ILootCondition {
	private final LootContext.EntityTarget target;
	private final MinMaxBounds.IntBound tier;
	private final AffixesPredicate affixes;

	public LootItemChampionPropertyCondition(LootContext.EntityTarget target,
	                                         MinMaxBounds.IntBound tier, AffixesPredicate affixes) {
		this.target = target;
		this.tier = tier;
		this.affixes = affixes;
	}

	@Nonnull
	@Override
	public Set<LootParameter<?>> getReferencedContextParams() {
		return ImmutableSet.of(this.target.getParam());
	}

	@Override
	public boolean test(LootContext context) {
		Entity entity = context.getParamOrNull(this.target.getParam());
		return entity != null && isChampionAndMatches(entity);
	}

	public boolean isChampionAndMatches(Entity entity) {
		return ChampionCapability.getCapability(entity).map(champion -> {
			IChampion.Server server = champion.getServer();
			int tier = server.getRank().map(Rank::getTier).orElse(0);

			if (tier <= 0 || !this.tier.matches(tier)) {
				return false;
			}
			List<IAffix> affixes = server.getAffixes();
			return this.affixes.matches(affixes);
		}).orElse(false);
	}

	@Nonnull
	@Override
	public LootConditionType getType() {
		return ModLootItemConditions.CHAMPION_PROPERTIES;
	}

	public LootContext.EntityTarget target() {
		return target;
	}

	public MinMaxBounds.IntBound tier() {
		return tier;
	}

	public AffixesPredicate affixes() {
		return affixes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		LootItemChampionPropertyCondition that = (LootItemChampionPropertyCondition) obj;
		return Objects.equals(this.target, that.target) &&
				Objects.equals(this.tier, that.tier) &&
				Objects.equals(this.affixes, that.affixes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(target, tier, affixes);
	}

	@Override
	public String toString() {
		return "LootItemChampionPropertyCondition[" +
				"target=" + target + ", " +
				"tier=" + tier + ", " +
				"affixes=" + affixes + ']';
	}


	public static class ChampionConditionSerializer implements ILootSerializer<LootItemChampionPropertyCondition> {

		@Override
		public void serialize(final JsonObject json, final LootItemChampionPropertyCondition value,
		                      final JsonSerializationContext context) {
			json.add("tier", value.tier.serializeToJson());
			json.add("affixes", value.affixes.serializeToJson());
			json.add("entity", context.serialize(value.target));
		}

		@Override
		public LootItemChampionPropertyCondition deserialize(JsonObject json, JsonDeserializationContext context) {
			MinMaxBounds.IntBound tier = MinMaxBounds.IntBound.fromJson(json.get("tier"));
			AffixesPredicate affixes = AffixesPredicate.fromJson(json.get("affixes"));
			return new LootItemChampionPropertyCondition(
					JSONUtils.getAsObject(json, "entity", context, LootContext.EntityTarget.class), tier,
					affixes);
		}
	}
}
