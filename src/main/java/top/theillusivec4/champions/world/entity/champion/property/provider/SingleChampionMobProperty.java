package top.theillusivec4.champions.world.entity.champion.property.provider;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import top.theillusivec4.champions.world.entity.champion.property.ChampionMobProperty;

public record SingleChampionMobProperty(ChampionMobProperty value) implements ChampionMobPropertyProvider {
  public static final MapCodec<SingleChampionMobProperty> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    ChampionMobProperty.MAP_CODEC.forGetter(SingleChampionMobProperty::value)
  ).apply(instance, SingleChampionMobProperty::new));

  @Override
  public ChampionMobProperty get() {
    return value;
  }

  @Override
  public MapCodec<? extends ChampionMobPropertyProvider> codec() {
    return MAP_CODEC;
  }
}
