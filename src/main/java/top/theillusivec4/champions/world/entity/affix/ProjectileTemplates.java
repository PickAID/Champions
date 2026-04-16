package top.theillusivec4.champions.world.entity.affix;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownLingeringPotion;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.world.entity.projectile.ArcticBullet;
import top.theillusivec4.champions.world.entity.projectile.EnkindlingBullet;

import java.util.function.Supplier;

public final class ProjectileTemplates {
	private static final DeferredRegister<MapCodec<? extends ProjectileTemplate>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.PROJECTILE_TEMPLATE_TYPE, Champions.MOD_ID);
	public static final Supplier<MapCodec<ArrowTemplate>> ARROW = register("arrow", ArrowTemplate.MAP_CODEC);
	public static final Supplier<MapCodec<ArcticBulletTemplate>> ARCTIC_BULLET = register("arctic_bullet", ArcticBulletTemplate.MAP_CODEC);
	public static final Supplier<MapCodec<EnkindlingBulletTemplate>> ENKINDLING_BULLET = register("enkindling_bullet", EnkindlingBulletTemplate.MAP_CODEC);
	public static final Supplier<MapCodec<ShulkerBulletTemplate>> SHULKER_BULLET = register("shulker_bullet", ShulkerBulletTemplate.MAP_CODEC);
	public static final Supplier<MapCodec<FireworkRocketTemplate>> FIREWORK_ROCKET = register("firework_rocket", FireworkRocketTemplate.MAP_CODEC);
	public static final Supplier<MapCodec<SmallFireballTemplate>> SMALL_FIREBALL = register("small_fireball", SmallFireballTemplate.MAP_CODEC);
	public static final Supplier<MapCodec<LargeFireballTemplate>> LARGE_FIREBALL = register("large_fireball", LargeFireballTemplate.MAP_CODEC);
	public static final Supplier<MapCodec<ThrownSplashPotionTemplate>> THROWN_SPLASH_POTION = register("thrown_splash_potion", ThrownSplashPotionTemplate.MAP_CODEC);
	public static final Supplier<MapCodec<ThrownLingeringPotionTemplate>> THROWN_LINGERING_POTION = register("thrown_lingering_potion", ThrownLingeringPotionTemplate.MAP_CODEC);

	private ProjectileTemplates() {
	}

	public static <T extends ProjectileTemplate> Supplier<MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
		return DEFERRED_REGISTER.register(name, () -> mapCodec);
	}

	public static void register(IEventBus modEventBus) {
		DEFERRED_REGISTER.register(modEventBus);
	}

	public record ArrowTemplate() implements ProjectileTemplate {
		public static final ArrowTemplate INSTANCE = new ArrowTemplate();
		public static final MapCodec<ArrowTemplate> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem) {
			ItemStack weaponItem = source.getWeaponItem();
//      Arrow arrow = new Arrow(
//        EntityType.ARROW,
//        level
//      );
//      return arrow;
			return new Arrow(
					level,
					source.getX(),
					source.getEyeY() - 0.1f,
					source.getZ(),
					projectileItem.copy(),
					weaponItem == null || weaponItem.isEmpty() ? null : weaponItem
			);

		}

		@Override
		public MapCodec<? extends ProjectileTemplate> codec() {
			return MAP_CODEC;
		}
	}

	public record ArcticBulletTemplate() implements ProjectileTemplate {
		public static final ArcticBulletTemplate INSTANCE = new ArcticBulletTemplate();
		public static final MapCodec<ArcticBulletTemplate> MAP_CODEC = MapCodec.unit(ArcticBulletTemplate.INSTANCE);

		@Override
		public Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem) {
			return new ArcticBullet(
					level,
					source
			);
		}

		@Override
		public MapCodec<? extends ProjectileTemplate> codec() {
			return MAP_CODEC;
		}
	}

	public record EnkindlingBulletTemplate() implements ProjectileTemplate {
		public static final EnkindlingBulletTemplate INSTANCE = new EnkindlingBulletTemplate();
		public static final MapCodec<EnkindlingBulletTemplate> MAP_CODEC = MapCodec.unit(EnkindlingBulletTemplate.INSTANCE);

		@Override
		public Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem) {
			return new EnkindlingBullet(
					level,
					source
			);
		}

		@Override
		public MapCodec<? extends ProjectileTemplate> codec() {
			return MAP_CODEC;
		}
	}

	public record ShulkerBulletTemplate() implements ProjectileTemplate {
		public static final ShulkerBulletTemplate INSTANCE = new ShulkerBulletTemplate();
		public static final MapCodec<ShulkerBulletTemplate> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem) {
			ShulkerBullet bullet = new ShulkerBullet(EntityType.SHULKER_BULLET, level);
//      bullet.setOwner(source);
//      Vec3 position = source.getBoundingBox().getCenter();
//      bullet.snapTo(position.x, position.y, position.z, bullet.getYRot(), bullet.getXRot());
			Entity target = source instanceof Mob mob ? mob.getTarget() : null;
			bullet.finalTarget = EntityReference.of(target);
			bullet.currentMoveDirection = Direction.UP;
			bullet.selectNextMoveDirection(Direction.Axis.X, target);
			return bullet;
		}

		@Override
		public MapCodec<? extends ProjectileTemplate> codec() {
			return MAP_CODEC;
		}
	}

	public record FireworkRocketTemplate() implements ProjectileTemplate {
		public static final FireworkRocketTemplate INSTANCE = new FireworkRocketTemplate();
		public static final MapCodec<FireworkRocketTemplate> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem) {
//      return new FireworkRocketEntity(
//        level,
//        source.getX(),
//        source.getEyeY() - 0.1f,
//        source.getZ(),
//        projectileItem
//      );
			return new FireworkRocketEntity(
					EntityType.FIREWORK_ROCKET,
					level
			);
		}

		@Override
		public MapCodec<? extends ProjectileTemplate> codec() {
			return MAP_CODEC;
		}
	}

	public record SmallFireballTemplate() implements ProjectileTemplate {
		public static final SmallFireballTemplate INSTANCE = new SmallFireballTemplate();
		public static final MapCodec<SmallFireballTemplate> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem) {
//      Direction direction = source.getDirection();
//      RandomSource random = level.getRandom();
//      double dirX = random.triangle(direction.getStepX(), 0.11485000000000001);
//      double dirY = random.triangle(direction.getStepY(), 0.11485000000000001);
//      double dirZ = random.triangle(direction.getStepZ(), 0.11485000000000001);
//      Vec3 dir = new Vec3(dirX, dirY, dirZ);
			SmallFireball smallFireball = new SmallFireball(
					EntityType.SMALL_FIREBALL,
					level
			);
//      smallFireball.setOwner(source);
//      smallFireball.setPos(source.getX(), source.getEyeY() - 0.1f, source.getZ());
			return smallFireball;
//      return new SmallFireball(
//        level,
//        source.getX(),
//        source.getEyeY() - 0.1f,
//        source.getZ(),
//        dir.normalize()
//      );
		}

		@Override
		public MapCodec<? extends ProjectileTemplate> codec() {
			return MAP_CODEC;
		}
	}

	public record LargeFireballTemplate() implements ProjectileTemplate {
		public static final LargeFireballTemplate INSTANCE = new LargeFireballTemplate();
		public static final MapCodec<LargeFireballTemplate> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem) {
			LargeFireball largeFireball = new LargeFireball(
					EntityType.FIREBALL,
					level
			);
//      double x = source.getX();
//      double y = source.getEyeY() - 0.1f;
//      double z = source.getZ();
//      Vec3 viewVector = source.getViewVector(1.0F);
//      double xdd = target.getX() - (source.getX() + viewVector.x * 4.0);
//      double ydd = target.getY(0.5) - (0.5 + source.getY(0.5));
//      double zdd = target.getZ() - (source.getZ() + viewVector.z * 4.0);
//      Vec3 direction = new Vec3(xdd, ydd, zdd);
//      largeFireball.snapTo(source.getX(), source.getEyeY() - 0.1f, source.getZ(), largeFireball.getYRot(), largeFireball.getXRot());
//      largeFireball.setOwner(source);
//      largeFireball.setPos(x, y, z);
//      largeFireball.setDeltaMovement(direction.normalize().scale(0.1));
//      largeFireball.needsSync = true;

			return largeFireball;
		}

		@Override
		public MapCodec<? extends ProjectileTemplate> codec() {
			return MAP_CODEC;
		}
	}

	public record ThrownSplashPotionTemplate() implements ProjectileTemplate {
		public static final ThrownSplashPotionTemplate INSTANCE = new ThrownSplashPotionTemplate();
		public static final MapCodec<ThrownSplashPotionTemplate> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem) {
			ThrownSplashPotion thrownSplashPotion = new ThrownSplashPotion(
					level,
					source.getX(),
					source.getEyeY() - 0.1f,
					source.getZ(),
					projectileItem
			);
			thrownSplashPotion.setOwner(source);

			return thrownSplashPotion;
		}

		@Override
		public MapCodec<? extends ProjectileTemplate> codec() {
			return MAP_CODEC;
		}
	}

	public record ThrownLingeringPotionTemplate() implements ProjectileTemplate {
		public static final ThrownLingeringPotionTemplate INSTANCE = new ThrownLingeringPotionTemplate();
		public static final MapCodec<ThrownLingeringPotionTemplate> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public Projectile provide(ServerLevel level, Entity source, ItemStack projectileItem) {
			ThrownLingeringPotion thrownLingeringPotion = new ThrownLingeringPotion(
					level,
					source.getX(),
					source.getEyeY() - 0.1f,
					source.getZ(),
					projectileItem
			);
			thrownLingeringPotion.setOwner(source);

			return thrownLingeringPotion;
		}

		@Override
		public MapCodec<? extends ProjectileTemplate> codec() {
			return MAP_CODEC;
		}
	}
}
