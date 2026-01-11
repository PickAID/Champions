package top.theillusivec4.champions.client.gui.components;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.client.util.ClientUtil;
import top.theillusivec4.champions.network.protocol.game.ClientboundChampionBossEventPacket;
import top.theillusivec4.champions.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public final class ChampionHealthOverlay {
  private static final Identifier BAR = Utils.id("textures/gui/bars.png");
  private static final Identifier STAR = Utils.id("textures/gui/staricon.png");
  private final Map<UUID, ClientChampionBossEvent> events = new HashMap<>();
  private final ChampionHealthOverlay.Handler handler = new Handler();
  private DisplayMode mode = DisplayMode.LOOK;
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

  public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    if (this.isDisplayLookAt()) {
      /*
      LOOK 模式显示准星处实体
       */
      Entity entity = ClientUtil.getMouseEntity(deltaTracker.getGameTimeDeltaTicks());
      if (entity != null) {
        ChampionUtil.getHandler(entity).ifPresent(handler -> {
          if (handler.shouldDisplayHealthOverlay()) {
            Component name = handler.getPrefixName().map(component ->
              (Component) component.copy()
                .append(CommonComponents.space())
                .append(entity.getDisplayName())
            ).orElse(entity.getDisplayName());
            ClientChampionBossEvent event = new ClientChampionBossEvent(entity.getUUID(), name);
            event.setLevel(handler.getLevel());
            event.setColor(handler.getColor());
            event.setProgress(Math.clamp(handler.getHealth() / handler.getMaxHealth(), 0.0f, 1.0f));
            event.setAffixes(handler.getAffixes().getAffixes());
            this.render(guiGraphics, event);
          }
        });
      }

    }

    for (ClientChampionBossEvent event : this.events.values()) {
      this.render(guiGraphics, event);
      if (this.y >= guiGraphics.guiHeight() / 3) {
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

  public DisplayMode getMode() {
    return mode;
  }

  public void setMode(DisplayMode mode) {
    this.mode = mode;
  }

  private boolean isDisplayLookAt() {
    return this.mode == DisplayMode.LOOK;
  }

  private void render(GuiGraphics guiGraphics, ClientChampionBossEvent event) {
    Component name = event.getName();
    /*
    水平位置总是重新赋值
    垂直位置总是递增
     */
    int startX;
    int startY = y;

    /*
    等级显示
     */
    if (event.getLevel() <= 5) {
      // 小于5的等级⭐⭐⭐⭐⭐
      startX = guiGraphics.guiWidth() / 2 - 5 - 5 * (event.getLevel() - 1);
      this.drawStar(guiGraphics, startX, startY, event);
    } else {
      // 处理过高的等级 ⭐x6
      startX = guiGraphics.guiWidth() / 2 - 5;
      String msg = "x" + event.getLevel();
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, STAR, startX - this.getClient().font.width(msg) / 2, startY, 0, 0, 9, 9, 9, 9, event.getColor());
      startX = startX + 10 - this.getClient().font.width(msg) / 2;
      guiGraphics.drawString(this.getClient().font, msg, startX, startY, 16777215, true);
    }

    /*
    名称显示
     */
    startX = guiGraphics.guiWidth() / 2 - this.getClient().font.width(name) / 2;
    startY += 12;
    this.drawString(guiGraphics, startX, startY, event);

    startX = guiGraphics.guiWidth() / 2 - 91;
    startY += 9;
    // 血量条显示
    this.drawBar(guiGraphics, startX, startY, event);

    /*
    词缀显示
     */
    startX = guiGraphics.guiWidth() / 2 - this.getClient().font.width(event.getAffixesComponent()) / 2;
    startY += 6;
    this.drawAffixes(guiGraphics, startX, startY, event);

    // 用于可能的后续渲染
    this.y = startY;
  }

  private void drawAffixes(GuiGraphics guiGraphics, int x, int y, ClientChampionBossEvent event) {
    Component name = event.getAffixesComponent();
    guiGraphics.drawString(this.getClient().font, name, x, y, -1, true);
  }

  private void drawString(GuiGraphics guiGraphics, int x, int y, ClientChampionBossEvent event) {
    Component name = event.getName();
    guiGraphics.drawString(this.getClient().font, name, x, y, event.getColor(), true);
  }

  private void drawStar(GuiGraphics guiGraphics, int x, int y, ClientChampionBossEvent event) {
    for (int i = 0; i < event.getLevel(); i++) {
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, STAR, x, y, 0, 0, 9, 9, 9, 9, event.getColor());
      x += 10;
    }
  }

  private void drawBar(GuiGraphics guiGraphics, int x, int y, ClientChampionBossEvent event) {
    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BAR, x, y, 0, 60, 182, 5, 256, 256, event.getColor());
    int width = Mth.lerpDiscrete(event.getProgress(), 0, 182);
    if (width > 0) {
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BAR, x, y, 0, 65, width, 5, 256, 256, event.getColor());
    }
  }

  public enum DisplayMode {
    BROADCAST,
    LOOK,
    HIDE
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
