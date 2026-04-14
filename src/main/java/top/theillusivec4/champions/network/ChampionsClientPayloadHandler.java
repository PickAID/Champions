package top.theillusivec4.champions.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.theillusivec4.champions.ChampionsModClient;

public final class ChampionsClientPayloadHandler {
  private ChampionsClientPayloadHandler() {
  }

  public static void handleBossEvent(ChampionsBossEventPayload payload, IPayloadContext context) {
    ChampionsModClient.getInstance().getGui().getHealthOverlay().update(payload);
  }

}
