package top.theillusivec4.champions.core.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegistryBuilder;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.api.affix.ProjectileTemplate;
import top.theillusivec4.champions.api.affix.effect.AffixEntityEffect;
import top.theillusivec4.champions.api.affix.effect.AffixLocationBasedEffect;
import top.theillusivec4.champions.api.affix.effect.AffixValueEffect;
import top.theillusivec4.champions.api.affix.provider.AffixProvider;
import top.theillusivec4.champions.api.championmob.provider.ChampionMobPropertyProvider;

public final class ChampionsBuiltInRegistries {
  public static final Registry<DataComponentType<?>> AFFIX_EFFECT_COMPONENT_TYPE = register(ChampionsRegistries.AFFIX_EFFECT_COMPONENT_TYPE);
  public static final Registry<MapCodec<? extends AffixValueEffect>> AFFIX_VALUE_EFFECT_TYPE = register(ChampionsRegistries.AFFIX_VALUE_EFFECT_TYPE);
  public static final Registry<MapCodec<? extends AffixEntityEffect>> AFFIX_ENTITY_EFFECT_TYPE = register(ChampionsRegistries.AFFIX_ENTITY_EFFECT_TYPE);
  public static final Registry<MapCodec<? extends AffixLocationBasedEffect>> AFFIX_LOCATION_BASED_EFFECT_TYPE = register(ChampionsRegistries.AFFIX_LOCATION_BASED_EFFECT_TYPE);
  public static final Registry<MapCodec<? extends LevelBasedValue>> LEVEL_BASED_VALUE_TYPE = register(ChampionsRegistries.LEVEL_BASED_VALUE_TYPE);
  public static final Registry<MapCodec<? extends ProjectileTemplate>> PROJECTILE_TEMPLATE_TYPE = register(ChampionsRegistries.PROJECTILE_TEMPLATE_TYPE);
  public static final Registry<MapCodec<? extends AffixProvider>> AFFIX_PROVIDER_TYPE = register(ChampionsRegistries.AFFIX_PROVIDER_TYPE);
  public static final Registry<MapCodec<? extends ChampionMobPropertyProvider>> CHAMPION_PROPERTY_PROVIDER_TYPE = register(ChampionsRegistries.CHAMPION_PROPERTY_PROVIDER_TYPE);

  private ChampionsBuiltInRegistries() {
  }

  private static <T> Registry<T> register(ResourceKey<Registry<T>> registryKey) {
    return new RegistryBuilder<>(registryKey).sync(true).create();
  }

}
