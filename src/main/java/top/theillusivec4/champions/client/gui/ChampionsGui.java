package top.theillusivec4.champions.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import top.theillusivec4.champions.client.gui.components.ChampionsHealthOverlay;
import top.theillusivec4.champions.mixins.BossHealthOverlayAccessor;

public class ChampionsGui {
  private final ChampionsHealthOverlay healthOverlay = new ChampionsHealthOverlay();

  public ChampionsGui() {
  }

  public void renderHealthOverlay(GuiGraphics graphics, DeltaTracker tracker) {
    // 设置起始位置 后续渲染逐行进行
    int bossBars = ((BossHealthOverlayAccessor) Minecraft.getInstance().gui.getBossOverlay()).getEvents().size();
    // baseOffsetY = 3, BossEvent OffsetY = 32
    // 0
    int y = 3 + bossBars * 32;
    this.healthOverlay.setY(y);
    this.healthOverlay.render(graphics, tracker);
  }

  public ChampionsHealthOverlay getHealthOverlay() {
    return healthOverlay;
  }
}
