package top.theillusivec4.champions.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import top.theillusivec4.champions.common.capabilities.ChampionAttachment;
import top.theillusivec4.champions.common.rank.Rank;

import java.util.Optional;
import java.util.Set;

@Deprecated
public record EntityIsChampion(
  Optional<Integer> minTier,
  Optional<Integer> maxTier,
  LootContext.EntityTarget target
) implements LootItemCondition {

  public static final MapCodec<EntityIsChampion> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.INT.optionalFieldOf("minTier").forGetter(EntityIsChampion::minTier),
    Codec.INT.optionalFieldOf("maxTier").forGetter(EntityIsChampion::maxTier),
    LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(EntityIsChampion::target)
  ).apply(instance, EntityIsChampion::new));

  @Override
  public Set<ContextKey<?>> getReferencedContextParams() {
    return ImmutableSet.of(target.contextParam());
  }

  @Override
  public boolean test(LootContext context) {
    Entity entity = context.getOptionalParameter(target.contextParam());
    return entity != null && isChampion(entity);
  }

//  @Override
//  public LootItemConditionType getType() {
//    return ModLootItemConditions.ENTITY_IS_CHAMPION.get();
//  }

  @Override
  public MapCodec<EntityIsChampion> codec() {
    return CODEC;
  }

//  @Override
//  public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
//    return isChampion(entity);
//  }

  private boolean isChampion(Entity entity) {
    return ChampionAttachment.getAttachment(entity).map(champion -> {
      int tier = champion.getServer().getRank().map(Rank::getTier).orElse(0);
      boolean aboveMin = minTier.map(integer -> tier >= integer).orElseGet(() -> tier >= 1);
      boolean belowMax = maxTier.isEmpty() || tier <= maxTier.get();
      return aboveMin && belowMax;
    }).orElse(false);
  }
}
