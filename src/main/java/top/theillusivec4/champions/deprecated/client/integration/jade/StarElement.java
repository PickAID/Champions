//package top.theillusivec4.champions.deprecated.client.integration.jade;
//
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.renderer.RenderPipelines;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.chat.TextColor;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.ARGB;
//import org.jetbrains.annotations.Nullable;
//import snownee.jade.api.ui.Element;
//import snownee.jade.api.ui.ResizeableElement;
//import top.theillusivec4.champions.deprecated.client.config.ClientChampionsConfig;
//import top.theillusivec4.champions.deprecated.client.util.HUDHelper;
//
//public class StarElement extends Element {
//  private final int starCount;  // 记录星星的数量
//  private final int spacing;    // 间距
//  private final int color;  // 颜色
//
//  StarElement(int starCount, final String colorCode, int spacing) {
//    this.color = ARGB.opaque(TextColor.parseColor(colorCode).getOrThrow().getValue());
//    this.starCount = starCount;
//    this.spacing = spacing;
//    this.width = starCount * 9 + (starCount - 1) * spacing;
//    this.height = 9 + ClientChampionsConfig.jadeStarBottomPadding;
//  }
//
//  public static StarElement of(int starCount, final String colorCode, int spacing) {
//    return new StarElement(starCount, colorCode, spacing);
//  }
//
//  public ResourceLocation getTexture() {
//    return HUDHelper.getGuiStar();
//  }
//
//  @Override
//  public ResizeableElement size(int width, int height) {
//    return super.size(width, height);
//  }
//
//  @Override
//  public @Nullable Component getNarration() {
//    return null;
//  }
//
//  @Override
//  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
//    // set render element color
//    guiGraphics.pose().pushMatrix();
//    for (int i = 0; i < starCount; i++) {
//      guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
//        getTexture(),
//        this.getX() + mouseX + i * (9 + spacing), // 计算 X 偏移量
//        this.getY() + mouseY, // Y 坐标不变
//        0, 0,
//        9, 9, 9, 9, color
//      );
//    }
//    guiGraphics.pose().pushMatrix();
//  }
//}
