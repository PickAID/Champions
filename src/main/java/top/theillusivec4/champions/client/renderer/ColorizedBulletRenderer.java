package top.theillusivec4.champions.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import top.theillusivec4.champions.common.entity.BaseBulletEntity;
import top.theillusivec4.champions.common.util.Utils;

import javax.annotation.Nonnull;

public class ColorizedBulletRenderer<T extends BaseBulletEntity, S extends BulletRenderState> extends EntityRenderer<T, S> {

  private static final ResourceLocation GENERIC_SPARK_TEXTURE = Utils.getLocation("textures/entity/generic_spark.png");
  private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(GENERIC_SPARK_TEXTURE);
  private final ShulkerBulletModel model;

  private final int color;

  public ColorizedBulletRenderer(EntityRendererProvider.Context manager, int color) {
    super(manager);
    this.color = color;
    this.model = new ShulkerBulletModel(manager.bakeLayer(ModelLayers.SHULKER_BULLET));
  }

  @Override
  protected int getBlockLightLevel(@Nonnull final BaseBulletEntity bullet,
                                   @Nonnull final BlockPos blockPos) {
    return 15;
  }

  @Override
  public void render(S renderState,
                     PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
    matrixStack.pushPose();
    float yRot = Mth.rotLerp(renderState.xRot, renderState.yRot, renderState.partialTick);
    float xRot = Mth.lerp(renderState.partialTick, renderState.xRot, renderState.yRot);
    float tickModifier = renderState.ageInTicks + renderState.partialTick;
    matrixStack.translate(0.0D, 0.15000000596046448D, 0.0D);
    matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(tickModifier * 0.1F) * 180.0F));
    matrixStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(tickModifier * 0.1F) * 180.0F));
    matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(tickModifier * 0.15F) * 360.0F));
    matrixStack.scale(-0.5F, -0.5F, 0.5F);
    this.model.setupAnim(new BulletRenderState(color, xRot, yRot));
    VertexConsumer vertexconsumer = buffer.getBuffer(this.model.renderType(GENERIC_SPARK_TEXTURE));
    // argb range(0, 255)
    // 0 transparent 255 opaque.
    // so need calculate percent to argb range: 100% * 255 = 255, 15% * 255 = 38.25 ~= 38
    this.model.renderToBuffer(matrixStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY,
      ARGB.opaque(this.color));
    matrixStack.scale(1.5F, 1.5F, 1.5F);
    VertexConsumer vertexconsumer1 = buffer.getBuffer(RENDER_TYPE);
    this.model.renderToBuffer(matrixStack, vertexconsumer1, packedLight, OverlayTexture.NO_OVERLAY,
      ARGB.color(ARGB.alpha(15 * 255), this.color));
    matrixStack.popPose();
    super.render(renderState, matrixStack, buffer, packedLight);
  }

  @Override
  public void extractRenderState(T entity, S renderState, float p_360538_) {
    super.extractRenderState(entity, renderState, p_360538_);
  }

  @Override
  @SuppressWarnings("unchecked")
  public S createRenderState() {
    return (S) new BulletRenderState(color, 0f, 0f);
  }
}
