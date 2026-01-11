package top.theillusivec4.champions.network.protocol;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.ChampionsClient;
import top.theillusivec4.champions.network.protocol.game.ClientboundChampionBossEventPacket;

/**
 * Common
 */
public final class ClientGamePacketListener {

  public static void register(IEventBus modEventBus) {
    modEventBus.register(new ClientGamePacketListener());
  }

  private ClientGamePacketListener() {
  }

  @SubscribeEvent
  public void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
    PayloadRegistrar registrar = event.registrar(Champions.VERSION);
    registrar
      .playToClient(
        CustomPacketPayloads.CLIENTBOUND_CHAMPION_BOSS_EVENT,
        ClientboundChampionBossEventPacket.STREAM_CODEC,
        this::handleChampionBossEvent
      );
  }

  private void handleChampionBossEvent(ClientboundChampionBossEventPacket packet, IPayloadContext context) {
    ChampionsClient.getInstance().getGui().getChampionHealthOverlay().update(packet);
  }

}
