package top.theillusivec4.champions.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.rank.Rank;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public record LootItemChampionPropertyCondition(LootContext.EntityTarget target,
                                                MinMaxBounds.Ints tier, AffixesPredicate affixes)
        implements LootItemCondition {

    public static final LootItemConditionType INSTANCE =
            new LootItemConditionType(new ChampionConditionSerializer());

    @Nonnull
    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(this.target.getParam());
    }

    @Override
    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(this.target.getParam());
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
    public LootItemConditionType getType() {
        return INSTANCE;
    }

    public static class ChampionConditionSerializer
            implements Serializer<LootItemChampionPropertyCondition> {

        @Override
        public void serialize(final JsonObject json, final LootItemChampionPropertyCondition value,
                              final JsonSerializationContext context) {
            json.add("tier", value.tier.serializeToJson());
            json.add("affixes", value.affixes.serializeToJson());
            json.add("entity", context.serialize(value.target));
        }

        @Nonnull
        @Override
        public LootItemChampionPropertyCondition deserialize(JsonObject json, @Nonnull
        JsonDeserializationContext context) {
            MinMaxBounds.Ints tier = MinMaxBounds.Ints.fromJson(json.get("tier"));
            AffixesPredicate affixes = AffixesPredicate.fromJson(json.get("affixes"));
            return new LootItemChampionPropertyCondition(
                    GsonHelper.getAsObject(json, "entity", context, LootContext.EntityTarget.class), tier,
                    affixes);
        }
    }
}
