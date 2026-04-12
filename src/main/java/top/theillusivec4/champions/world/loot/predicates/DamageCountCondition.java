package top.theillusivec4.champions.world.loot.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import top.theillusivec4.champions.world.loot.parameters.ChampionsLootContextParams;

import java.util.Optional;

public record DamageCountCondition(MinMaxBounds.Ints count) implements LootItemCondition {
  public static final MapCodec<DamageCountCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    MinMaxBounds.Ints.CODEC.fieldOf("count").forGetter(DamageCountCondition::count)
  ).apply(instance, DamageCountCondition::new));

  public static DamageCountCondition.Builder builder() {
    return new Builder();
  }

  @Override
  public LootItemConditionType getType() {
    return ChampionsLootItemConditions.DAMAGE_COUNT.get();
  }

  @Override
  public boolean test(LootContext lootContext) {
    return Optional.ofNullable(lootContext.getParamOrNull(ChampionsLootContextParams.DAMAGE_COUNT))
      .map(this.count::matches)
      .orElse(false);
  }

  public static class Builder implements LootItemCondition.Builder {
    private MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;

    public Builder setCount(MinMaxBounds.Ints count) {
      this.count = count;
      return this;
    }

    @Override
    public LootItemCondition build() {
      return new DamageCountCondition(count);
    }
  }
}
