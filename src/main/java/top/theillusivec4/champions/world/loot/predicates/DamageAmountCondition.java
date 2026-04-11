package top.theillusivec4.champions.world.loot.predicates;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record DamageAmountCondition() implements LootItemCondition {
  @Override
  public LootItemConditionType getType() {
    return null;
  }

  @Override
  public boolean test(LootContext lootContext) {
    return false;
  }
}
