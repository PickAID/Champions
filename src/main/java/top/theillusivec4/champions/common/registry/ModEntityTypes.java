package top.theillusivec4.champions.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.entity.ArcticBulletEntity;
import top.theillusivec4.champions.common.entity.EnkindlingBulletEntity;

public class ModEntityTypes {
  private static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Champions.MODID);

  public static final DeferredHolder<EntityType<?>, EntityType<EnkindlingBulletEntity>> ENKINDLING_BULLET = ENTITY_TYPE.register("enkindling_bullet", () -> EntityType.Builder.<EnkindlingBulletEntity>of(EnkindlingBulletEntity::new, MobCategory.MISC).sized(2, 2).build(Keys.ENKINDLING_BULLET));
  public static final DeferredHolder<EntityType<?>, EntityType<ArcticBulletEntity>> ARCTIC_BULLET = ENTITY_TYPE.register("arctic_bullet", () -> EntityType.Builder.<ArcticBulletEntity>of(ArcticBulletEntity::new, MobCategory.MISC).sized(2, 2).build(Keys.ARCTIC_BULLET));

  public static void register(IEventBus bus) {
    ENTITY_TYPE.register(bus);
  }

  static class Keys {
    static ResourceKey<EntityType<?>> ARCTIC_BULLET = ResourceKey.create(ENTITY_TYPE.getRegistryKey(), Champions.getLocation("arctic_bullet"));
    static ResourceKey<EntityType<?>> ENKINDLING_BULLET = ResourceKey.create(ENTITY_TYPE.getRegistryKey(), Champions.getLocation("enkindling_bullet"));
  }
}
