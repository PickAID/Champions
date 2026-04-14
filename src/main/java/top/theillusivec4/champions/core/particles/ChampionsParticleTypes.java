package top.theillusivec4.champions.core.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.NonNull;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.client.particle.RankParticle;

public class ChampionsParticleTypes {
  private static final DeferredRegister<ParticleType<?>> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ChampionsMod.MOD_ID);
  public static final DeferredHolder<ParticleType<?>, ParticleType<RankParticle.Options>> CHAMPION = register("champion", false, RankParticle.Options.MAP_CODEC, RankParticle.Options.STREAM_CODEC);

  public static ParticleOptions champion(int color) {
    return new RankParticle.Options(color);
  }

  public static <T extends ParticleOptions> DeferredHolder<ParticleType<?>, ParticleType<T>> register(String name, boolean overrideLimiter, MapCodec<T> mapCodec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
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

  public static void register(IEventBus modEventBus) {
    DEFERRED_REGISTER.register(modEventBus);
  }
}
