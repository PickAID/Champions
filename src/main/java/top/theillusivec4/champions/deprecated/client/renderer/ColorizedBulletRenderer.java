package top.theillusivec4.champions.deprecated.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.object.projectile.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import top.theillusivec4.champions.world.entity.BaseBulletEntity;
import top.theillusivec4.champions.util.Utils;

public class ColorizedBulletRenderer<T extends BaseBulletEntity, S extends BulletRenderState> extends EntityRenderer<T, S> {

  private static final Identifier GENERIC_SPARK_TEXTURE = Utils.id("textures/entity/generic_spark.png");
  private static final RenderType RENDER_TYPE = RenderTypes.entityTranslucent(GENERIC_SPARK_TEXTURE);
  private final ShulkerBulletModel model;

  private final int color;

  public ColorizedBulletRenderer(EntityRendererProvider.Context manager, int color) {
    super(manager);
    this.color = color;
    this.model = new ShulkerBulletModel(manager.bakeLayer(ModelLayers.SHULKER_BULLET));
  }

  @Override
  protected int getBlockLightLevel(final BaseBulletEntity bullet,
                                   final BlockPos blockPos) {
    return 15;
  }

  @Override
  public void submit(S renderState, PoseStack matrixStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
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
    // argb range(0, 255)
    // 0 transparent 255 opaque.
    // so need calculate percent to argb range: 100% * 255 = 255, 15% * 255 = 38.25 ~= 38
    submitNodeCollector.submitModel(
      this.model,
      renderState,
      matrixStack,
      this.model.renderType(GENERIC_SPARK_TEXTURE),
      renderState.lightCoords,
      OverlayTexture.NO_OVERLAY,
      renderState.outlineColor,
      null,
      ARGB.opaque(this.color),
      null
    );
    matrixStack.scale(1.5F, 1.5F, 1.5F);
    submitNodeCollector.order(1)
      .submitModel(
        this.model, renderState, matrixStack, RENDER_TYPE, renderState.lightCoords, OverlayTexture.NO_OVERLAY, ARGB.color(ARGB.alpha(15 * 255), this.color), null, renderState.outlineColor, null
      );

    matrixStack.popPose();
    super.submit(renderState, matrixStack, submitNodeCollector, cameraRenderState);
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
