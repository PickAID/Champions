package top.theillusivec4.champions.world.entity.affix;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.world.entity.affix.effects.*;
import top.theillusivec4.champions.world.entity.affix.effects.AffixEntityEffect;
import top.theillusivec4.champions.world.entity.affix.effects.AffixValueEffect;
import top.theillusivec4.champions.world.level.storage.loot.parameters.ChampionsLootContextParamSets;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class AffixEffectComponents {
  public static final Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(Codec.lazyInitialized(ChampionsBuiltInRegistries.AFFIX_EFFECT_COMPONENT_TYPE::byNameCodec));
  private static final DeferredRegister<DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.AFFIX_EFFECT_COMPONENT_TYPE, Champions.MOD_ID);
  public static final Supplier<DataComponentType<List<AffixLocationBasedEffect>>> INITIALIZE = register("initialize", builder -> builder.persistent(AffixLocationBasedEffect.CODEC.listOf()));
  public static final Supplier<DataComponentType<List<AffixAttributeEffect>>> ATTRIBUTES = register("attributes", builder -> builder.persistent(AffixAttributeEffect.CODEC.listOf()));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> DAMAGE_PROTECTION = register("damage_protection", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_DAMAGE)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<DamageImmunity>>>> DAMAGE_IMMUNITY = register("damage_immunity", builder -> builder.persistent(ConditionalEffect.validatedListCodec(DamageImmunity.CODEC, ChampionsLootContextParamSets.AFFIXED_DAMAGE)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> KNOCKBACK = register("knockback", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_DAMAGE)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> DAMAGE = register("damage", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_DAMAGE)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> HEAL = register("heal", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_ENTITY)));
  public static final Supplier<DataComponentType<List<TargetedConditionalEffect<AffixEntityEffect>>>> POST_ATTACK = register("post_attack", builder -> builder.persistent(TargetedConditionalEffect.validatedListCodec(AffixEntityEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_DAMAGE)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixEntityEffect>>>> TICK = register("tick", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixEntityEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_ENTITY)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixEntityEffect>>>> TARGET = register("target", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixEntityEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_ENTITY)));

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
    return DEFERRED_REGISTER.register(name, () -> builder.apply(DataComponentType.builder()).build());
  }

  private AffixEffectComponents() {
  }

}
