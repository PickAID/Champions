package top.theillusivec4.champions.affix.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.registries.ChampionsBuiltInRegistries;

import java.util.function.Function;

public interface AffixLocationBasedEffect {
  Codec<AffixLocationBasedEffect> CODEC = Codec.lazyInitialized(() -> ChampionsBuiltInRegistries.AFFIX_LOCATION_BASED_EFFECT_TYPE.byNameCodec().dispatch(AffixLocationBasedEffect::codec, Function.identity()));

  void onChangedBlock(ServerLevel level, int affixLevel, Entity entity, Vec3 origin, boolean becameActive);

  MapCodec<? extends AffixLocationBasedEffect> codec();

  default void onDeactivated(ServerLevel level, int affixLevel, Entity entity, Vec3 origin) {
  }
}
