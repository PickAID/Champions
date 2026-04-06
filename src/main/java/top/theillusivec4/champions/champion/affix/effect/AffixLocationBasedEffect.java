package top.theillusivec4.champions.champion.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.champion.value.based.lootcontext.LevelBasedValue;
import top.theillusivec4.champions.registries.ChampionsBuiltInRegistries;

import java.util.function.Function;

public interface AffixLocationBasedEffect {
  Codec<AffixLocationBasedEffect> CODEC = ChampionsBuiltInRegistries.AFFIX_LOCATION_BASED_EFFECT_TYPE.byNameCodec().dispatch(AffixLocationBasedEffect::codec, Function.identity());

  static AffixLocationBasedEffect attribute(Identifier id, Holder<Attribute> attribute, LevelBasedValue amount, AttributeModifier.Operation operation) {
    return new AffixAttributeEffect(id, attribute, amount, operation);
  }

  static AffixEntityEffect applyMobEffect(HolderSet<MobEffect> mobEffect, LevelBasedValue minDuration, LevelBasedValue maxDuration, LevelBasedValue minAmplifier, LevelBasedValue maxAmplifier) {
    return new AffixEntityEffects.ApplyMobEffect(mobEffect, minDuration, maxDuration, minAmplifier, maxAmplifier);
  }

  void onChangedBlock(ServerLevel level, int affixLevel, Entity entity, Vec3 origin, boolean becameActive);

  MapCodec<? extends AffixLocationBasedEffect> codec();

  default void onDeactivated(ServerLevel level, int affixLevel, Entity entity, Vec3 origin) {
  }
}
