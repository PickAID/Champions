package top.theillusivec4.champions.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.registry.ModLootItemConditions;

import java.util.Optional;
import java.util.Set;

public record EntityIsChampion(Optional<Integer> minTier, Optional<Integer> maxTier,
                               LootContext.EntityTarget target) implements LootItemCondition, EntitySubPredicate {

  public static final MapCodec<EntityIsChampion> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.INT.optionalFieldOf("minTier").forGetter(EntityIsChampion::minTier),
    Codec.INT.optionalFieldOf("maxTier").forGetter(EntityIsChampion::maxTier),
    LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(EntityIsChampion::target)
  ).apply(instance, EntityIsChampion::new));

  @Override
  public Set<ContextKey<?>> getReferencedContextParams() {
    return ImmutableSet.of(target.getParam());
  }

  @Override
  public boolean test(LootContext context) {
    Entity entity = context.getOptionalParameter(target.getParam());
    return entity != null && isChampion(entity);
  }

  @Override
  public LootItemConditionType getType() {
    return ModLootItemConditions.ENTITY_IS_CHAMPION.get();
  }

  @Override
  public MapCodec<? extends EntitySubPredicate> codec() {
    return CODEC;
  }

  @Override
  public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
    return isChampion(entity);
  }

  private boolean isChampion(Entity entity) {
    return ChampionAttachment.getAttachment(entity).map(champion -> {
      int tier = champion.getServer().getRank().map(Rank::getTier).orElse(0);
      boolean aboveMin = minTier.map(integer -> tier >= integer).orElseGet(() -> tier >= 1);
      boolean belowMax = maxTier.isEmpty() || tier <= maxTier.get();
      return aboveMin && belowMax;
    }).orElse(false);
  }
}
