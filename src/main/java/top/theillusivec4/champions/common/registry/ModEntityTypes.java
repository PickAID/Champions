package top.theillusivec4.champions.common.registry;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.entity.ArcticBulletEntity;
import top.theillusivec4.champions.common.entity.EnkindlingBulletEntity;
import top.theillusivec4.champions.common.util.Utils;

public class ModEntityTypes {
	private static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITIES, Champions.MODID);
	public static final RegistryObject<EntityType<? extends ArcticBulletEntity>> ARCTIC_BULLET = ENTITY_TYPE.register("arctic_bullet", () -> EntityType.Builder.<ArcticBulletEntity>of(ArcticBulletEntity::new, EntityClassification.MISC).sized(2, 2).build(Champions.MODID));
	public static final RegistryObject<EntityType<? extends EnkindlingBulletEntity>> ENKINDLING_BULLET = ENTITY_TYPE.register("enkindling_bullet", () -> EntityType.Builder.<EnkindlingBulletEntity>of(EnkindlingBulletEntity::new, EntityClassification.MISC).sized(2, 2).build(Champions.MODID));

	public static void register(IEventBus bus) {
		ENTITY_TYPE.register(bus);
	}

	public static class Tags {
		public static final ITag.INamedTag<EntityType<?>> IS_ENDER = create("is_ender");
		public static final ITag.INamedTag<EntityType<?>> ALLOW_CHAMPIONS = create("allow_champions");

		private static ITag.INamedTag<EntityType<?>> create(String name) {
			return EntityTypeTags.bind(Utils.getLocation(name).toString());
		}
	}
}
