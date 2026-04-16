package top.theillusivec4.champions.world.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.world.entity.projectile.ArcticBullet;
import top.theillusivec4.champions.world.entity.projectile.EnkindlingBullet;

import java.util.function.Supplier;

public class ChampionsEntityTypes {
  private static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Champions.MOD_ID);

public static final Supplier<EntityType<ArcticBullet>> ARCTIC_BULLET = register("arctic_bullet",  EntityType.Builder.<ArcticBullet>of(ArcticBullet::new, MobCategory.MISC).sized(2, 2));
  public static final Supplier<EntityType<EnkindlingBullet>> ENKINDLING_BULLET = register("enkinndling_bullet",  EntityType.Builder.<EnkindlingBullet>of(EnkindlingBullet::new, MobCategory.MISC).sized(2, 2));

  public static <T extends Entity> Supplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, id -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, id)));
  }

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

}
