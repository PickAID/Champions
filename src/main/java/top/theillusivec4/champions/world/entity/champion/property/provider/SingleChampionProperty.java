package top.theillusivec4.champions.world.entity.champion.property.provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import top.theillusivec4.champions.world.entity.champion.property.ChampionProperty;

public record SingleChampionProperty(ChampionProperty value) implements ChampionPropertyProvider {
  public static final MapCodec<SingleChampionProperty> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    ChampionProperty.MAP_CODEC.forGetter(SingleChampionProperty::value)
  ).apply(instance, SingleChampionProperty::new));

  @Override
  public ChampionProperty get() {
    return value;
  }

  @Override
  public MapCodec<? extends ChampionPropertyProvider> codec() {
    return MAP_CODEC;
  }
}
