package top.theillusivec4.champions.affix;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.champions.registries.ChampionsRegistries;

import java.util.function.Function;

public interface ProjectileTemplate {
  Codec<ProjectileTemplate> CODEC = Codec.lazyInitialized(() -> ChampionsRegistries.PROJECTILE_TEMPLATE_TYPE.byNameCodec().dispatch(ProjectileTemplate::codec, Function.identity()));

  Projectile create(ServerLevel level, Entity source, ItemStack item);

  MapCodec<? extends ProjectileTemplate> codec();
}
