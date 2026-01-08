package top.theillusivec4.champions.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import top.theillusivec4.champions.client.gui.components.ChampionHealthOverlay;
import top.theillusivec4.champions.mixin.BossHealthOverlayAccessor;

public final class Gui {
  private final ChampionHealthOverlay championHealthOverlay = new ChampionHealthOverlay();

  public Gui() {
  }

  public void renderChampionHealthOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    Minecraft client = Minecraft.getInstance();
    //noinspection ConstantValue
    if (client != null) {
      // 设置起始位置 后续渲染逐行进行
      int events = ((BossHealthOverlayAccessor) client.gui.getBossOverlay()).getEvents().size();
      int baseYOffset = 3;
      int yOffset = 38;
      int y = events == 0 ? 3 : events * 35;
      this.championHealthOverlay.setY(y);
      this.championHealthOverlay.render(guiGraphics, deltaTracker);
    }
  }
}
