package top.theillusivec4.champions.common.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.effect.entity.AffixEntityEffect;
import top.theillusivec4.champions.api.affix.effect.value.AffixValueEffect;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootContextBasedValue;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.FloatLootParamSource;
import top.theillusivec4.champions.common.util.Utils;

public final class Registries {
  public static final ResourceKey<Registry<Affix>> AFFIX = create("affix");
  public static final ResourceKey<Registry<DataComponentType<?>>> AFFIX_EFFECT_COMPONENT_TYPE = create("affix_effect_component_type");
  public static final ResourceKey<Registry<MapCodec<? extends LootContextBasedValue>>> LOOT_CONTEXT_BASED_VALUE_TYPE = create("loot_context_based_value_type");
  public static final ResourceKey<Registry<MapCodec<? extends AffixValueEffect>>> AFFIX_VALUE_EFFECT_TYPE = create("affix_value_effect_type");
  public static final ResourceKey<Registry<MapCodec<? extends AffixEntityEffect>>> AFFIX_ENTITY_EFFECT_TYPE = create("affix_entity_effect_type");
  public static final ResourceKey<Registry<FloatLootParamSource<?>>> FLOAT_LOOT_PARAM_SOURCE = create("float_loot_param_source");

  private static <T> ResourceKey<Registry<T>> create(String name) {
    return ResourceKey.createRegistryKey(Utils.id(name));
  }

  private Registries() {
  }
}
