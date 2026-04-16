package top.theillusivec4.champions.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.theillusivec4.champions.ChampionsClient;

public final class ChampionsClientPayloadHandler {
  private ChampionsClientPayloadHandler() {
  }

  public static void handleBossEvent(ChampionsBossEventPayload payload, IPayloadContext context) {
    ChampionsClient.getInstance().getGui().getHealthOverlay().update(payload);
  }

}
