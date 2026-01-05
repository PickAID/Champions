package top.theillusivec4.champions.api.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.api.affix.effect.entity.AffixEntityEffect;
import top.theillusivec4.champions.api.affix.effect.value.AffixValueEffect;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class AllOf {
  public static <T, A extends T> MapCodec<A> codec(Codec<T> codec, Function<List<T>, A> constructor, Function<A, List<T>> accessor) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(
      codec.listOf().fieldOf("effects").forGetter(accessor)
    ).apply(instance, constructor));
  }

  public static AffixEntityEffect entityEffects(AffixEntityEffect... effects) {
    return new AllOf.EntityEffects(Arrays.stream(effects).toList());
  }

  public static AffixValueEffect valueEffects(AffixValueEffect... effects) {
    return new AllOf.ValueEffects(Arrays.stream(effects).toList());
  }

  private AllOf() {
  }

  public record ValueEffects(List<AffixValueEffect> effects) implements AffixValueEffect {
    public static final MapCodec<ValueEffects> MAP_CODEC = AllOf.codec(AffixValueEffect.CODEC, ValueEffects::new, ValueEffects::effects);

    @Override
    public float process(LootContext context, int level, float inputValue) {
      for (AffixValueEffect effect : this.effects) {
        inputValue = effect.process(context, level, inputValue);
      }

      return inputValue;
    }

    @Override
    public MapCodec<? extends AffixValueEffect> codec() {
      return MAP_CODEC;
    }

    @Override
    public void validate(ValidationContext context) {
      for (int i = 0; i < this.effects.size(); i++) {
        AffixValueEffect effect = this.effects.get(i);
        Validatable.validate(context, "effects[" + i + "]", effect);
      }
    }
  }

  public record EntityEffects(List<AffixEntityEffect> effects) implements AffixEntityEffect {
    public static final MapCodec<EntityEffects> MAP_CODEC = AllOf.codec(AffixEntityEffect.CODEC, EntityEffects::new, EntityEffects::effects);

    @Override
    public void apply(LootContext context, int level, Entity entity, Vec3 position) {
      for (AffixEntityEffect effect : this.effects) {
        effect.apply(context, level, entity, position);
      }
    }

    @Override
    public MapCodec<? extends AffixEntityEffect> codec() {
      return MAP_CODEC;
    }

    @Override
    public void validate(ValidationContext context) {
      for (int i = 0; i < this.effects.size(); i++) {
        AffixEntityEffect effect = this.effects.get(i);
        Validatable.validate(context, "effects[" + i + "]", effect);
      }
    }
  }
}
