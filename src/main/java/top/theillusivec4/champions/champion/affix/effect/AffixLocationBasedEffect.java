package top.theillusivec4.champions.champion.affix.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.registry.BuiltInRegistries;

import java.util.function.Function;

public interface AffixLocationBasedEffect extends LootContextUser {
  Codec<AffixLocationBasedEffect> CODEC = Codec.lazyInitialized(() -> BuiltInRegistries.AFFIX_LOCATION_BASED_EFFECT_TYPE.byNameCodec().dispatch(AffixLocationBasedEffect::codec, Function.identity()));

  void onChangedBlock(LootContext context, int level, Entity entity, Vec3 origin, boolean becameActive);

  MapCodec<? extends AffixLocationBasedEffect> codec();

  default void onDeactivated(LootContext context, int level, Entity entity, Vec3 origin) {
  }
}
