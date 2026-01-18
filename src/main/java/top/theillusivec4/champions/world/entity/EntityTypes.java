package top.theillusivec4.champions.world.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.util.Util;

import java.util.function.Supplier;

public class EntityTypes {
  private static final DeferredRegister<EntityType<?>> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Champions.MODID);

  public static final DeferredHolder<EntityType<?>, EntityType<EnkindlingBulletEntity>> ENKINDLING_BULLET = DEFERRED_REGISTER.register("enkindling_bullet", () -> EntityType.Builder.<EnkindlingBulletEntity>of(EnkindlingBulletEntity::new, MobCategory.MISC).sized(2, 2).build(Keys.ENKINDLING_BULLET));
  //  public static final DeferredHolder<EntityType<?>, EntityType<ArcticBulletEntity>> ARCTIC_BULLET = ENTITY_TYPE.register("arctic_bullet", () -> EntityType.Builder.<ArcticBulletEntity>of(ArcticBulletEntity::new, MobCategory.MISC).sized(2, 2).build(Keys.ARCTIC_BULLET));
  public static final Supplier<EntityType<ArcticBullet>> ARCTIC_BULLET = register("arctic_bullet",  EntityType.Builder.<ArcticBullet>of(ArcticBullet::new, MobCategory.MISC).sized(2, 2));

  public static <T extends Entity> Supplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
    return DEFERRED_REGISTER.register(name, id -> builder.build(ResourceKey.create(Registries.ENTITY_TYPE, id)));
  }

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }

  static class Keys {
    static ResourceKey<EntityType<?>> ARCTIC_BULLET = ResourceKey.create(DEFERRED_REGISTER.getRegistryKey(), Util.id("arctic_bullet"));
    static ResourceKey<EntityType<?>> ENKINDLING_BULLET = ResourceKey.create(DEFERRED_REGISTER.getRegistryKey(), Util.id("enkindling_bullet"));
  }

  public static class Tags {
    public static final TagKey<EntityType<?>> IS_ENDER = create("is_ender");
    public static final TagKey<EntityType<?>> ALLOW_CHAMPIONS = create("allow_champions");

    private static TagKey<EntityType<?>> create(String name) {
      return TagKey.create(Registries.ENTITY_TYPE, Util.id(name));
    }
  }
}
