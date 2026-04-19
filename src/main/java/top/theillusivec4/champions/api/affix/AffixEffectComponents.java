package top.theillusivec4.champions.api.affix;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.enchantment.effects.DamageImmunity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.effect.*;
import top.theillusivec4.champions.world.entity.affix.effects.*;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.world.level.storage.loot.parameters.ChampionsLootContextParamSets;
import top.theillusivec4.champions.core.registries.ChampionsBuiltInRegistries;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class AffixEffectComponents {
  public static final Codec<DataComponentMap> CODEC = Codec.lazyInitialized(() -> {
    var codec = ChampionsBuiltInRegistries.AFFIX_EFFECT_COMPONENT_TYPE.byNameCodec();
    return DataComponentMap.makeCodec(codec);
  });
  private static final DeferredRegister<DataComponentType<?>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.AFFIX_EFFECT_COMPONENT_TYPE, Champions.MOD_ID);
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixLocationBasedEffect>>>> LOCATION_CHANGED = register("location_changed", builder -> builder.persistent(ConditionalEffect.codec(AffixLocationBasedEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_ENTITY).listOf()));
  public static final Supplier<DataComponentType<List<AffixAttributeEffect>>> ATTRIBUTES = register("attributes", builder -> builder.persistent(AffixAttributeEffect.CODEC.listOf()));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> DAMAGE_PROTECTION = register("damage_protection", builder -> builder.persistent(ConditionalEffect.codec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_DAMAGE).listOf()));
  public static final Supplier<DataComponentType<List<ConditionalEffect<DamageImmunity>>>> DAMAGE_IMMUNITY = register("damage_immunity", builder -> builder.persistent(ConditionalEffect.codec(DamageImmunity.CODEC, ChampionsLootContextParamSets.AFFIXED_DAMAGE).listOf()));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> KNOCKBACK = register("knockback", builder -> builder.persistent(ConditionalEffect.codec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_DAMAGE).listOf()));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> DAMAGE = register("damage", builder -> builder.persistent(ConditionalEffect.codec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_DAMAGE).listOf()));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixValueEffect>>>> HEAL = register("heal", builder -> builder.persistent(ConditionalEffect.codec(AffixValueEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_ENTITY).listOf()));
  public static final Supplier<DataComponentType<List<TargetedConditionalEffect<AffixEntityEffect>>>> POST_ATTACK = register("post_attack", builder -> builder.persistent(TargetedConditionalEffect.codec(AffixEntityEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_DAMAGE).listOf()));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixEntityEffect>>>> TICK = register("tick", builder -> builder.persistent(ConditionalEffect.codec(AffixEntityEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_ENTITY).listOf()));
  public static final Supplier<DataComponentType<List<ConditionalEffect<AffixEntityEffect>>>> TARGET = register("target", builder -> builder.persistent(ConditionalEffect.codec(AffixEntityEffect.CODEC, ChampionsLootContextParamSets.AFFIXED_ENTITY).listOf()));

  private AffixEffectComponents() {
  }

  private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator) {
    return DEFERRED_REGISTER.register(name, () -> operator.apply(DataComponentType.builder()).build());
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
