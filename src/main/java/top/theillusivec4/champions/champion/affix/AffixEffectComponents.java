package top.theillusivec4.champions.champion.affix;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.affix.effect.*;
import top.theillusivec4.champions.champion.affix.effect.AffixEntityEffect;
import top.theillusivec4.champions.champion.affix.effect.AffixValueEffect;
import top.theillusivec4.champions.world.loot.parameters.ChampionsLootContextParamSets;
import top.theillusivec4.champions.registries.ChampionsBuiltInRegistries;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class AffixEffectComponents {
  public static final Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(Codec.lazyInitialized(ChampionsBuiltInRegistries.AFFIX_EFFECT_COMPONENT_TYPE::byNameCodec));
  private static final DeferredRegister<DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.AFFIX_EFFECT_COMPONENT_TYPE, Champions.MODID);
  public static final Supplier<DataComponentType<List<AffixLocationBasedEffect>>> INITIALIZE = register("initialize", builder -> builder.persistent(AffixLocationBasedEffect.CODEC.listOf()));
  public static final Supplier<DataComponentType<List<AffixAttributeEffect>>> ATTRIBUTES = register("attributes", builder -> builder.persistent(AffixAttributeEffect.CODEC.listOf()));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> DAMAGE_PROTECTION = register("damage_protection", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.DAMAGE_PROTECTION)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<DamageImmunity>>>> DAMAGE_IMMUNITY = register("damage_immunity", builder -> builder.persistent(ConditionalEffect.validatedListCodec(DamageImmunity.CODEC, ChampionsLootContextParamSets.DAMAGE_PROTECTION)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> KNOCKBACK = register("knockback", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.KNOCKBACK)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> DAMAGE = register("damage", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.DAMAGE)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> HEAL = register("heal", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.HEAL)));
  public static final Supplier<DataComponentType<List<TargetedConditionalEffect<AffixEntityEffect>>>> POST_ATTACK = register("post_attack", builder -> builder.persistent(TargetedConditionalEffect.validatedListCodec(AffixEntityEffect.CODEC, ChampionsLootContextParamSets.POST_ATTACK)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixEntityEffect>>>> TICK = register("tick", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixEntityEffect.CODEC, ChampionsLootContextParamSets.TICK)));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixEntityEffect>>>> TARGET = register("target", builder -> builder.persistent(ConditionalEffect.validatedListCodec(AffixEntityEffect.CODEC, ChampionsLootContextParamSets.TICK)));

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
    return DEFERRED_REGISTER.register(name, () -> builder.apply(DataComponentType.builder()).build());
  }

  private AffixEffectComponents() {
  }

}
