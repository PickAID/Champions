package top.theillusivec4.champions.champion.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public interface AllOf {
  static <T, A extends T> MapCodec<A> codec(Codec<T> codec, Function<List<T>, A> constructor, Function<A, List<T>> accessor) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(
      codec.listOf().fieldOf("effects").forGetter(accessor)
    ).apply(instance, constructor));
  }

  static AffixEntityEffect entityEffects(AffixEntityEffect... effects) {
    return new AllOf.EntityEffects(Arrays.stream(effects).toList());
  }

  static AffixValueEffect valueEffects(AffixValueEffect... effects) {
    return new AllOf.ValueEffects(Arrays.stream(effects).toList());
  }

  record ValueEffects(List<AffixValueEffect> effects) implements AffixValueEffect {
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

  }

  record EntityEffects(List<AffixEntityEffect> effects) implements AffixEntityEffect {
    public static final MapCodec<EntityEffects> MAP_CODEC = AllOf.codec(AffixEntityEffect.CODEC, EntityEffects::new, EntityEffects::effects);

    @Override
    public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 origin) {
      for (AffixEntityEffect effect : this.effects) {
        effect.apply(level, affixLevel, source, target, origin);
      }
    }

    @Override
    public MapCodec<? extends AffixEntityEffect> codec() {
      return MAP_CODEC;
    }

  }
}
