package top.theillusivec4.champions.api.affix.effect.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.api.affix.effect.AffixLocationBasedEffect;
import top.theillusivec4.champions.common.registries.BuiltInRegistries;

import java.util.function.Function;

public interface AffixEntityEffect extends AffixLocationBasedEffect {
  Codec<AffixEntityEffect> CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.AFFIX_ENTITY_EFFECT_TYPE.byNameCodec().dispatch(AffixEntityEffect::codec, Function.identity()));

  void apply(LootContext context, int level, Entity entity, Vec3 origin);

  @Override
  default void onChangedBlock(LootContext context, int level, Entity entity, Vec3 origin, boolean becameActive) {
    this.apply(context, level, entity, origin);
  }

  MapCodec<? extends AffixEntityEffect> codec();
}
