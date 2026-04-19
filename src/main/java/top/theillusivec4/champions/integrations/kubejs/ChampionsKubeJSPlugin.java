package top.theillusivec4.champions.integrations.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.api.affix.effect.AffixEntityEffect;
import top.theillusivec4.champions.api.affix.effect.AffixValueEffect;
import top.theillusivec4.champions.api.championmob.ChampionMobEggTemplate;
import top.theillusivec4.champions.api.championmob.Rank;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.integrations.kubejs.builder.AffixBuilder;
import top.theillusivec4.champions.integrations.kubejs.wrapper.AffixEntityEffectWrapper;
import top.theillusivec4.champions.integrations.kubejs.wrapper.AffixValueEffectWrapper;
import top.theillusivec4.champions.integrations.kubejs.wrapper.LevelBasedValueWrapper;

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

  @Override
  public void registerTypeWrappers(TypeWrapperRegistry registry) {
    registry.register(LevelBasedValue.class, LevelBasedValueWrapper::wrap);
    registry.register(AffixValueEffect.class, AffixValueEffectWrapper::wrap);
    registry.register(AffixEntityEffect.class, AffixEntityEffectWrapper::wrap);
  }
}
