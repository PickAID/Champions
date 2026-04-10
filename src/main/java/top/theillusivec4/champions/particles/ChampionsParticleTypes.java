package top.theillusivec4.champions.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.checkerframework.checker.nullness.qual.NonNull;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.client.particle.RankParticle;

import java.util.function.Supplier;

public final class ChampionsParticleTypes {
  private static final DeferredRegister<ParticleType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.PARTICLE_TYPE, ChampionsMod.MOD_ID);
  public static Supplier<ParticleType<RankParticle.Options>> RANK = register("rank", true, RankParticle.Options.MAP_CODEC, RankParticle.Options.STREAM_CODEC);

  private ChampionsParticleTypes() {
  }

  public static ParticleOptions champion(int color) {
    return new RankParticle.Options(color);
  }

  private static <T extends ParticleOptions> Supplier<ParticleType<T>> register(String name, boolean overrideLimiter, MapCodec<T> mapCodec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
    return DEFERRED_REGISTER.register(name, () -> new ParticleType<>(overrideLimiter) {
      @Override
      public @NonNull MapCodec<T> codec() {
        return mapCodec;
      }

      @Override
      public @NonNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
      }
    });
  }

  public static void register(IEventBus bus) {
    DEFERRED_REGISTER.register(bus);
  }
}
