package top.theillusivec4.champions.integrations.jade.element;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.ui.Element;
import top.theillusivec4.champions.util.Util;

public class StarElement extends Element {
  private static final ResourceLocation TEXTURE = Util.id("textures/gui/star.png");
  private static final Vec2 SIZE = new Vec2(9, 9);
  private final TextColor color;

  public StarElement(TextColor color) {
    this.color = color;
  }

  @Override
  public Vec2 getSize() {
    return SIZE;
  }

  @Override
  public void render(GuiGraphics graphics, float x, float y, float maxX, float maxY) {
    int value = this.color.getValue();
    float r = (float) FastColor.ARGB32.red(value) / 255F;
    float g = (float) FastColor.ARGB32.green(value) / 255F;
    float b = (float) FastColor.ARGB32.blue(value) / 255F;
    RenderSystem.setShaderColor(r, g, b, 1.0F);
    graphics.blit(TEXTURE, (int) x, (int) y, 0, 0, 9, 9, 9, 9);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }
}
