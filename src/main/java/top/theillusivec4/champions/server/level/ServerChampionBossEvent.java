package top.theillusivec4.champions.server.level;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerPlayer;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.network.protocol.game.ClientboundChampionBossEventPacket;
import top.theillusivec4.champions.world.ChampionBossEvent;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class ServerChampionBossEvent extends ChampionBossEvent {
  public static final Codec<ServerChampionBossEvent> CODEC = RecordCodecBuilder.create(instance -> instance.group(UUIDUtil.CODEC.fieldOf("id").forGetter(ServerChampionBossEvent::getId),
    ComponentSerialization.CODEC.fieldOf("name").forGetter(ServerChampionBossEvent::getName),
    Codec.FLOAT.fieldOf("progress").forGetter(ServerChampionBossEvent::getProgress),
    Codec.INT.fieldOf("level").forGetter(ServerChampionBossEvent::getLevel),
    Codec.INT.fieldOf("color").forGetter(ServerChampionBossEvent::getColor),
    Affix.REFERENCE_CODEC.listOf().fieldOf("affixes").forGetter(ServerChampionBossEvent::getAffixes)
  ).apply(instance, ServerChampionBossEvent::new));
  private final Set<ServerPlayer> players = Sets.newHashSet();
  private final Set<ServerPlayer> unmodifiablePlayers = Collections.unmodifiableSet(this.players);

  public ServerChampionBossEvent(UUID id, Component name, float progress, int level, int color, List<Holder<Affix>> affixes) {
    super(id, name, progress, level, color, affixes);
  }

  public ServerChampionBossEvent(UUID id, Component name, int color) {
    super(id, name);
  }

  @Override
  public void setName(Component name) {
    super.setName(name);
    this.broadcast(ClientboundChampionBossEventPacket::createUpdateName);
  }

  @Override
  public void setProgress(float progress) {
    super.setProgress(progress);
    this.broadcast(ClientboundChampionBossEventPacket::createUpdateProgress);
  }

  @Override
  public void setLevel(int level) {
    super.setLevel(level);
    this.broadcast(ClientboundChampionBossEventPacket::createUpdateLevel);
  }

  @Override
  public void setColor(int color) {
    super.setColor(color);
    this.broadcast(ClientboundChampionBossEventPacket::createUpdateColor);
  }

  @Override
  public void setAffixes(List<Holder<Affix>> affixes) {
    super.setAffixes(affixes);
    this.broadcast(ClientboundChampionBossEventPacket::createUpdateAffixes);
  }

  public void addPlayer(ServerPlayer player) {
    if (this.players.add(player)) {
      player.connection.send(ClientboundChampionBossEventPacket.createAddPacket(this));
    }
  }

  public void removeAllPlayers() {
    if (!this.players.isEmpty()) {
      for (ServerPlayer player : this.players) {
        player.connection.send(ClientboundChampionBossEventPacket.createRemovePacket(this));
      }
    }
  }

  public void removePlayer(ServerPlayer player) {
    if (this.players.remove(player)) {
      player.connection.send(ClientboundChampionBossEventPacket.createRemovePacket(this));
    }
  }

  public Set<ServerPlayer> getPlayers() {
    return unmodifiablePlayers;
  }

  private void broadcast(Function<ServerChampionBossEvent, ClientboundChampionBossEventPacket> factory) {
    ClientboundChampionBossEventPacket packet = factory.apply(this);
    this.players.forEach(serverPlayer -> serverPlayer.connection.send(packet));
  }
}
