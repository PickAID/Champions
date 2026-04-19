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
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import top.theillusivec4.champions.util.Util;

public class ColorizedBulletRenderer extends EntityRenderer<ShulkerBullet> {
  private static final ResourceLocation GENERIC_SPARK_TEXTURE = Util.id("textures/entity/generic_spark.png");
  private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(GENERIC_SPARK_TEXTURE);
  private final ShulkerBulletModel<ShulkerBullet> model;
  private final int color;

  public ColorizedBulletRenderer(EntityRendererProvider.Context manager, int color) {
    super(manager);
    this.model = new ShulkerBulletModel<>(manager.bakeLayer(ModelLayers.SHULKER_BULLET));
    this.color = color;
  }

  @Override
  protected int getBlockLightLevel(ShulkerBullet entity, BlockPos pos) {
    return 15;
  }

  @Override
  public void render(ShulkerBullet entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    poseStack.pushPose();
    float yRot = Mth.rotLerp(entity.yRotO, entity.getYRot(), partialTicks);
    float xRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
    float tickModifier = (float) entity.tickCount + partialTicks;
    poseStack.translate(0.0D, 0.15000000596046448D, 0.0D);
    poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(tickModifier * 0.1F) * 180.0F));
    poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(tickModifier * 0.1F) * 180.0F));
    poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(tickModifier * 0.15F) * 360.0F));
    poseStack.scale(-0.5F, -0.5F, 0.5F);
    this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, yRot, xRot);
    VertexConsumer consumer = buffer.getBuffer(this.model.renderType(GENERIC_SPARK_TEXTURE));
    this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
      FastColor.ARGB32.color(255, this.color));
    poseStack.scale(1.5F, 1.5F, 1.5F);
    VertexConsumer consumer1 = buffer.getBuffer(RENDER_TYPE);
    this.model.renderToBuffer(poseStack, consumer1, packedLight, OverlayTexture.NO_OVERLAY,
      FastColor.ARGB32.color(38, this.color));
    poseStack.popPose();
    super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
  }

  @Override
  public ResourceLocation getTextureLocation(ShulkerBullet entity) {
    return GENERIC_SPARK_TEXTURE;
  }

}
