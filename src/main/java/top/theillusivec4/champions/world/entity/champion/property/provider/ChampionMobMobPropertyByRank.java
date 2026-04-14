package top.theillusivec4.champions.world.entity.champion.property.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import top.theillusivec4.champions.world.entity.champion.property.ChampionMobProperty;
import top.theillusivec4.champions.world.entity.champion.Rank;

public record ChampionMobMobPropertyByRank(Holder<Rank> rank) implements ChampionMobPropertyProvider {
  public static final MapCodec<ChampionMobMobPropertyByRank> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Rank.REFERENCE_CODEC.fieldOf("rank").forGetter(ChampionMobMobPropertyByRank::rank)
  ).apply(instance, ChampionMobMobPropertyByRank::new));
  public static final Codec<ChampionMobMobPropertyByRank> DIRECT_CODEC = Rank.REFERENCE_CODEC.xmap(ChampionMobMobPropertyByRank::new, ChampionMobMobPropertyByRank::rank);

  @Override
  public ChampionMobProperty get() {
    Rank value = this.rank.value();
    return new ChampionMobProperty(value.tier(), value.color(), value.description(), value.boss());
  }

  @Override
  public MapCodec<? extends ChampionMobPropertyProvider> codec() {
    return MAP_CODEC;
  }
}
