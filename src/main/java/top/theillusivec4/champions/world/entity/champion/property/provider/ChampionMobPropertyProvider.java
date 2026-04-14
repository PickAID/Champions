package top.theillusivec4.champions.world.entity.champion.property.provider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.world.entity.champion.Rank;
import top.theillusivec4.champions.world.entity.champion.property.ChampionMobProperty;

import java.util.function.Function;

public interface ChampionMobPropertyProvider {
  Codec<ChampionMobPropertyProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> {
    Codec<ChampionMobPropertyProvider> codec = ChampionsBuiltInRegistries.CHAMPION_PROPERTY_PROVIDER_TYPE.byNameCodec().dispatch(ChampionMobPropertyProvider::codec, Function.identity());
    return Codec.either(ChampionMobPropertyByRank.DIRECT_CODEC, codec)
      .xmap(
        either -> either.map(Function.identity(), Function.identity()),
        provider -> provider instanceof ChampionMobPropertyByRank byRank ? Either.left(byRank) : Either.right(provider)
      );
  });

  static ChampionMobPropertyProvider byRank(Holder<Rank> rank) {
    return new ChampionMobPropertyByRank(rank);
  }

  static ChampionMobPropertyProvider single(ChampionMobProperty.Builder builder) {
    return new SingleChampionMobProperty(builder.build());
  }

  ChampionMobProperty get();

  MapCodec<? extends ChampionMobPropertyProvider> codec();
}
