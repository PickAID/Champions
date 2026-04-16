package top.theillusivec4.champions.client.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.theillusivec4.champions.ChampionsClient;
import top.theillusivec4.champions.network.ChampionsBossEventPayload;

public final class ChampionsClientPayloadHandler {
  private ChampionsClientPayloadHandler() {
  }

  public static void handleBossEvent(ChampionsBossEventPayload payload, IPayloadContext context) {
    ChampionsClient.getInstance().getGui().getHealthOverlay().update(payload);
  }

}
