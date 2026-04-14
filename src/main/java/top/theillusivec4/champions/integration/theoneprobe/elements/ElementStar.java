package top.theillusivec4.champions.integration.theoneprobe.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import top.theillusivec4.champions.integration.theoneprobe.ChampionsTheOneProbePlugin;
import top.theillusivec4.champions.util.ChampionsUtil;

public class ElementStar implements IElement {
  public static final StreamCodec<RegistryFriendlyByteBuf, ElementStar> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT, element -> element.tier,
    ByteBufCodecs.INT, element -> element.color,
    ElementStar::new
  );
  private static final ResourceLocation TEXTURE = ChampionsUtil.id("textures/gui/star.png");
  private final int tier;
  private final int color;

  public ElementStar(int tier, int color) {
    this.tier = tier;
    this.color = color;
  }

  public static ElementStar create(int tier, TextColor color) {
    return new ElementStar(tier, color.getValue());
  }

  public static IElementFactory factory() {
    return new Factory();
  }

  @Override
  public void render(GuiGraphics graphics, int x, int y) {
    float r = (float) FastColor.ARGB32.red(this.color) / 255F;
    float g = (float) FastColor.ARGB32.green(this.color) / 255F;
    float b = (float) FastColor.ARGB32.blue(this.color) / 255F;
    RenderSystem.setShaderColor(r, g, b, 1.0F);
    for (int i = 0; i < this.tier; i++) {
      graphics.blit(TEXTURE, x + i * this.getTextureWidth(), y, 0, 0, 9, 9, 9, 9);
    }
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }

  @Override
  public int getWidth() {
    return tier * 9;
  }

  @Override
  public int getHeight() {
    return 9;
  }

  @Override
  public void toBytes(RegistryFriendlyByteBuf buf) {
    STREAM_CODEC.encode(buf, this);
//    buf.writeBoolean(true);
//    buf.writeResourceLocation(TEXTURE);
//    buf.writeInt(this.getWidth());
//    buf.writeInt(this.getHeight());
//    buf.writeInt(this.getTextureWidth());
//    buf.writeInt(this.getTextureHeight());
//    buf.writeInt(this.getColor());
  }

  @Override
  public ResourceLocation getID() {
    return ChampionsTheOneProbePlugin.ELEMENT_STAR;
  }

  private int getTextureWidth() {
    return 9;
  }

  private int getTextureHeight() {
    return 9;
  }

  private int getColor() {
    return color;
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
      return ChampionsTheOneProbePlugin.ELEMENT_STAR;
    }
  }

}
