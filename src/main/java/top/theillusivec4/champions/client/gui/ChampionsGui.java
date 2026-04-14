package top.theillusivec4.champions.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.champions.client.gui.components.ChampionsHealthOverlay;
import top.theillusivec4.champions.mixin.BossHealthOverlayAccessor;

import java.util.Objects;

public final class ChampionsGui {
	private static final Logger LOGGER = LogManager.getLogger();
	private final ChampionsHealthOverlay healthOverlay = new ChampionsHealthOverlay();

	public ChampionsGui() {
	}

	public void renderChampionHealthOverlay(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
		// 设置起始位置 后续渲染逐行进行
		int bossBars = ((BossHealthOverlayAccessor) this.getClient().gui.getBossOverlay()).getEvents().size();
		// baseOffsetY = 3, BossEvent OffsetY = 32
		// 0
		int y = 3 + bossBars * 32;
		this.healthOverlay.setY(y);
		this.healthOverlay.render(guiGraphics, deltaTracker);
	}

	public ChampionsHealthOverlay getHealthOverlay() {
		return healthOverlay;
	}

	private Minecraft getClient() {
		return Objects.requireNonNull(Minecraft.getInstance(), () -> {
			LOGGER.error("Minecraft instance is null!");
			return "Minecraft instance is null!";
		});
	}
}
