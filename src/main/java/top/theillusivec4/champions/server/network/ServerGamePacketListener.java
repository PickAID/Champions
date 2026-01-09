package top.theillusivec4.champions.server.network;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.network.protocol.CustomPacketPayloads;
import top.theillusivec4.champions.network.protocol.game.ClientboundChampionBossEventPacket;

public final class ServerGamePacketListener {
  public static void register(IEventBus modEventBus) {

  }


  private ServerGamePacketListener() {
  }
}
