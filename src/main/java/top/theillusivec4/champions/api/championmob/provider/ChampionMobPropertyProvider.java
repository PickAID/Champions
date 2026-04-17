package top.theillusivec4.champions.api.championmob.provider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import top.theillusivec4.champions.api.championmob.Rank;
import top.theillusivec4.champions.api.championmob.ChampionMobProperty;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.world.entity.championmob.providers.ChampionMobMobPropertyByRank;
import top.theillusivec4.champions.world.entity.championmob.providers.SingleChampionMobProperty;

import java.util.function.Function;

public interface ChampionMobPropertyProvider {
  Codec<ChampionMobPropertyProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> {
    Codec<ChampionMobPropertyProvider> codec = ChampionsBuiltInRegistries.CHAMPION_PROPERTY_PROVIDER_TYPE.byNameCodec().dispatch(ChampionMobPropertyProvider::codec, Function.identity());
    return Codec.either(ChampionMobMobPropertyByRank.DIRECT_CODEC, codec)
      .xmap(
        either -> either.map(Function.identity(), Function.identity()),
        provider -> provider instanceof ChampionMobMobPropertyByRank byRank ? Either.left(byRank) : Either.right(provider)
      );
  });

  static ChampionMobPropertyProvider byRank(Holder<Rank> rank) {
    return new ChampionMobMobPropertyByRank(rank);
  }

  static ChampionMobPropertyProvider single(ChampionMobProperty.Builder builder) {
    return new SingleChampionMobProperty(builder.build());
  }

  ChampionMobProperty get();

  MapCodec<? extends ChampionMobPropertyProvider> codec();
}
