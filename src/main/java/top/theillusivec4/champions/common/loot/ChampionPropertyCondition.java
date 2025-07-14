package top.theillusivec4.champions.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.registry.ModLootItemConditions;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public record ChampionPropertyCondition(LootContext.EntityTarget target,
                                        Optional<MinMaxBounds.Ints> tier, Optional<AffixesPredicate> affixes)
  implements LootItemCondition, EntitySubPredicate {

  public static final MapCodec<ChampionPropertyCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(ChampionPropertyCondition::target),
    MinMaxBounds.Ints.CODEC.optionalFieldOf("tier").forGetter(ChampionPropertyCondition::tier),
    AffixesPredicate.CODEC.optionalFieldOf("affixes").forGetter(ChampionPropertyCondition::affixes)
  ).apply(instance, ChampionPropertyCondition::new));

  @Nonnull
  @Override
  public Set<LootContextParam<?>> getReferencedContextParams() {
    return ImmutableSet.of(this.target.getParam());
  }

  @Override
  public boolean test(LootContext context) {
    var entity = context.getParamOrNull(this.target.getParam());
    return entity != null && isChampionAndMatches(entity);
  }

  @Override
  public LootItemConditionType getType() {
    return ModLootItemConditions.CHAMPION_PROPERTIES.get();
  }

  @Override
  public @NotNull MapCodec<? extends EntitySubPredicate> codec() {
    return MAP_CODEC;
  }

  @Override
  public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
    if (level.getLevel().isClientSide())
      return false;

    return isChampionAndMatches(entity);
  }

  public boolean isChampionAndMatches(Entity entity) {
    return ChampionAttachment.getAttachment(entity).map(champion -> {
      IChampion.Server server = champion.getServer();
      int tier = server.getRank().map(Rank::getTier).orElse(0);

      if (tier <= 0 || !this.tier.map(t -> t.matches(tier)).orElse(true)) {
        return false;
      }
      List<IAffix> affixes = server.getAffixes();
      return this.affixes.map(affixesPredicate -> affixesPredicate.matches(affixes)).orElse(true);
    }).orElse(false);
  }
}
