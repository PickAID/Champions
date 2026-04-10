package top.theillusivec4.champions.server.boss;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.champion.ChampionsBossEvent;
import top.theillusivec4.champions.network.ChampionsBossEventPayload;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class ChampionsServerBossEvent extends ChampionsBossEvent {
  public static final ChampionsServerBossEvent EMPTY = new ChampionsServerBossEvent(UUID.randomUUID(), CommonComponents.EMPTY);
  public static final MapCodec<ChampionsServerBossEvent> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    UUIDUtil.CODEC.fieldOf("id").forGetter(ChampionsBossEvent::getId),
    ComponentSerialization.CODEC.fieldOf("name").forGetter(ChampionsBossEvent::getName),
    Codec.FLOAT.fieldOf("progress").forGetter(ChampionsBossEvent::getProgress),
    Codec.INT.fieldOf("level").forGetter(ChampionsBossEvent::getLevel),
    TextColor.CODEC.fieldOf("color").forGetter(ChampionsBossEvent::getColor),
    AffixContainer.MAP_CODEC.codec().optionalFieldOf("affixes", AffixContainer.EMPTY).forGetter(ChampionsBossEvent::getAffixes)
  ).apply(instance, ChampionsServerBossEvent::new));
  private final Set<ServerPlayer> players = Sets.newHashSet();
  private final Set<ServerPlayer> unmodifiablePlayers = Collections.unmodifiableSet(this.players);

  public ChampionsServerBossEvent(UUID id, Component name, float progress, int level, TextColor color, AffixContainer affixes) {
    super(id, name, progress, level, color, affixes);
  }

  public Set<ServerPlayer> getPlayers() {
    return unmodifiablePlayers;
  }

  public ChampionsServerBossEvent(UUID id, Component name) {
    super(id, name);
  }

  @Override
  public void setProgress(float progress) {
    if (this != EMPTY) {
      super.setProgress(progress);
      this.broadcast(ChampionsBossEventPayload::createUpdateProgress);
    }
  }

  @Override
  public void setLevel(int level) {
    if (this != EMPTY) {
      super.setLevel(level);
      this.broadcast(ChampionsBossEventPayload::createUpdateLevel);
    }
  }

  @Override
  public void setColor(TextColor color) {
    if (this != EMPTY) {
      super.setColor(color);
      this.broadcast(ChampionsBossEventPayload::createUpdateColor);
    }
  }

  @Override
  public void setAffixes(AffixContainer affixes) {
    if (this != EMPTY) {
      super.setAffixes(affixes);
      this.broadcast(ChampionsBossEventPayload::createUpdateAffixes);
    }
  }

  public void addPlayer(ServerPlayer player) {
    if (this != EMPTY) {
      if (this.players.add(player)) {
        PacketDistributor.sendToPlayer(player, ChampionsBossEventPayload.createAddPacket(this));
      }
    }
  }

  public void removePlayer(ServerPlayer player) {
    if (this != EMPTY) {
      if (this.players.remove(player)) {
        PacketDistributor.sendToPlayer(player, ChampionsBossEventPayload.createRemovePacket(this));
      }
    }
  }

  public void removeAllPlayers() {
    if (this != EMPTY) {
      this.broadcast(ChampionsBossEventPayload::createRemovePacket);
      this.players.clear();
    }
  }

  private void broadcast(Function<ChampionsServerBossEvent, ChampionsBossEventPayload> factory) {
    ChampionsBossEventPayload payload = factory.apply(this);
    this.players.forEach(player -> PacketDistributor.sendToPlayer(player, payload));
  }

}
