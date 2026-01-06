package top.theillusivec4.champions.api.affix.effect.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;

public record SpawnParticlesEffect(
  ParticleOptions particle,
  int count,
  PositionSource horizontalPosition,
  PositionSource verticalPosition,
  VelocitySource horizontalVelocity,
  VelocitySource verticalVelocity,
  FloatProvider speed
) implements AffixEntityEffect {
  public static final MapCodec<SpawnParticlesEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    ParticleTypes.CODEC.fieldOf("particle").forGetter(SpawnParticlesEffect::particle),
    Codec.INT.optionalFieldOf("count", 0).forGetter(SpawnParticlesEffect::count),
    PositionSource.MAP_CODEC.fieldOf("horizontal_position").forGetter(SpawnParticlesEffect::horizontalPosition),
    PositionSource.MAP_CODEC.fieldOf("vertical_position").forGetter(SpawnParticlesEffect::verticalPosition),
    VelocitySource.MAP_CODEC.fieldOf("horizontal_velocity").forGetter(SpawnParticlesEffect::horizontalVelocity),
    VelocitySource.MAP_CODEC.fieldOf("vertical_velocity").forGetter(SpawnParticlesEffect::verticalVelocity),
    FloatProvider.CODEC.optionalFieldOf("speed", ConstantFloat.ZERO).forGetter(SpawnParticlesEffect::speed)
  ).apply(instance, SpawnParticlesEffect::new));

  @Override
  public void apply(LootContext context, int level, Entity entity, Vec3 origin) {
    ServerLevel serverLevel = context.getLevel();
    RandomSource random = entity.getRandom();
    Vec3 movement = entity.getKnownMovement();
    float bbWidth = entity.getBbWidth();
    float bbHeight = entity.getBbHeight();
    serverLevel.sendParticles(
      this.particle,
      this.horizontalPosition.getCoordinate(origin.x(), origin.x(), bbWidth, random),
      this.verticalPosition.getCoordinate(origin.y(), origin.y() + bbHeight / 2.0F, bbHeight, random),
      this.horizontalPosition.getCoordinate(origin.z(), origin.z(), bbWidth, random),
      this.count,
      this.horizontalVelocity.getVelocity(movement.x(), random),
      this.verticalVelocity.getVelocity(movement.y(), random),
      this.horizontalVelocity.getVelocity(movement.z(), random),
      this.speed.sample(random)
    );
  }

  @Override
  public MapCodec<? extends AffixEntityEffect> codec() {
    return MAP_CODEC;
  }

  public enum PositionSourceType implements StringRepresentable {
    ENTITY_POSITION("entity_position", (pos, center, bbSpan, random) -> pos),
    BOUNDING_BOX("in_bounding_box", (pos, center, bbSpan, random) -> center + (random.nextDouble() - 0.5) * bbSpan);
    public static final Codec<PositionSourceType> CODEC = StringRepresentable.fromEnum(PositionSourceType::values);

    private final String name;
    private final CoordinateSource source;

    PositionSourceType(String name, CoordinateSource source) {
      this.name = name;
      this.source = source;
    }

    public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) {
      return this.source.getCoordinate(position, center, boundingBoxSpan, random);
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    @FunctionalInterface
    private interface CoordinateSource {
      double getCoordinate(double pos, double center, float boundingBoxSpan, RandomSource random);
    }
  }

  public record PositionSource(PositionSourceType type, float offset, float scale) {
    public static final MapCodec<PositionSource> MAP_CODEC = RecordCodecBuilder.<PositionSource>mapCodec(instance -> instance.group(
      PositionSourceType.CODEC.fieldOf("type").forGetter(PositionSource::type),
      Codec.FLOAT.optionalFieldOf("offset", 0.0f).forGetter(PositionSource::offset),
      Codec.FLOAT.optionalFieldOf("scale", 1.0f).forGetter(PositionSource::scale)
    ).apply(instance, PositionSource::new)).validate(source -> {
      if (source.type == PositionSourceType.ENTITY_POSITION && source.scale() != 1.0f) {
        return DataResult.error(() -> "Cannot scale an entity position coordinate source");
      }

      return DataResult.success(source);
    });

    public static PositionSource inBoundingBox() {
      return inBoundingBox(0.0f);
    }

    public static PositionSource inBoundingBox(float offset) {
      return inBoundingBox(offset, 1.0f);
    }

    public static PositionSource inBoundingBox(float offset, float scale) {
      return new PositionSource(PositionSourceType.BOUNDING_BOX, offset, scale);
    }

    public static PositionSource entityPosition() {
      return entityPosition(0.0f);
    }

    public static PositionSource entityPosition(float offset) {
      return new PositionSource(PositionSourceType.BOUNDING_BOX, offset, 1.0f);
    }

    public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) {
      return this.type.getCoordinate(position, center, boundingBoxSpan * this.scale, random) + this.offset;
    }
  }

  public record VelocitySource(float movementScale, FloatProvider base) {
    public static final VelocitySource INSTANCE = new VelocitySource(0.0f, ConstantFloat.ZERO);
    public static final MapCodec<VelocitySource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.FLOAT.optionalFieldOf("movement_scale", 0.0f).forGetter(VelocitySource::movementScale),
      FloatProvider.CODEC.optionalFieldOf("base", ConstantFloat.ZERO).forGetter(VelocitySource::base)
    ).apply(instance, VelocitySource::new));

    public static VelocitySource create(float movementScale) {
      return create(movementScale, ConstantFloat.ZERO);
    }

    public static VelocitySource create(float movementScale, FloatProvider base) {
      return new VelocitySource(movementScale, base);
    }

    public double getVelocity(double movement, RandomSource random) {
      return movement * this.movementScale + this.base.sample(random);
    }
  }
}
