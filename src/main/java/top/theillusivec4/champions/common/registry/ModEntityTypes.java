package top.theillusivec4.champions.common.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.entity.ArcticBulletEntity;
import top.theillusivec4.champions.common.entity.EnkindlingBulletEntity;

public class ModEntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Champions.MODID);
    public static final RegistryObject<EntityType<? extends ArcticBulletEntity>> ARCTIC_BULLET = ENTITY_TYPE.register("arctic_bullet", () -> EntityType.Builder.<ArcticBulletEntity>of(ArcticBulletEntity::new, MobCategory.MISC).sized(2, 2).build(Champions.MODID));
    public static final RegistryObject<EntityType<? extends EnkindlingBulletEntity>> ENKINDLING_BULLET = ENTITY_TYPE.register("enkindling_bullet", () -> EntityType.Builder.<EnkindlingBulletEntity>of(EnkindlingBulletEntity::new, MobCategory.MISC).sized(2, 2).build(Champions.MODID));

    public static void register(IEventBus bus) {
        ENTITY_TYPE.register(bus);
    }

    public static class Tags {
        public static final TagKey<EntityType<?>> IS_ENDER = create("is_ender");
        public static final TagKey<EntityType<?>> ALLOW_CHAMPIONS = create("allow_champions");

        private static TagKey<EntityType<?>> create(String name) {
            return TagKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(), Champions.getLocation(name));
        }
    }
}
