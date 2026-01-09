package top.theillusivec4.champions.network.protocol;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.network.protocol.game.ClientboundChampionBossEventPacket;
import top.theillusivec4.champions.util.Utils;

public final class CustomPacketPayloads {
  public static final CustomPacketPayload.Type<ClientboundChampionBossEventPacket> CLIENTBOUND_CHAMPION_BOSS_EVENT = register("clientbound_champion_event");

  public static void register(IEventBus modEventBus) {
//    modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
//      PayloadRegistrar registrar = event.registrar(Champions.VERSION);
//      registrar
//        .playToClient(
//          CLIENTBOUND_CHAMPION_BOSS_EVENT,
//          ClientboundChampionBossEventPacket.STREAM_CODEC,
//          (payload, context) ->
//        );
//    });
  }

  private static <T extends CustomPacketPayload> CustomPacketPayload.Type<T> register(String name) {
    return new CustomPacketPayload.Type<>(Utils.id(name));
  }

  private CustomPacketPayloads() {
  }
}
