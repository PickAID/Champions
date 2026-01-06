package top.theillusivec4.champions.affix.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record PlaySound(List<Holder<SoundEvent>> soundEvents, FloatProvider volume, FloatProvider pitch) implements AffixEntityEffect {
  public static final MapCodec<PlaySound> MAP_CODEC = RecordCodecBuilder.mapCodec(
    i -> i.group(
        ExtraCodecs.compactListCodec(SoundEvent.CODEC, SoundEvent.CODEC.sizeLimitedListOf(255))
          .fieldOf("sound")
          .forGetter(PlaySound::soundEvents),
        FloatProvider.codec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlaySound::volume),
        FloatProvider.codec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlaySound::pitch)
      )
      .apply(i, PlaySound::new)
  );

  @Override
  public void apply(LootContext context, int level, Entity entity, Vec3 origin) {
    ServerLevel serverLevel = context.getLevel();
    if (!entity.isSilent()) {
      RandomSource random = entity.getRandom();
      int index = Mth.clamp(level - 1, 0, this.soundEvents.size() - 1);
      serverLevel.playSound(
        null,
        origin.x(),
        origin.y(),
        origin.z(),
        this.soundEvents.get(index),
        entity.getSoundSource(),
        this.volume.sample(random),
        this.pitch.sample(random)
      );
    }
  }

  @Override
  public MapCodec<? extends AffixEntityEffect> codec() {
    return MAP_CODEC;
  }
}
