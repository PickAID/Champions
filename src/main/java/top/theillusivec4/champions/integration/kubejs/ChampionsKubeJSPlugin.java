package top.theillusivec4.champions.integration.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.integration.kubejs.affix.AffixBuilder;
import top.theillusivec4.champions.world.entity.affix.Affix;
import top.theillusivec4.champions.world.entity.champion.Rank;
import top.theillusivec4.champions.world.item.champion.ChampionMobEggTemplate;

public class ChampionsKubeJSPlugin implements KubeJSPlugin {

  @Override
  public void registerBuilderTypes(BuilderTypeRegistry registry) {
    registry.addDefault(ChampionsRegistries.AFFIX, AffixBuilder.class, AffixBuilder::new);
  }

  @Override
  public void registerServerRegistries(ServerRegistryRegistry registry) {
    registry.register(ChampionsRegistries.AFFIX, Affix.DIRECT_CODEC, Affix.class);
    registry.register(ChampionsRegistries.RANK, Rank.DIRECT_CODEC, Rank.class);
    registry.register(ChampionsRegistries.CHAMPION_MOB_EGG, ChampionMobEggTemplate.DIRECT_CODEC, ChampionMobEggTemplate.class);
  }
}
