package top.theillusivec4.champions.client.event;

import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.champions.ChampionsClient;

public final class ClientEventListener {
//    private final BossHealthOverlayHandler overlayHandler = new BossHealthOverlayHandler();

  private static void register() {
    NeoForge.EVENT_BUS.register(new ClientEventListener());
  }

  private ClientEventListener() {
  }

//    @SubscribeEvent
//    public void onCustomizeGuiOverlayBossProgress(CustomizeGuiOverlayEvent.BossEventProgress event) {
//      if (event.getBossEvent() instanceof ChampionClientBossEvent championClientBossEvent) {
//        GuiGraphics guiGraphics = event.getGuiGraphics();
//        int x = event.getX();
//        int y = event.getY();
//        overlayHandler.render(guiGraphics, x, y, championClientBossEvent);
//        event.setIncrement(event.getIncrement() + 6);
//        event.setCanceled(true);
//      }
//    }

//    @SubscribeEvent
//    public void onRenderNameTagCanRender(RenderNameTagEvent.CanRender event) {
//      ChampionUtil.getHandler(event.getEntity()).ifPresent(handler -> {
//        if (handler.getLevel() >= 1) {
//          event.setCanRender(TriState.TRUE);
//        }
//      });
//    }
//
//    @SubscribeEvent
//    public void onRenderNameTagDoRender(RenderNameTagEvent.DoRender event) {
//      PoseStack poseStack = event.getPoseStack();
//
//    }

}
