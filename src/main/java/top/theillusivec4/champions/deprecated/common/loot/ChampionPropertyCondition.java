package top.theillusivec4.champions.deprecated.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import top.theillusivec4.champions.deprecated.api.affix.IAffix;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.common.capabilities.ChampionAttachment;
import top.theillusivec4.champions.deprecated.common.rank.Rank;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Deprecated
public record ChampionPropertyCondition(
  LootContext.EntityTarget target,
  Optional<MinMaxBounds.Ints> tier,
  Optional<AffixesPredicate> affixes
)
  implements LootItemCondition {

  public static final MapCodec<ChampionPropertyCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(ChampionPropertyCondition::target),
    MinMaxBounds.Ints.CODEC.optionalFieldOf("tier").forGetter(ChampionPropertyCondition::tier),
    AffixesPredicate.CODEC.optionalFieldOf("affixes").forGetter(ChampionPropertyCondition::affixes)
  ).apply(instance, ChampionPropertyCondition::new));

  @Override
  public Set<ContextKey<?>> getReferencedContextParams() {
    return ImmutableSet.of(this.target.contextParam());
  }

  @Override
  public boolean test(LootContext context) {
    var entity = context.getOptionalParameter(this.target.contextParam());
    return entity != null && isChampionAndMatches(entity);
  }

//  @Override
//  public LootItemConditionType getType() {
//    return ModLootItemConditions.CHAMPION_PROPERTIES.get();
//  }

  @Override
  public MapCodec<ChampionPropertyCondition> codec() {
    return CODEC;
  }

//  @Override
//  public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
//    if (level.getLevel().isClientSide())
//      return false;
//
//    return isChampionAndMatches(entity);
//  }

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
