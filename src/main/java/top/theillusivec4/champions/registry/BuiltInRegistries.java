package top.theillusivec4.champions.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.effect.AffixLocationBasedEffect;
import top.theillusivec4.champions.champion.affix.effect.entity.AffixEntityEffect;
import top.theillusivec4.champions.champion.affix.effect.value.AffixValueEffect;
import top.theillusivec4.champions.champion.value.based.lootcontext.FloatLootParamSource;
import top.theillusivec4.champions.champion.value.based.lootcontext.LootContextBasedValue;
import top.theillusivec4.champions.champion.item.ChampionSpawnEgg;
import top.theillusivec4.champions.champion.rank.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class BuiltInRegistries {
  private static @Nullable List<Registry<?>> registries = new ArrayList<>();
  public static final Registry<DataComponentType<?>> AFFIX_EFFECT_COMPONENT_TYPE = simple(Registries.AFFIX_EFFECT_COMPONENT_TYPE);
  public static final Registry<MapCodec<? extends LootContextBasedValue>> LOOT_CONTEXT_BASED_VALUE_TYPE = simple(Registries.LOOT_CONTEXT_BASED_VALUE_TYPE);
  public static final Registry<MapCodec<? extends AffixValueEffect>> AFFIX_VALUE_EFFECT_TYPE = simple(Registries.AFFIX_VALUE_EFFECT_TYPE);
  public static final Registry<MapCodec<? extends AffixEntityEffect>> AFFIX_ENTITY_EFFECT_TYPE = simple(Registries.AFFIX_ENTITY_EFFECT_TYPE);
  public static final Registry<MapCodec<? extends AffixLocationBasedEffect>> AFFIX_LOCATION_BASED_EFFECT_TYPE = simple(Registries.AFFIX_LOCATION_BASED_EFFECT_TYPE);
  public static final Registry<FloatLootParamSource<?>> LOOT_PARAM_FLOAT_SOURCE = simple(Registries.FLOAT_LOOT_PARAM_SOURCE);

  public static void register(IEventBus modEventBus) {
    modEventBus.addListener(NewRegistryEvent.class, event -> {
      Objects.requireNonNull(registries, "注册注册表时，注册表容器异常为Null").forEach(event::register);
      registries = null;
    });

    modEventBus.addListener(DataPackRegistryEvent.NewRegistry.class, event -> {
      event.dataPackRegistry(Registries.AFFIX, Affix.DIRECT_CODEC, Affix.DIRECT_CODEC);
      event.dataPackRegistry(Registries.RANK, Rank.DIRECT_CODEC, Rank.DIRECT_CODEC);
      event.dataPackRegistry(Registries.SPAWN_EGG, ChampionSpawnEgg.DIRECT_CODEC, ChampionSpawnEgg.DIRECT_CODEC);
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
    Registry<T> registry = builder.create();
    Objects.requireNonNull(registries, "创建注册表时，注册表容器异常为null").add(registry);
    return registry;
  }

  private BuiltInRegistries() {
  }
}
