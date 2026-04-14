package top.theillusivec4.champions.world.entity.champion.property.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import top.theillusivec4.champions.world.entity.champion.Rank;
import top.theillusivec4.champions.world.entity.champion.property.ChampionProperty;

public record ChampionPropertyByRank(Holder<Rank> rank) implements ChampionPropertyProvider {
  public static final MapCodec<ChampionPropertyByRank> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    Rank.REFERENCE_CODEC.fieldOf("rank").forGetter(ChampionPropertyByRank::rank)
  ).apply(instance, ChampionPropertyByRank::new));
  public static final Codec<ChampionPropertyByRank> DIRECT_CODEC = Rank.REFERENCE_CODEC.xmap(ChampionPropertyByRank::new, ChampionPropertyByRank::rank);

  @Override
  public ChampionProperty get() {
    Rank value = this.rank.value();
    return new ChampionProperty(value.tier(), value.color(), value.description(), value.boss());
  }

  @Override
  public MapCodec<? extends ChampionPropertyProvider> codec() {
    return MAP_CODEC;
  }
}
