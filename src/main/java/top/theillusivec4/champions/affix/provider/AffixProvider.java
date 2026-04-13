package top.theillusivec4.champions.affix.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.affix.AffixInstance;
import top.theillusivec4.champions.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.function.Function;
import java.util.stream.Stream;

public interface AffixProvider {
  Codec<AffixProvider> DIRECT_CODEC = Codec.lazyInitialized(() -> ChampionsBuiltInRegistries.AFFIX_PROVIDER_TYPE.byNameCodec().dispatch(AffixProvider::codec, Function.identity()));
  Codec<Holder<AffixProvider>> REFERENCE_CODEC = RegistryFileCodec.create(ChampionsRegistries.AFFIX_PROVIDER, DIRECT_CODEC, false);
  Codec<HolderSet<AffixProvider>> LIST_CODEC = RegistryCodecs.homogeneousList(ChampionsRegistries.AFFIX_PROVIDER, DIRECT_CODEC);

  static AffixProvider byCost(HolderSet<Affix> affixes, IntProvider cost) {
    return new AffixesByCost(affixes, cost);
  }

  static AffixProvider byCostWithDifficulty(HolderSet<Affix> affixes, int minCost, int maxCostSpan) {
    return new AffixesByCostWithDifficulty(affixes, minCost, maxCostSpan);
  }

  static AffixProvider single(Holder<Affix> affix, IntProvider level) {
    return new SingleAffix(affix, level);
  }

  Stream<AffixInstance> get(EntityType<?> entity, RandomSource random, DifficultyInstance difficulty);

  MapCodec<? extends AffixProvider> codec();

  default void affixTo(EntityType<?> entity, AffixContainer.Mutable mutable, RandomSource random, DifficultyInstance instance) {
    this.get(entity, random, instance).forEach(affixInstance -> mutable.upgrade(affixInstance.affix(), affixInstance.level()));
  }
}
