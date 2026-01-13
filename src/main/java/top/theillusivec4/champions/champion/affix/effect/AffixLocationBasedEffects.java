package top.theillusivec4.champions.champion.affix.effect;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.affix.effect.entity.ApplyMobEffect;
import top.theillusivec4.champions.champion.value.based.lootcontext.LootContextBasedValue;
import top.theillusivec4.champions.registry.Registries;

import java.util.function.Supplier;

public final class AffixLocationBasedEffects {
  private static final DeferredRegister<MapCodec<? extends AffixLocationBasedEffect>> DEFERRED_REGISTER = DeferredRegister.create(Registries.AFFIX_LOCATION_BASED_EFFECT_TYPE, Champions.MODID);
  public static final DeferredHolder<MapCodec<? extends AffixLocationBasedEffect>, MapCodec<AttributeEffect>> ATTRIBUTE = register("attribute", AttributeEffect.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends AffixLocationBasedEffect>, MapCodec<ApplyMobEffect>> APPLY_MOB_EFFECT = register("apply_mob_effect", ApplyMobEffect.MAP_CODEC);

  public static AffixLocationBasedEffect attribute(Identifier id, Holder<Attribute> attribute, LootContextBasedValue amount, AttributeModifier.Operation operation) {
    return new AttributeEffect(id, attribute, amount, operation);
  }

  public static AffixLocationBasedEffect applyMobEffect(Holder<MobEffect> toApply, LootContextBasedValue duration, LootContextBasedValue amplifier, boolean infinite) {
    return new ApplyMobEffect(toApply, duration, amplifier, infinite);
  }

  public static <T extends AffixLocationBasedEffect> DeferredHolder<MapCodec<? extends AffixLocationBasedEffect>, MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
    return DEFERRED_REGISTER.register(name, () -> mapCodec);
  }

  public static <T extends AffixLocationBasedEffect> DeferredHolder<MapCodec<? extends AffixLocationBasedEffect>, MapCodec<T>> register(String name, Supplier<MapCodec<T>> mapCodecSupplier) {
    return DEFERRED_REGISTER.register(name, mapCodecSupplier);
  }

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  private AffixLocationBasedEffects() {
  }
}
