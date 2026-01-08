package top.theillusivec4.champions.champion.affix;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.Validatable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.affix.effect.*;
import top.theillusivec4.champions.champion.affix.effect.entity.AffixEntityEffect;
import top.theillusivec4.champions.champion.affix.effect.value.AffixValueEffect;
import top.theillusivec4.champions.world.loot.parameters.LootContextParamSets;
import top.theillusivec4.champions.registry.BuiltInRegistries;
import top.theillusivec4.champions.registry.Registries;

import java.util.List;
import java.util.function.UnaryOperator;

public final class AffixEffectComponents {
  public static final Codec<DataComponentMap> CODEC = DataComponentMap.makeCodec(Codec.lazyInitialized(BuiltInRegistries.AFFIX_EFFECT_COMPONENT_TYPE::byNameCodec));
  private static final DeferredRegister<DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.AFFIX_EFFECT_COMPONENT_TYPE, Champions.MODID);
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> DAMAGE_PROTECTION = register("damage_protection", builder -> builder.persistent(validatedConditionalWithEletmentListCodec(AffixValueEffect.CODEC, LootContextParamSets.DAMAGE_PROTECTION)));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<DamageImmunity>>>> DAMAGE_IMMUNITY = register("damage_immunity", builder -> builder.persistent(validatedConditionalListCodec(DamageImmunity.CODEC, LootContextParamSets.DAMAGE_PROTECTION)));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> KNOCKBACK = register("knockback", builder -> builder.persistent(validatedConditionalWithEletmentListCodec(AffixValueEffect.CODEC, LootContextParamSets.KNOCKBACK)));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> DAMAGE = register("damage", builder -> builder.persistent(validatedConditionalWithEletmentListCodec(AffixValueEffect.CODEC, LootContextParamSets.DAMAGE)));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> HEAL = register("heal", builder -> builder.persistent(validatedConditionalWithEletmentListCodec(AffixValueEffect.CODEC, LootContextParamSets.HEAL)));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<TargetedConditionalEffect<AffixEntityEffect>>>> POST_ATTACK = register("post_attack", builder -> builder.persistent(validatedTargetedConditionalWithEletmentListCodec(AffixEntityEffect.CODEC, LootContextParamSets.POST_ATTACK)));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ConditionalEffect<AffixEntityEffect>>>> TICK = register("tick", builder -> builder.persistent(validatedConditionalWithEletmentListCodec(AffixEntityEffect.CODEC, LootContextParamSets.TICK)));
  public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<AffixLocationBasedEffect>>> INITIALIZE = register("initialize", builder -> builder.persistent(validatedListCodec(AffixLocationBasedEffect.CODEC, LootContextParamSets.LOCATION)));

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private static <T extends Validatable> Codec<List<TargetedConditionalEffect<T>>> validatedTargetedConditionalWithEletmentListCodec(Codec<T> elementCodec, ContextKeySet paramSet) {
    return TargetedConditionalEffect.validatedListCodec(elementCodec.validate(Validatable.validatorForContext(paramSet)), paramSet);
  }

  private static <T extends Validatable> Codec<List<ConditionalEffect<T>>> validatedConditionalWithEletmentListCodec(Codec<T> elementCodec, ContextKeySet paramSet) {
    return ConditionalEffect.validatedListCodec(elementCodec.validate(Validatable.validatorForContext(paramSet)), paramSet);
  }

  private static <T> Codec<List<ConditionalEffect<T>>> validatedConditionalListCodec(Codec<T> elementCodec, ContextKeySet paramSet) {
    return ConditionalEffect.validatedListCodec(elementCodec, paramSet);
  }

  private static <T extends Validatable> Codec<List<T>> validatedListCodec(Codec<T> elementCodec, ContextKeySet paramSet) {
    return elementCodec.listOf().validate(Validatable.listValidatorForContext(paramSet));
  }

  private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
    return DEFERRED_REGISTER.register(name, () -> builder.apply(DataComponentType.builder()).build());
  }

  private AffixEffectComponents() {
  }

}
