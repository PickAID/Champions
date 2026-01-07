package top.theillusivec4.champions.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import top.theillusivec4.champions.client.gui.components.ChampionHealthOverlay;

public final class Gui {
  private final ChampionHealthOverlay championHealthOverlay = new ChampionHealthOverlay();

  public Gui() {
  }

  public void renderChampionHealthOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    // 设置起始位置 后续渲染逐行进行
    this.championHealthOverlay.setY(3);
    this.championHealthOverlay.render(guiGraphics, deltaTracker);
  }
}
