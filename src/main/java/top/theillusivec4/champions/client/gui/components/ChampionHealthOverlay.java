package top.theillusivec4.champions.client.gui.components;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.util.Utils;

public final class ChampionHealthOverlay {
  private static final Identifier BAR = Utils.id("textures/gui/bars.png");
  private static final Identifier STAR = Utils.id("textures/gui/staricon.png");
  private DisplayMode mode = DisplayMode.LOOK;
  private int x;
  private int y;

  private static @Nullable Entity getMouseEntity(Minecraft client, float partialTicks) {
    //noinspection ConstantValue
    if (client != null) {
      Entity cameraEntity = client.getCameraEntity();
      if (cameraEntity != null) {
        Vec3 from = cameraEntity.getEyePosition(partialTicks);
        Vec3 direction = cameraEntity.getViewVector(partialTicks);
        double maxDistance = 3.0;
        double maxDistanceSqr = maxDistance * maxDistance;
        Vec3 to = from.add(direction.x * maxDistance, direction.y * maxDistance, direction.z * maxDistance);
        AABB box = cameraEntity.getBoundingBox().expandTowards(direction.scale(maxDistance)).inflate(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(cameraEntity, from, to, box, EntitySelector.CAN_BE_PICKED, maxDistanceSqr);
        return entityHitResult != null ? entityHitResult.getEntity() : null;
      }

    }
    return null;
  }

  public ChampionHealthOverlay() {
  }

  public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
    if (this.mode == DisplayMode.LOOK) {
      /*
      LOOK 模式显示准星处实体
       */
      Entity entity = getMouseEntity(this.getClient(), deltaTracker.getGameTimeDeltaTicks());
      if (entity instanceof LivingEntity livingEntity) {
        ChampionUtil.getHandler(entity).ifPresent(handler -> {
          if (handler.isDisplay()) {
            Component name = handler.getPrefixName().copy().append(CommonComponents.space().append(entity.getDisplayName()));
            ChampionBossEvent event = new ChampionBossEvent(name);
            event.setLevel(handler.getLevel());
            event.setColor(handler.getColor());
            event.setProgress(Math.clamp(livingEntity.getHealth() / livingEntity.getMaxHealth(), 0.0f, 1.0f));
            handler.getAllAffixes().getAffixes().forEach(event::addAffix);
            this.render(guiGraphics, event);
          }
        });
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

  private void render(GuiGraphics guiGraphics, ChampionBossEvent event) {
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
    if (event.getLevel() <= 18) {
      // 小于18的等级⭐⭐⭐
      startX = guiGraphics.guiWidth() / 2 - 5 - 5 * (event.getLevel() - 1);
      this.drawStar(guiGraphics, startX, startY, event);
    } else {
      // 处理过高的等级 ⭐x20
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

  private void drawAffixes(GuiGraphics guiGraphics, int x, int y, ChampionBossEvent event) {
    Component name = event.getAffixesComponent();
    guiGraphics.drawString(this.getClient().font, name, x, y, -1, true);
  }

  private void drawString(GuiGraphics guiGraphics, int x, int y, ChampionBossEvent event) {
    Component name = event.getName();
    guiGraphics.drawString(this.getClient().font, name, x, y, event.getColor(), true);
  }

  private void drawStar(GuiGraphics guiGraphics, int x, int y, ChampionBossEvent event) {
    for (int i = 0; i < event.getLevel(); i++) {
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, STAR, x, y, 0, 0, 9, 9, 9, 9, event.getColor());
      x += 10;
    }
  }

  private void drawBar(GuiGraphics guiGraphics, int x, int y, ChampionBossEvent event) {
    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BAR, x, y, 0, 60, 182, 5, 256, 256, event.getColor());
    int width = Mth.lerpDiscrete(event.getProgress(), 0, 182);
    if (width > 0) {
      guiGraphics.blit(RenderPipelines.GUI_TEXTURED, BAR, x, y, 0, 65, width, 5, 256, 256, event.getColor());
    }
  }

  public enum DisplayMode {
    BROADCAST,
    LOOK,
    HIDE;
  }
}
