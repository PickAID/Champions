package top.theillusivec4.champions.affix.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.function.Function;

public interface AffixEntityEffect extends AffixLocationBasedEffect {
  Codec<AffixEntityEffect> CODEC = Codec.lazyInitialized(() -> ChampionsRegistries.AFFIX_ENTITY_EFFECT_TYPE.byNameCodec().dispatch(AffixEntityEffect::codec, Function.identity()));

  void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position);

  @Override
  default void onChangedBlock(ServerLevel level, int affixLevel, Entity source, Vec3 origin, boolean becameActive) {
    this.apply(level, affixLevel, source, source, origin);
  }

  MapCodec<? extends AffixEntityEffect> codec();
}
