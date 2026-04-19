package top.theillusivec4.champions.integrations.theoneprobe.elements;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import mcjty.theoneprobe.api.ITextStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.integrations.theoneprobe.ChampionsTheOneProbePlugin;
import top.theillusivec4.champions.integrations.theoneprobe.styles.TextStyle;

public class ElementColoredText implements IElement {
  public static final StreamCodec<RegistryFriendlyByteBuf, ElementColoredText> STREAM_CODEC = StreamCodec.composite(
    ComponentSerialization.STREAM_CODEC, element -> element.text,
    ByteBufCodecs.INT, element -> element.color,
    TextStyle.STREAM_CODEC, element -> element.style,
    ElementColoredText::new
  );
  private final Component text;
  private final int color;
  private final TextStyle style;

  public ElementColoredText(Component text, int color, TextStyle style) {
    this.text = text;
    this.color = color;
    this.style = style;
  }

  public static ElementColoredText create(Component text, TextColor color) {
    return new ElementColoredText(text, color.getValue(), TextStyle.EMPTY);
  }

  public static IElementFactory factory() {
    return new Factory();
  }

  @Override
  public void render(GuiGraphics graphics, int x, int y) {
    int width = this.getTextWidth();
    x = switch (this.style.getAlignment()) {
      case ALIGN_BOTTOMRIGHT -> x + this.getInternalWidth() - width + this.style.getLeftPadding();
      case ALIGN_CENTER -> x + this.getInternalWidth() / 2 - width / 2 + this.style.getLeftPadding();
      case ALIGN_TOPLEFT -> x + this.style.getLeftPadding();
    };
    y += this.style.getTopPadding();
    PoseStack stack = graphics.pose();
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    stack.pushPose();
    stack.translate(0.0F, 0.0F, 32.0F);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    Lighting.setupFor3DItems();
    RenderSystem.disableDepthTest();
    RenderSystem.disableBlend();
    graphics.drawString(Minecraft.getInstance().font, text, x, y, this.color);
    RenderSystem.enableDepthTest();
    RenderSystem.enableBlend();
    stack.popPose();
  }

  @Override
  public int getWidth() {
    return this.style.getLeftPadding() + (this.style.getWidth() != null ? this.style.getWidth() : this.getTextWidth()) + this.style.getRightPadding();
  }

  @Override
  public int getHeight() {
    return this.style.getTopPadding() + (this.style.getHeight() != null ? this.style.getHeight() : this.getTextHeight()) + this.style.getBottomPadding();
  }

  @Override
  public void toBytes(RegistryFriendlyByteBuf buf) {
    STREAM_CODEC.encode(buf, this);
  }

  @Override
  public ResourceLocation getID() {
    return ChampionsTheOneProbePlugin.ELEMENT_COLORED_TEXT;
  }

  private int getTextHeight() {
    return 9;
  }

  public ITextStyle getStyle() {
    return style;
  }

  protected int getInternalWidth() {
    return this.style.getWidth() != null ? this.style.getWidth() : this.getTextWidth();
  }

  private int getTextWidth() {
    return Minecraft.getInstance().font.width(text.getVisualOrderText());
  }

  public static class Factory implements IElementFactory {
    private Factory() {
    }

    @Override
    public IElement createElement(RegistryFriendlyByteBuf buf) {
      return STREAM_CODEC.decode(buf);
    }

    @Override
    public ResourceLocation getId() {
      return ChampionsTheOneProbePlugin.ELEMENT_COLORED_TEXT;
    }
  }

}
