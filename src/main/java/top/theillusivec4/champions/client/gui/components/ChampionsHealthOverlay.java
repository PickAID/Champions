package top.theillusivec4.champions.client.gui.components;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.client.ChampionsClientConfig;
import top.theillusivec4.champions.client.util.ClientUtil;
import top.theillusivec4.champions.network.ChampionsBossEventPayload;
import top.theillusivec4.champions.util.ChampionsUtil;
import top.theillusivec4.champions.world.entity.affix.AffixHelper;
import top.theillusivec4.champions.world.entity.affix.EntityAffixes;
import top.theillusivec4.champions.world.entity.champion.property.ChampionPropertyHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ChampionsHealthOverlay {
	private static final Identifier BAR = ChampionsUtil.id("textures/gui/bars.png");
	private static final Identifier STAR = ChampionsUtil.id("textures/gui/star.png");
	private final Map<UUID, ChampionsClientBossEvent> events = new HashMap<>();
	private final Handler handler = new Handler();
	private int x;
	private int y;

	public ChampionsHealthOverlay() {
	}

	public void update(ChampionsBossEventPayload payload) {
		payload.dispatch(
				payload.id(),
				this.handler
		);
	}

	public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		// 准星处的实体。
		if (ChampionsClientConfig.DISPLAY_HEALTH_OVERLAY_ON_TARGET.get()) {

			Entity entity = ClientUtil.getMouseEntity(deltaTracker.getGameTimeDeltaTicks());
			if (entity != null) {
				if (entity instanceof LivingEntity livingEntity && !ChampionPropertyHelper.isBoss(entity)) {
					ChampionsClientBossEvent event = new ChampionsClientBossEvent(entity.getUUID(), ChampionPropertyHelper.getDisplayName(entity));
					event.setTier(ChampionPropertyHelper.getTier(entity));
					event.setColor(ChampionPropertyHelper.getColor(entity));
					event.setProgress(Math.clamp(livingEntity.getHealth() / livingEntity.getMaxHealth(), 0.0f, 1.0f));
					event.setAffixes(AffixHelper.get(entity));
					this.render(graphics, event);
				}
			}
		}

		// 独立的BossBar。
		for (ChampionsClientBossEvent event : this.events.values()) {
			this.render(graphics, event);
			if (this.y >= graphics.guiHeight() / 3) {
				break;
			}
		}
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	private Minecraft getClient() {
		return Minecraft.getInstance();
	}

	private void render(GuiGraphicsExtractor guiGraphics, ChampionsClientBossEvent event) {
		Component name = event.getName();
    /*
    minecraft.font.lineHeight = 9
    Y坐标：先渲染，然后向下偏移自身所占的Y大小
     */
		int startX;
		int startY = y;
		// 显示等级
		if (event.getTier() <= 9) {
			// 小于5的等级⭐⭐⭐⭐⭐
			startX = guiGraphics.guiWidth() / 2 - 5 - 5 * (event.getTier() - 1);
			this.drawStar(guiGraphics, startX, startY, event);
		} else {
			// 处理过高的等级 ⭐x6
			startX = guiGraphics.guiWidth() / 2 - 5;
			String msg = "x" + event.getTier();
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, STAR, startX - this.getClient().font.width(msg) / 2, startY, 0, 0, 9, 9, 9, 9, event.getColor().getValue());
			startX = startX + 10 - this.getClient().font.width(msg) / 2;
			guiGraphics.text(this.getClient().font, msg, startX, startY, -1, true);
		}
		startX = guiGraphics.guiWidth() / 2 - this.getClient().font.width(name) / 2;
		startY += 12; // 星星的
		// 显示名称
		this.drawString(guiGraphics, startX, startY, event);
		startX = guiGraphics.guiWidth() / 2 - 91;
		startY += 9; // 名称的
		// 显示BossBar。
		this.drawBar(guiGraphics, startX, startY, event);
		startX = guiGraphics.guiWidth() / 2 - this.getClient().font.width(event.getAffixesComponent()) / 2;
		startY += 6; // BossBar的
		// 显示词缀
		this.drawAffixes(guiGraphics, startX, startY, event);
		this.y = startY + 9 + 3; // 词缀的 基础的
	}

	private void drawAffixes(GuiGraphicsExtractor guiGraphics, int x, int y, ChampionsClientBossEvent event) {
		Component name = event.getAffixesComponent();
		guiGraphics.text(this.getClient().font, name, x, y, -1, true);
	}

	private void drawString(GuiGraphicsExtractor guiGraphics, int x, int y, ChampionsClientBossEvent event) {
		Component name = event.getName();
		guiGraphics.text(this.getClient().font, name, x, y, event.getColor().getValue(), true);
	}

	private void drawStar(GuiGraphicsExtractor guiGraphics, int x, int y, ChampionsClientBossEvent event) {
		for (int i = 0; i < event.getTier(); i++) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, STAR, x, y, 0, 0, 9, 9, 9, 9, event.getColor().getValue());
			x += 10;
		}
	}

	private void drawBar(GuiGraphicsExtractor guiGraphics, int x, int y, ChampionsClientBossEvent event) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BAR, x, y, 0, 60, 182, 5, 256, 256, event.getColor().getValue());
		int width = Mth.lerpDiscrete(event.getProgress(), 0, 182);
		if (width > 0) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BAR, x, y, 0, 65, width, 5, 256, 256, event.getColor().getValue());
		}
	}

	private class Handler implements ChampionsBossEventPayload.Handler {

		@Override
		public void add(UUID id, Component name, float progress, int level, TextColor color, EntityAffixes affixes) {
			ChampionsClientBossEvent event = new ChampionsClientBossEvent(id, name, progress, level, color, affixes);
			ChampionsHealthOverlay.this.events.put(id, event);
		}

		@Override
		public void remove(UUID id) {
			ChampionsHealthOverlay.this.events.remove(id);
		}

		@Override
		public void updateProgress(UUID id, float progress) {
			ChampionsClientBossEvent event = ChampionsHealthOverlay.this.events.get(id);
			if (event != null) {
				event.setProgress(progress);
			}
		}

		@Override
		public void updateLevel(UUID id, int level) {
			ChampionsClientBossEvent event = ChampionsHealthOverlay.this.events.get(id);
			if (event != null) {
				event.setTier(level);
			}
		}

		@Override
		public void updateColor(UUID id, TextColor color) {
			ChampionsClientBossEvent event = ChampionsHealthOverlay.this.events.get(id);
			if (event != null) {
				event.setColor(color);
			}
		}

		@Override
		public void updateName(UUID id, Component name) {
			ChampionsClientBossEvent event = ChampionsHealthOverlay.this.events.get(id);
			if (event != null) {
				event.setName(name);
			}
		}

		@Override
		public void updateAffixes(UUID id, EntityAffixes affixes) {
			ChampionsClientBossEvent event = ChampionsHealthOverlay.this.events.get(id);
			if (event != null) {
				event.setAffixes(affixes);
			}
		}
	}
}
