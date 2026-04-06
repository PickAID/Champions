package top.theillusivec4.champions.client.gui.components;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.ChampionsClient;
import top.theillusivec4.champions.champion.ChampionHelper;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.client.util.ClientUtil;
import top.theillusivec4.champions.network.protocol.game.ClientboundChampionBossEventPacket;
import top.theillusivec4.champions.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ChampionHealthOverlay {
	private static final Identifier BAR = Util.id("textures/gui/bars.png");
	private static final Identifier STAR = Util.id("textures/gui/staricon.png");
	private final Map<UUID, ClientChampionBossEvent> events = new HashMap<>();
	private final ChampionHealthOverlay.Handler handler = new Handler();
	private int x;
	private int y;

	public ChampionHealthOverlay() {
	}

	public void update(ClientboundChampionBossEventPacket packet) {
		packet.dispatch(
				packet.getId(),
				this.handler
		);
	}

	public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		// 准星处的实体。
		if (ChampionsClient.getInstance().displayHealthOverlay()) {

			Entity entity = ClientUtil.getMouseEntity(deltaTracker.getGameTimeDeltaTicks());
			if (entity != null) {
				if (entity instanceof LivingEntity livingEntity && ChampionHelper.canDisplayHealthOverlay(entity)) {
					ClientChampionBossEvent event = new ClientChampionBossEvent(entity.getUUID(), ChampionHelper.getName(entity));
					event.setLevel(ChampionHelper.getLevel(entity));
					event.setColor(ChampionHelper.getColor(entity));
					event.setProgress(Math.clamp(livingEntity.getHealth() / livingEntity.getMaxHealth(), 0.0f, 1.0f));
					event.setAffixes(ChampionHelper.getAffixList(entity));
					this.render(graphics, event);
				}
			}
		}

		// 独立的BossBar。
		for (ClientChampionBossEvent event : this.events.values()) {
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

	private void render(GuiGraphicsExtractor guiGraphics, ClientChampionBossEvent event) {
		Component name = event.getName();
    /*
    minecraft.font.lineHeight = 9
    Y坐标：先渲染，然后向下偏移自身所占的Y大小
     */
		int startX;
		int startY = y;
		// 显示等级
		if (event.getLevel() <= 9) {
			// 小于5的等级⭐⭐⭐⭐⭐
			startX = guiGraphics.guiWidth() / 2 - 5 - 5 * (event.getLevel() - 1);
			this.drawStar(guiGraphics, startX, startY, event);
		} else {
			// 处理过高的等级 ⭐x6
			startX = guiGraphics.guiWidth() / 2 - 5;
			String msg = "x" + event.getLevel();
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, STAR, startX - this.getClient().font.width(msg) / 2, startY, 0, 0, 9, 9, 9, 9, event.getColor());
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

	private void drawAffixes(GuiGraphicsExtractor guiGraphics, int x, int y, ClientChampionBossEvent event) {
		Component name = event.getAffixesComponent();
		guiGraphics.text(this.getClient().font, name, x, y, -1, true);
	}

	private void drawString(GuiGraphicsExtractor guiGraphics, int x, int y, ClientChampionBossEvent event) {
		Component name = event.getName();
		guiGraphics.text(this.getClient().font, name, x, y, event.getColor(), true);
	}

	private void drawStar(GuiGraphicsExtractor guiGraphics, int x, int y, ClientChampionBossEvent event) {
		for (int i = 0; i < event.getLevel(); i++) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, STAR, x, y, 0, 0, 9, 9, 9, 9, event.getColor());
			x += 10;
		}
	}

	private void drawBar(GuiGraphicsExtractor guiGraphics, int x, int y, ClientChampionBossEvent event) {
		guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BAR, x, y, 0, 60, 182, 5, 256, 256, event.getColor());
		int width = Mth.lerpDiscrete(event.getProgress(), 0, 182);
		if (width > 0) {
			guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BAR, x, y, 0, 65, width, 5, 256, 256, event.getColor());
		}
	}

	private class Handler implements ClientboundChampionBossEventPacket.Handler {

		@Override
		public void add(UUID id, Component name, float progress, int level, int color, List<Holder<Affix>> affixes) {
			ClientChampionBossEvent event = new ClientChampionBossEvent(id, name, progress, level, color, affixes);
			ChampionHealthOverlay.this.events.put(id, event);
		}

		@Override
		public void remove(UUID id) {
			ChampionHealthOverlay.this.events.remove(id);
		}

		@Override
		public void updateProgress(UUID id, float progress) {
			ClientChampionBossEvent event = ChampionHealthOverlay.this.events.get(id);
			if (event != null) {
				event.setProgress(progress);
			}
		}

		@Override
		public void updateLevel(UUID id, int level) {
			ClientChampionBossEvent event = ChampionHealthOverlay.this.events.get(id);
			if (event != null) {
				event.setLevel(level);
			}
		}

		@Override
		public void updateColor(UUID id, int color) {
			ClientChampionBossEvent event = ChampionHealthOverlay.this.events.get(id);
			if (event != null) {
				event.setColor(color);
			}
		}

		@Override
		public void updateName(UUID id, Component name) {
			ClientChampionBossEvent event = ChampionHealthOverlay.this.events.get(id);
			if (event != null) {
				event.setName(name);
			}
		}

		@Override
		public void updateAffixes(UUID id, List<Holder<Affix>> affixes) {
			ClientChampionBossEvent event = ChampionHealthOverlay.this.events.get(id);
			if (event != null) {
				event.setAffixes(affixes);
			}
		}
	}
}
