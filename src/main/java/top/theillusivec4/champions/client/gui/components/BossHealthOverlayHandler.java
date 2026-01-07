package top.theillusivec4.champions.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import top.theillusivec4.champions.util.Utils;

/**
 * BossBar渲染处理程序，该类的目的是拓展与重写{@link net.minecraft.client.gui.components.BossHealthOverlay}的行为
 */
public class BossHealthOverlayHandler {
  private static final Identifier BAR = Utils.id("textures/gui/bars.png");
  private static final Identifier STAR = Utils.id("textures/gui/staricon.png");

  public BossHealthOverlayHandler() {

  }

  public void render(GuiGraphics guiGraphics, int x, int y, ChampionClientBossEvent event) {
    /*
    生命值条显示
     */
    this.drawBar(guiGraphics, x, y, event);

    /*
    等级显示
     */
    int startX;
    int startY;

    if (event.getLevel() <= 18) {
      // 小于18的等级⭐⭐⭐
      startX = guiGraphics.guiWidth() / 2 - 5 - 5 * event.getLevel();
      startY = y + 1;
      this.drawStar(guiGraphics, startX, startY, event);
    } else {
      // 处理过高的等级 ⭐x20
      startX = guiGraphics.guiWidth() / 2 - 5;
      String count = "x" + event.getLevel();
      startY = y + 1;
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, STAR, startX - this.getClient().font.width(count) / 2, startY, 0, 0, 9, 9, 9, 9, event.color());
      startX = startX + 10 - this.getClient().font.width(count) / 2;
      startY = y + 2;
      guiGraphics.drawString(this.getClient().font, count, startX, startY, 16777215, true);
    }

    /*
    名称显示
     */
    startX = guiGraphics.guiWidth() / 2 + this.getClient().font.width(event.getName()) / 2;
    startY = y - 9;
    this.drawString(guiGraphics, startX, startY, event);

    /*
    词缀显示
     */
    startX = guiGraphics.guiWidth() / 2 + this.getClient().font.width(event.getAffixesComponent()) / 2;
    startY = y + 6;
    this.drawAffixes(guiGraphics, startX, startY, event);
  }

  public void drawBar(GuiGraphics guiGraphics, int x, int y, ChampionClientBossEvent event) {
    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BAR, x, y, 0, 60, 182, 5, 256, 256, event.color());
    int width = Mth.lerpDiscrete(event.getProgress(), 0, 182);
    if (width > 0) {
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BAR, x, y, 0, 65, width, 5, 256,
        256, event.color());
    }
  }

  public void drawStar(GuiGraphics guiGraphics, int x, int y, ChampionClientBossEvent event) {
    for (int i = 0; i < event.getLevel(); i++) {
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, STAR, x, y, 0, 0, 9, 9, 9, 9, event.color());
      x += 10;
    }
  }

  public void drawString(GuiGraphics guiGraphics, int x, int y, ChampionClientBossEvent event) {
    Component name = event.getName();
    guiGraphics.drawString(this.getClient().font, name, x, y, event.color(), true);
  }

  public void drawAffixes(GuiGraphics guiGraphics, int x, int y, ChampionClientBossEvent event) {
    Component name = event.getAffixesComponent();
    guiGraphics.drawString(this.getClient().font, name, x, y, 16777215, true);
  }

  public Minecraft getClient() {
    return Minecraft.getInstance();
  }
}
