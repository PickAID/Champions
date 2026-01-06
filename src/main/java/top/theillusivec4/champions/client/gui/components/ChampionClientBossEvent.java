package top.theillusivec4.champions.client.gui.components;

import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class ChampionClientBossEvent extends LerpingBossEvent {
  public ChampionClientBossEvent(UUID id, Component name, float progress, BossBarColor color, BossBarOverlay overlay, boolean darkenScreen, boolean playMusic, boolean createWorldFog) {
    super(id, name, progress, color, overlay, darkenScreen, playMusic, createWorldFog);
  }
}
