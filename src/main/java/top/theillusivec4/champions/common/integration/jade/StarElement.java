package top.theillusivec4.champions.common.integration.jade;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.ui.Element;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.client.util.HUDHelper;

public class StarElement extends Element {
  private final int starCount;  // 记录星星的数量
  private final int spacing;    // 间距
  private final float r, g, b;  // 颜色

  StarElement(int starCount, final String colorCode, int spacing) {
    int color = TextColor.parseColor(colorCode).getOrThrow().getValue();
    this.starCount = starCount;
    this.spacing = spacing;

    r = FastColor.ARGB32.red(color) / 255.0F;
    g = FastColor.ARGB32.green(color) / 255.0F;
    b = FastColor.ARGB32.blue(color) / 255.0F;
  }

  public static StarElement of(int starCount, final String colorCode, int spacing) {
    return new StarElement(starCount, colorCode, spacing);
  }

  public ResourceLocation getTexture() {
    return HUDHelper.getGuiStar();
  }

  @Override
  public Vec2 getSize() {
    // 宽度 = (9px * 星星数) + (间距 * (星星数 - 1))
    return new Vec2(starCount * 9 + (starCount - 1) * spacing, 9+ ClientChampionsConfig.jadeStarBottomPadding);
  }

  @Override
  public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
    // set render element color
    RenderSystem.setShaderColor(r, g, b, 1.0F);
    for (int i = 0; i < starCount; i++) {
      guiGraphics.blit(
        getTexture(),
        (int) (x + i * (9 + spacing)), // 计算 X 偏移量
        (int) y, // Y 坐标不变
        0, 0,
        9, 9, 9, 9
      );
    }
    // reset color
    RenderSystem.setShaderColor(1F, 1F, 1F, 1.0F);

  }
}
