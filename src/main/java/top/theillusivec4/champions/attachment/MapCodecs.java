package top.theillusivec4.champions.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;

final class MapCodecs {
  public static final MapCodec<ServerBossEvent> SERVER_BOSS_EVENT = RecordCodecBuilder.mapCodec(instance -> instance.group(
    ComponentSerialization.CODEC.fieldOf("name").forGetter(ServerBossEvent::getName),
    BossEvent.BossBarColor.CODEC.fieldOf("color").forGetter(BossEvent::getColor),
    BossEvent.BossBarOverlay.CODEC.fieldOf("overlay").forGetter(BossEvent::getOverlay),
    Codec.FLOAT.fieldOf("progress").forGetter(BossEvent::getProgress),
    Codec.BOOL.fieldOf("darken_screen").forGetter(BossEvent::shouldDarkenScreen),
    Codec.BOOL.fieldOf("play_boss_music").forGetter(BossEvent::shouldPlayBossMusic),
    Codec.BOOL.fieldOf("create_world_fog").forGetter(BossEvent::shouldCreateWorldFog)
  ).apply(instance, (component, bossBarColor, bossBarOverlay, progress, darkenScreen, playBossMusic, createWorldFog) -> {
    ServerBossEvent serverBossEvent = new ServerBossEvent(component, bossBarColor, bossBarOverlay);
    serverBossEvent.setProgress(progress);
    serverBossEvent.setDarkenScreen(darkenScreen);
    serverBossEvent.setPlayBossMusic(playBossMusic);
    serverBossEvent.setCreateWorldFog(createWorldFog);
    return serverBossEvent;
  }));

  private MapCodecs() {
  }
}
