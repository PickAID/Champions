package top.theillusivec4.champions.client.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;
import top.theillusivec4.champions.core.particles.ChampionsParticleTypes;

import java.util.Random;

public class RankParticle extends SingleQuadParticle {
  private static final Random RANDOM = new Random();
  private final SpriteSet spriteSet;
  private TextureAtlasSprite sprite;

  public RankParticle(
    ClientLevel level,
    double x,
    double y,
    double z,
    double xSpeed,
    double ySpeed,
    double zSpeed,
    SpriteSet spriteSet
  ) {
    super(level, x, y, z, 0.5D - RANDOM.nextDouble(),
      ySpeed, 0.5D - RANDOM.nextDouble());
    this.spriteSet = spriteSet;
    this.sprite = spriteSet.get(level.getRandom());
    this.yd *= 0.2F;

    if (xSpeed == 0.0D && zSpeed == 0.0D) {
      this.xd *= 0.1F;
      this.zd *= 0.1F;
    }
    this.quadSize *= 0.75F;
    this.lifetime = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
    this.hasPhysics = false;
    this.setSpriteFromAge();
  }

  private void setSpriteFromAge() {
    if (!this.removed) {
      this.sprite = this.spriteSet.get(this.age, this.lifetime);
    }
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;

    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      this.setSpriteFromAge();
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
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  private TextureAtlasSprite getSprite() {
    return this.sprite;
  }

  @Override
  protected float getU0() {
    return this.getSprite().getU0();
  }

  @Override
  protected float getU1() {
    return this.getSprite().getU1();
  }

  @Override
  protected float getV0() {
    return this.getSprite().getV0();
  }

  @Override
  protected float getV1() {
    return this.getSprite().getV1();
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
      return ChampionsParticleTypes.RANK.get();
    }
  }

  public record Provider(SpriteSet spriteSet) implements ParticleProvider<Options> {

    @Override
    public Particle createParticle(Options options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      RankParticle particle = new RankParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
      float f = level.getRandom().nextFloat() * 0.5F + 0.35F;
      float red = FastColor.ARGB32.red(options.color()) / 255.0f * f;
      float green = FastColor.ARGB32.green(options.color()) / 255.0f * f;
      float blue = FastColor.ARGB32.blue(options.color()) / 255.0f * f;
      particle.setColor(red, green, blue);
      return particle;
    }
  }
}
