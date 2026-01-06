package top.theillusivec4.champions.common.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.effect.AffixLocationBasedEffect;
import top.theillusivec4.champions.api.affix.effect.entity.AffixEntityEffect;
import top.theillusivec4.champions.api.affix.effect.value.AffixValueEffect;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.FloatLootParamSource;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootContextBasedValue;

import java.util.function.Consumer;

public final class BuiltInRegistries {
  public static final Registry<DataComponentType<?>> AFFIX_EFFECT_COMPONENT_TYPE = simple(Registries.AFFIX_EFFECT_COMPONENT_TYPE);
  public static final Registry<MapCodec<? extends LootContextBasedValue>> LOOT_CONTEXT_BASED_VALUE_TYPE = simple(Registries.LOOT_CONTEXT_BASED_VALUE_TYPE);
  public static final Registry<MapCodec<? extends AffixValueEffect>> AFFIX_VALUE_EFFECT_TYPE = simple(Registries.AFFIX_VALUE_EFFECT_TYPE);
  public static final Registry<MapCodec<? extends AffixEntityEffect>> AFFIX_ENTITY_EFFECT_TYPE = simple(Registries.AFFIX_ENTITY_EFFECT_TYPE);
  public static final Registry<MapCodec<? extends AffixLocationBasedEffect>> AFFIX_LOCATION_BASED_EFFECT_TYPE = simple(Registries.AFFIX_LOCATION_BASED_EFFECT_TYPE);
  public static final Registry<FloatLootParamSource<?>> LOOT_PARAM_FLOAT_SOURCE = simple(Registries.FLOAT_LOOT_PARAM_SOURCE);

  public static void register(IEventBus modEventBus) {
    modEventBus.addListener(NewRegistryEvent.class, event -> {
      event.register(AFFIX_EFFECT_COMPONENT_TYPE);
      event.register(LOOT_CONTEXT_BASED_VALUE_TYPE);
      event.register(AFFIX_VALUE_EFFECT_TYPE);
      event.register(AFFIX_ENTITY_EFFECT_TYPE);
      event.register(AFFIX_LOCATION_BASED_EFFECT_TYPE);
      event.register(LOOT_PARAM_FLOAT_SOURCE);
    });

    modEventBus.addListener(DataPackRegistryEvent.NewRegistry.class, event -> {
      event.dataPackRegistry(Registries.AFFIX, Affix.DIRECT_CODEC, Affix.DIRECT_CODEC);
    });
  }

  private static <T> Registry<T> simple(ResourceKey<Registry<T>> key) {
    return create(key, builder -> builder.sync(true));
  }

  private static <T> Registry<T> create(ResourceKey<Registry<T>> key) {
    return new RegistryBuilder<>(key).create();
  }

  private static <T> Registry<T> create(ResourceKey<Registry<T>> key, Consumer<RegistryBuilder<T>> consumer) {
    RegistryBuilder<T> builder = new RegistryBuilder<>(key);
    consumer.accept(builder);
    return builder.create();
  }

  private BuiltInRegistries() {
  }
}
