package top.theillusivec4.champions.common.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.util.Utils;

public class EntityTypes {
  private static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Champions.MODID);

  public static final DeferredHolder<EntityType<?>, EntityType<EnkindlingBulletEntity>> ENKINDLING_BULLET = ENTITY_TYPE.register("enkindling_bullet", () -> EntityType.Builder.<EnkindlingBulletEntity>of(EnkindlingBulletEntity::new, MobCategory.MISC).sized(2, 2).build(Keys.ENKINDLING_BULLET));
  public static final DeferredHolder<EntityType<?>, EntityType<ArcticBulletEntity>> ARCTIC_BULLET = ENTITY_TYPE.register("arctic_bullet", () -> EntityType.Builder.<ArcticBulletEntity>of(ArcticBulletEntity::new, MobCategory.MISC).sized(2, 2).build(Keys.ARCTIC_BULLET));

  public static void register(IEventBus modEventBus) {
    ENTITY_TYPE.register(modEventBus);
  }

  static class Keys {
    static ResourceKey<EntityType<?>> ARCTIC_BULLET = ResourceKey.create(ENTITY_TYPE.getRegistryKey(), Utils.id("arctic_bullet"));
    static ResourceKey<EntityType<?>> ENKINDLING_BULLET = ResourceKey.create(ENTITY_TYPE.getRegistryKey(), Utils.id("enkindling_bullet"));
  }

  public static class Tags {
    public static final TagKey<EntityType<?>> IS_ENDER = create("is_ender");
    public static final TagKey<EntityType<?>> ALLOW_CHAMPIONS = create("allow_champions");

    private static TagKey<EntityType<?>> create(String name) {
      return TagKey.create(Registries.ENTITY_TYPE, Utils.id(name));
    }
  }
}
