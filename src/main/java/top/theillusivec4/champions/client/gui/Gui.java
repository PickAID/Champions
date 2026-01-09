package top.theillusivec4.champions.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.client.gui.components.ChampionHealthOverlay;
import top.theillusivec4.champions.client.util.ClientUtil;
import top.theillusivec4.champions.mixin.BossHealthOverlayAccessor;

/**
 * 由于加载机制，这里并不能使用从构造函数注入依赖的优雅方式
 */
public final class Gui {
  private static final Logger LOGGER = LogManager.getLogger();
  private final ChampionHealthOverlay championHealthOverlay = new ChampionHealthOverlay();

  public Gui() {
  }

  public void renderChampionHealthOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    // 设置起始位置 后续渲染逐行进行
    int events = ((BossHealthOverlayAccessor) ClientUtil.getClient().gui.getBossOverlay()).getEvents().size();
    // baseOffsetY = 3, BossEvent OffsetY = 38
    int y = events == 0 ? 3 : events * 35;
    this.championHealthOverlay.setY(y);
    this.championHealthOverlay.render(guiGraphics, deltaTracker);
  }

  public ChampionHealthOverlay getChampionHealthOverlay() {
    return championHealthOverlay;
  }
}
