package top.theillusivec4.champions.world.loot.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import top.theillusivec4.champions.champion.affix.Damage;
import top.theillusivec4.champions.world.loot.parameters.ChampionsLootContextParams;

import java.util.Optional;
import java.util.Set;

public record LatestDamageCondition(Optional<MinMaxBounds.Ints> count, Optional<MinMaxBounds.Ints> latestTime) implements LootItemCondition {
  public static final LatestDamageCondition EMPTY = new LatestDamageCondition(Optional.empty(), Optional.empty());
  public static final MapCodec<LatestDamageCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    MinMaxBounds.Ints.CODEC.optionalFieldOf("count").forGetter(LatestDamageCondition::count),
    MinMaxBounds.Ints.CODEC.optionalFieldOf("latest_time").forGetter(LatestDamageCondition::latestTime)
  ).apply(instance, LatestDamageCondition::new));

  public static LootItemCondition.Builder builder() {
    return () -> EMPTY;
  }

  public static LootItemCondition.Builder builder(MinMaxBounds.Ints count) {
    return () -> new LatestDamageCondition(Optional.of(count), Optional.empty());
  }

  public static LootItemCondition.Builder builder(MinMaxBounds.Ints count, MinMaxBounds.Ints latestTime) {
    return () -> new LatestDamageCondition(Optional.of(count), Optional.of(latestTime));
  }

  @Override
  public MapCodec<? extends LootItemCondition> codec() {
    return MAP_CODEC;
  }

  @Override
  public boolean test(LootContext context) {
    DamageSource source = context.getOptionalParameter(net.minecraft.world.level.storage.loot.parameters.LootContextParams.DAMAGE_SOURCE);
    Damage damage = context.getOptionalParameter(ChampionsLootContextParams.LATEST_DAMAGE);
    return source != null && damage != null && damage.damageType() == source.typeHolder() && count.map(ints -> ints.matches(damage.damageCount())).orElse(true);
  }

  @Override
  public Set<ContextKey<?>> getReferencedContextParams() {
    return Set.of(
      net.minecraft.world.level.storage.loot.parameters.LootContextParams.DAMAGE_SOURCE,
      ChampionsLootContextParams.LATEST_DAMAGE
    );
  }

}
