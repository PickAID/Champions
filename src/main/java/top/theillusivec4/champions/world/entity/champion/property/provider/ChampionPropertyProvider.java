package top.theillusivec4.champions.world.entity.champion.property.provider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.world.entity.champion.Rank;
import top.theillusivec4.champions.world.entity.champion.property.ChampionProperty;

import java.util.function.Function;

public interface ChampionPropertyProvider {
  Codec<ChampionPropertyProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> {
    Codec<ChampionPropertyProvider> codec = ChampionsBuiltInRegistries.CHAMPION_PROPERTY_PROVIDER_TYPE.byNameCodec().dispatch(ChampionPropertyProvider::codec, Function.identity());
    return Codec.either(ChampionPropertyByRank.DIRECT_CODEC, codec)
      .xmap(
        either -> either.map(Function.identity(), Function.identity()),
        provider -> provider instanceof ChampionPropertyByRank byRank ? Either.left(byRank) : Either.right(provider)
      );
  });

  static ChampionPropertyProvider byRank(Holder<Rank> rank) {
    return new ChampionPropertyByRank(rank);
  }

  static ChampionPropertyProvider single(ChampionProperty.Builder builder) {
    return new SingleChampionProperty(builder.build());
  }

  ChampionProperty get();

  MapCodec<? extends ChampionPropertyProvider> codec();
}
