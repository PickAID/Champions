package top.theillusivec4.champions.server.level;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;

public class ChampionServerBossEvent extends ServerBossEvent {
  public ChampionServerBossEvent(Component name, BossBarColor color, BossBarOverlay overlay) {
    super(name, color, overlay);
  }
}
