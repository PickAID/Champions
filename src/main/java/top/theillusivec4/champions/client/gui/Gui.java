package top.theillusivec4.champions.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.client.gui.components.ChampionHealthOverlay;
import top.theillusivec4.champions.client.util.ClientUtil;
import top.theillusivec4.champions.mixin.BossHealthOverlayAccessor;

import java.util.Objects;

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
    int bossBars = ((BossHealthOverlayAccessor) this.getClient().gui.getBossOverlay()).getEvents().size();
    // baseOffsetY = 3, BossEvent OffsetY = 32
    // 0
    int y = 3 + bossBars * 32;
    this.championHealthOverlay.setY(y);
    this.championHealthOverlay.render(guiGraphics, deltaTracker);
  }

  public ChampionHealthOverlay getChampionHealthOverlay() {
    return championHealthOverlay;
  }

  private Minecraft getClient() {
    return Objects.requireNonNull(Minecraft.getInstance(), () -> {
      LOGGER.error("Minecraft instance is null!");
      return "Minecraft instance is null!";
    });
  }
}
