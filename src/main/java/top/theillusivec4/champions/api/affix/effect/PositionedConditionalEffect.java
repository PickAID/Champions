package top.theillusivec4.champions.api.affix.effect;

import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record PositionedConditionalEffect<T>(T effect, AffixPosition position, Optional<LootItemCondition> requirements) implements Validatable {

  @Override
  public void validate(ValidationContext context) {
    Validatable.validate(context, "requirements", this.requirements);
  }
}
