package top.theillusivec4.champions.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.registry.ModLootItemConditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class EntityIsChampion implements ILootCondition {

    @Nullable
    private final Integer minTier;
    @Nullable
    private final Integer maxTier;
    private final LootContext.EntityTarget target;

    private EntityIsChampion(@Nullable Integer minTier, @Nullable Integer maxTier,
                             LootContext.EntityTarget targetIn) {
        this.minTier = minTier;
        this.maxTier = maxTier;
        this.target = targetIn;
    }

    @Nonnull
    @Override
    public Set<LootParameter<?>> getReferencedContextParams() {
        return ImmutableSet.of(this.target.getParam());
    }

    @Override
    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(this.target.getParam());

        if (entity == null) {
            return false;
        } else {
            return ChampionCapability.getCapability(entity).map(champion -> {
                int tier = champion.getServer().getRank().map(Rank::getTier).orElse(0);
                boolean aboveMin = minTier == null ? tier >= 1 : tier >= minTier;
                boolean belowMax = maxTier == null || tier <= maxTier;
                return aboveMin && belowMax;
            }).orElse(false);
        }
    }

    @Nonnull
    @Override
    public LootConditionType getType() {
        return ModLootItemConditions.ENTITY_IS_CHAMPION;
    }

    public static class Serializer
            implements ILootSerializer<EntityIsChampion> {

        @Override
        public void serialize(final JsonObject json, final EntityIsChampion value,
                              final JsonSerializationContext context) {
            json.addProperty("maxTier", value.maxTier);
            json.addProperty("minTier", value.minTier);
            json.add("entity", context.serialize(value.target));
        }

        @Nonnull
        @Override
        public EntityIsChampion deserialize(
                JsonObject json,
                @Nonnull JsonDeserializationContext context) {
            Integer minTier = json.has("minTier") ? JSONUtils.getAsInt(json, "minTier") : null;
            Integer maxTier = json.has("maxTier") ? JSONUtils.getAsInt(json, "maxTier") : null;

            return new EntityIsChampion(minTier, maxTier,
		            JSONUtils.getAsObject(json, "entity", context, LootContext.EntityTarget.class));
        }
    }
}
