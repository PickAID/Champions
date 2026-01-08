package top.theillusivec4.champions.champion.provider;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;

public interface ChampionProvider {
  void apply(Entity entity);

  MapCodec<? extends ChampionProvider> codec();
}
