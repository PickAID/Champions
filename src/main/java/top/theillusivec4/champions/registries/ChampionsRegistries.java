package top.theillusivec4.champions.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.affix.LevelBasedValue;
import top.theillusivec4.champions.affix.ProjectileTemplate;
import top.theillusivec4.champions.affix.effects.AffixEntityEffect;
import top.theillusivec4.champions.affix.effects.AffixLocationBasedEffect;
import top.theillusivec4.champions.affix.effects.AffixValueEffect;
import top.theillusivec4.champions.util.ChampionsUtil;

import java.util.ArrayList;
import java.util.List;

public final class ChampionsRegistries {
  private static List<Registry<?>> registries = new ArrayList<>();
  public static final Registry<MapCodec<? extends ProjectileTemplate>> PROJECTILE_TEMPLATE_TYPE = register(Keys.PROJECTILE_TEMPLATE_TYPE);
  public static final Registry<MapCodec<? extends LevelBasedValue>> LEVEL_BASED_VALUE_TYPE = register(Keys.LEVEL_BASED_VALUE_TYPE);
  public static final Registry<DataComponentType<?>> AFFIX_EFFECT_COMPONENT_TYPE = register(Keys.AFFIX_EFFECT_COMPONENT_TYPE);
  public static final Registry<MapCodec<? extends AffixLocationBasedEffect>> AFFIX_LOCATION_BASED_EFFECT_TYPE = register(Keys.AFFIX_LOCATION_BASED_EFFECT_TYPE);
  public static final Registry<MapCodec<? extends AffixEntityEffect>> AFFIX_ENTITY_EFFECT_TYPE = register(Keys.AFFIX_ENTITY_EFFECT_TYPE);
  public static final Registry<MapCodec<? extends AffixValueEffect>> AFFIX_VALUE_EFFECT_TYPE = register(Keys.AFFIX_VALUE_EFFECT_TYPE);

  private ChampionsRegistries() {
  }

  private static <T> Registry<T> register(ResourceKey<Registry<T>> registryKey) {
    var registry = new RegistryBuilder<>(registryKey).sync(true).create();
    registries.add(registry);
    return registry;
  }

  public static void register(IEventBus bus) {
    bus.addListener(ChampionsRegistries::registerRegistries);
    bus.addListener(ChampionsRegistries::registerDataPackRegistries);
  }

  private static void registerRegistries(NewRegistryEvent event) {
    registries.forEach(event::register);
    registries = null;
  }

  private static void registerDataPackRegistries(DataPackRegistryEvent.NewRegistry event) {

  }


  public static final class Keys {
    public static final ResourceKey<Registry<MapCodec<? extends ProjectileTemplate>>> PROJECTILE_TEMPLATE_TYPE = register("projectile_template_type");
    public static final ResourceKey<Registry<Affix>> AFFIX = register("affix");
    public static final ResourceKey<Registry<MapCodec<? extends LevelBasedValue>>> LEVEL_BASED_VALUE_TYPE = register("level_based_value_type");
    public static final ResourceKey<Registry<DataComponentType<?>>> AFFIX_EFFECT_COMPONENT_TYPE = register("affix_effect_component_type");
    public static final ResourceKey<Registry<MapCodec<? extends AffixLocationBasedEffect>>> AFFIX_LOCATION_BASED_EFFECT_TYPE = register("affix_location_based_effect_type");
    public static final ResourceKey<Registry<MapCodec<? extends AffixEntityEffect>>> AFFIX_ENTITY_EFFECT_TYPE = register("affix_entity_effect_type");
    public static final ResourceKey<Registry<MapCodec<? extends AffixValueEffect>>> AFFIX_VALUE_EFFECT_TYPE = register("affix_value_effect_type");

    private Keys() {
    }

    private static <T> ResourceKey<Registry<T>> register(String name) {
      return ResourceKey.createRegistryKey(ChampionsUtil.id(name));
    }
  }
}
