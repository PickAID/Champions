package top.theillusivec4.champions.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.theillusivec4.champions.particle.ParticleTypes;

import java.util.Random;

public class RankParticle extends SingleQuadParticle {
  private static final Random RANDOM = new Random();
  private final SpriteSet spriteSet;

  public RankParticle(
    ClientLevel clientLevel,
    double pX,
    double pY,
    double pZ,
    double pXSpeed,
    double pYSpeed,
    double pZSpeed,
    SpriteSet pSpriteSet
  ) {
    super(clientLevel, pX, pY, pZ, 0.5D - RANDOM.nextDouble(),
      pYSpeed, 0.5D - RANDOM.nextDouble(), pSpriteSet.first());
    this.spriteSet = pSpriteSet;
    this.yd *= 0.2F;

    if (pXSpeed == 0.0D && pZSpeed == 0.0D) {
      this.xd *= 0.1F;
      this.zd *= 0.1F;
    }
    this.quadSize *= 0.75F;
    this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    this.hasPhysics = false;
    this.setSpriteFromAge(spriteSet);
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;

    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      this.setSpriteFromAge(this.spriteSet);
      this.yd += 0.004D;
      this.move(this.xd, this.yd, this.zd);

      if (this.y == this.yo) {
        this.xd *= 1.1D;
        this.zd *= 1.1D;
      }
      this.xd *= 0.96F;
      this.yd *= 0.96F;
      this.zd *= 0.96F;

      if (this.onGround) {
        this.xd *= 0.7F;
        this.zd *= 0.7F;
      }
    }
  }

  @Override
  public SingleQuadParticle.Layer getLayer() {
    return SingleQuadParticle.Layer.TRANSLUCENT;
  }

  public record Options(int color) implements ParticleOptions {
    public static final MapCodec<Options> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.INT.fieldOf("color").forGetter(Options::color)
    ).apply(instance, Options::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, Options> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.INT, Options::color,
      Options::new
    );

    @Override
    public ParticleType<?> getType() {
      return ParticleTypes.RANK_PARTICLE_TYPE.get();
    }
  }

  public record Provider(SpriteSet spriteSet) implements ParticleProvider<RankParticle.Options> {

    @Override
    public Particle createParticle(
      RankParticle.Options options,
      ClientLevel level,
      double x,
      double y,
      double z,
      double xSpeed,
      double ySpeed,
      double zSpeed,
      RandomSource randomSource
    ) {
      RankParticle particle = new RankParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
      float f = level.getRandom().nextFloat() * 0.5F + 0.35F;
      float red = ARGB.red(options.color()) / 255.0f * f;
      float green = ARGB.green(options.color()) / 255.0f * f;
      float blue = ARGB.blue(options.color()) / 255.0f * f;
      particle.setColor(red, green, blue);
      return particle;
    }
  }
}
