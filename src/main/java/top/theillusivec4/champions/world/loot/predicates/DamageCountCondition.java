package top.theillusivec4.champions.world.loot.predicates;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record DamageCountCondition(MinMaxBounds.Ints count) implements LootItemCondition {

  public static DamageCountCondition.Builder builder() {
    return new Builder();
  }

  @Override
  public LootItemConditionType getType() {
    return ChampionsLootItemConditions.DAMAGE_COUNT.get();
  }

  @Override
  public boolean test(LootContext lootContext) {
    return false;
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
