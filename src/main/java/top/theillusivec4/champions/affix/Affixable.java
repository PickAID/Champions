package top.theillusivec4.champions.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.common.data.DataMapProvider;

public record Affixable(int value) {
  public static final MapCodec<Affixable> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Codec.INT.fieldOf("value").forGetter(Affixable::value)
  ).apply(instance, Affixable::new));
  public static final Codec<Affixable> CODEC = Codec.lazyInitialized(() -> {
    Codec<Affixable> directCodec = Codec.INT.xmap(Affixable::new, Affixable::value);
    return Codec.withAlternative(directCodec, MAP_CODEC.codec());
  });

  public static void bootstrap(DataMapProvider.Builder<Affixable, EntityType<?>> context) {
    for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
      if (entityType.getCategory() != MobCategory.MONSTER) {
        register(context, entityType, 0);
      }
    }
  }

  private static void register(DataMapProvider.Builder<Affixable, EntityType<?>> context, EntityType<?> entityType, int value) {
    context.add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType), new Affixable(value), false);
  }
}
