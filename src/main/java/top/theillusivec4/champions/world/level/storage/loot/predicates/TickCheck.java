package top.theillusivec4.champions.world.level.storage.loot.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Optional;

public record TickCheck(Target target, Optional<Long> period, IntRange value) implements LootItemCondition {
  public static final MapCodec<TickCheck> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Target.CODEC.fieldOf("target").forGetter(TickCheck::target),
    Codec.LONG.optionalFieldOf("period").forGetter(TickCheck::period),
    IntRange.CODEC.fieldOf("value").forGetter(TickCheck::value)
  ).apply(instance, TickCheck::new));

  public static TickCheck.Builder tick(Target target, IntRange value) {
    return new TickCheck.Builder(target, value);
  }

  @Override
  public LootItemConditionType getType() {
    return ChampionsLootItemConditions.TICK_CHECK.get();
  }

  @Override
  public boolean test(LootContext lootContext) {
    return switch (this.target) {
      case LEVEL -> {
        long i = lootContext.getLevel().getDayTime();
        if (this.period.isPresent()) {
          i %= this.period.get();
        }
        yield this.value.test(lootContext, (int) i);
      }
      case THIS_ENTITY -> Optional.ofNullable(lootContext.getParamOrNull(LootContextParams.THIS_ENTITY))
        .map(entity -> {
          int i = entity.tickCount;
          if (this.period.isPresent()) {
            i %= this.period.get();
          }
          return this.value.test(lootContext, i);
        })
        .orElse(false);
    };
  }

  public enum Target implements StringRepresentable {
    LEVEL("level"),
    THIS_ENTITY("this_entity");
    public static final Codec<Target> CODEC = StringRepresentable.fromEnum(Target::values);
    private final String name;

    Target(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return name;
    }
  }

  public static class Builder implements LootItemCondition.Builder {
    private final Target target;
    private final IntRange value;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Long> period = Optional.empty();

    public Builder(Target target, IntRange value) {
      this.target = target;
      this.value = value;
    }

    public Builder setPeriod(long period) {
      this.period = Optional.of(period);
      return this;
    }

    @Override
    public LootItemCondition build() {
      return new TickCheck(this.target, this.period, this.value);
    }
  }
}
