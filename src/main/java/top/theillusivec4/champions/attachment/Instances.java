package top.theillusivec4.champions.attachment;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;

final class Instances {
  public static final ServerBossEvent SERVER_BOSS_EVENT = new ServerBossEvent(Component.literal(""), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.NOTCHED_10);

  private Instances() {
  }
}
