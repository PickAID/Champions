package top.theillusivec4.champions.client.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.theillusivec4.champions.ChampionsModClient;
import top.theillusivec4.champions.network.ChampionsBossEventPayload;

public final class ChampionsClientPayloadHandler {
  private ChampionsClientPayloadHandler() {
  }

  public static void handleBossEvent(ChampionsBossEventPayload payload, IPayloadContext context) {
    ChampionsModClient.getInstance().getGui().getHealthOverlay().update(payload);
  }

}
