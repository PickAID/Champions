package top.theillusivec4.champions.affix;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.world.entity.ArcticBullet;
import top.theillusivec4.champions.world.entity.EnkindlingBullet;

import java.util.function.Supplier;

public final class ProjectileTemplates {
  private static final DeferredRegister<MapCodec<? extends ProjectileTemplate>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.PROJECTILE_TEMPLATE_TYPE, ChampionsMod.MOD_ID);
  public static final Supplier<MapCodec<ArrowProvider>> ARROW = register("arrow", () -> ArrowProvider.MAP_CODEC);
  public static final Supplier<MapCodec<ArcticBulletProvider>> ARCTIC_BULLET = register("arctic_bullet", () -> ArcticBulletProvider.MAP_CODEC);
  public static final Supplier<MapCodec<EnkindlingBulletProvider>> ENKINDLING_BULLET = register("enkindling_bullet",() ->  EnkindlingBulletProvider.MAP_CODEC);
  public static final Supplier<MapCodec<ShulkerBulletProvider>> SHULKER_BULLET = register("shulker_bullet", () -> ShulkerBulletProvider.MAP_CODEC);
  public static final Supplier<MapCodec<FireworkRocketProvider>> FIREWORK_ROCKET = register("firework_rocket", () -> FireworkRocketProvider.MAP_CODEC);
  public static final Supplier<MapCodec<SmallFireballProvider>> SMALL_FIREBALL = register("small_fireball",() ->  SmallFireballProvider.MAP_CODEC);
  public static final Supplier<MapCodec<LargeFireballProvider>> LARGE_FIREBALL = register("large_fireball", () -> LargeFireballProvider.MAP_CODEC);
  public static final Supplier<MapCodec<ThrownSplashPotionProvider>> THROWN_SPLASH_POTION = register("thrown_splash_potion", () -> ThrownSplashPotionProvider.MAP_CODEC);
  public static final Supplier<MapCodec<ThrownLingeringPotionProvider>> THROWN_LINGERING_POTION = register("thrown_lingering_potion", () -> ThrownLingeringPotionProvider.MAP_CODEC);

  private ProjectileTemplates() {
  }

  private static <T extends ProjectileTemplate> Supplier<MapCodec<T>> register(String name, Supplier<MapCodec<T>> supplier) {
    return DEFERRED_REGISTER.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }

  public record ArrowProvider() implements ProjectileTemplate {
    public static final ArrowProvider INSTANCE = new ArrowProvider();
    public static final MapCodec<ArrowProvider> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Projectile create(ServerLevel level, Entity source, ItemStack projectileItem) {
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

  public record ArcticBulletProvider() implements ProjectileTemplate {
    public static final ArcticBulletProvider INSTANCE = new ArcticBulletProvider();
    public static final MapCodec<ArcticBulletProvider> MAP_CODEC = MapCodec.unit(ArcticBulletProvider.INSTANCE);

    @Override
    public Projectile create(ServerLevel level, Entity source, ItemStack projectileItem) {
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

  public record EnkindlingBulletProvider() implements ProjectileTemplate {
    public static final EnkindlingBulletProvider INSTANCE = new EnkindlingBulletProvider();
    public static final MapCodec<EnkindlingBulletProvider> MAP_CODEC = MapCodec.unit(EnkindlingBulletProvider.INSTANCE);

    @Override
    public Projectile create(ServerLevel level, Entity source, ItemStack projectileItem) {
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

  public record ShulkerBulletProvider() implements ProjectileTemplate {
    public static final ShulkerBulletProvider INSTANCE = new ShulkerBulletProvider();
    public static final MapCodec<ShulkerBulletProvider> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Projectile create(ServerLevel level, Entity source, ItemStack projectileItem) {
      ShulkerBullet bullet = new ShulkerBullet(EntityType.SHULKER_BULLET, level);
//      bullet.setOwner(source);
//      Vec3 position = source.getBoundingBox().getCenter();
//      bullet.snapTo(position.x, position.y, position.z, bullet.getYRot(), bullet.getXRot());
      bullet.finalTarget = source instanceof Mob mob ? mob.getTarget() : null;
      bullet.currentMoveDirection = Direction.UP;
      bullet.selectNextMoveDirection(Direction.Axis.X);
      return bullet;
    }

    @Override
    public MapCodec<? extends ProjectileTemplate> codec() {
      return MAP_CODEC;
    }
  }

  public record FireworkRocketProvider() implements ProjectileTemplate {
    public static final FireworkRocketProvider INSTANCE = new FireworkRocketProvider();
    public static final MapCodec<FireworkRocketProvider> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Projectile create(ServerLevel level, Entity source, ItemStack projectileItem) {
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

  public record SmallFireballProvider() implements ProjectileTemplate {
    public static final SmallFireballProvider INSTANCE = new SmallFireballProvider();
    public static final MapCodec<SmallFireballProvider> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Projectile create(ServerLevel level, Entity source, ItemStack projectileItem) {
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

  public record LargeFireballProvider() implements ProjectileTemplate {
    public static final LargeFireballProvider INSTANCE = new LargeFireballProvider();
    public static final MapCodec<LargeFireballProvider> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Projectile create(ServerLevel level, Entity source, ItemStack projectileItem) {
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

  public record ThrownSplashPotionProvider() implements ProjectileTemplate {
    public static final ThrownSplashPotionProvider INSTANCE = new ThrownSplashPotionProvider();
    public static final MapCodec<ThrownSplashPotionProvider> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Projectile create(ServerLevel level, Entity source, ItemStack projectileItem) {
      ThrownPotion thrownSplashPotion = new ThrownPotion(
        level,
        source.getX(),
        source.getEyeY() - 0.1f,
        source.getZ()
      );
      thrownSplashPotion.setOwner(source);

      return thrownSplashPotion;
    }

    @Override
    public MapCodec<? extends ProjectileTemplate> codec() {
      return MAP_CODEC;
    }
  }

  public record ThrownLingeringPotionProvider() implements ProjectileTemplate {
    public static final ThrownLingeringPotionProvider INSTANCE = new ThrownLingeringPotionProvider();
    public static final MapCodec<ThrownLingeringPotionProvider> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public Projectile create(ServerLevel level, Entity source, ItemStack projectileItem) {
      ThrownPotion potion = new ThrownPotion(
        level,
        source.getX(),
        source.getEyeY() - 0.1f,
        source.getZ()
      );
      potion.setOwner(source);

      return potion;
    }

    @Override
    public MapCodec<? extends ProjectileTemplate> codec() {
      return MAP_CODEC;
    }
  }
}
