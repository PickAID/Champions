package top.theillusivec4.champions.champion.config;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Entity;

public interface ChampionConfigProvider {

  void apply(Entity entity);

  MapCodec<? extends ChampionConfigProvider> codec();
}
