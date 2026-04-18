package top.theillusivec4.champions.world.entity.championmob.providers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import top.theillusivec4.champions.api.championmob.provider.ChampionMobPropertyProvider;
import top.theillusivec4.champions.api.championmob.Rank;
import top.theillusivec4.champions.api.championmob.ChampionMobProperty;

public record ChampionMobPropertyByRank(Holder<Rank> rank) implements ChampionMobPropertyProvider {
  public static final MapCodec<ChampionMobPropertyByRank> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Rank.REFERENCE_CODEC.fieldOf("rank").forGetter(ChampionMobPropertyByRank::rank)
  ).apply(instance, ChampionMobPropertyByRank::new));
  public static final Codec<ChampionMobPropertyByRank> DIRECT_CODEC = Rank.REFERENCE_CODEC.xmap(ChampionMobPropertyByRank::new, ChampionMobPropertyByRank::rank);

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
