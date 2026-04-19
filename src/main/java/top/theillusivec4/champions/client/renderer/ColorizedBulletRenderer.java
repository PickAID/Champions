package top.theillusivec4.champions.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.ShulkerBulletModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import top.theillusivec4.champions.client.renderer.entity.state.ColorizedBulletRenderState;
import top.theillusivec4.champions.util.Util;

public class ColorizedBulletRenderer extends EntityRenderer<ShulkerBullet, ColorizedBulletRenderState> {
	private static final Identifier TEXTURE_LOCATION = Util.id("textures/entity/generic_spark.png");
	private static final RenderType RENDER_TYPE = RenderTypes.entityTranslucent(TEXTURE_LOCATION);
	private final ShulkerBulletModel model;
	private final int color;

	public ColorizedBulletRenderer(EntityRendererProvider.Context manager, int color) {
		super(manager);
		this.model = new ShulkerBulletModel(manager.bakeLayer(ModelLayers.SHULKER_BULLET));
		this.color = color;
	}

	@Override
	protected int getBlockLightLevel(ShulkerBullet bullet, BlockPos blockPos) {
		return 15;
	}

	@Override
	public void submit(ColorizedBulletRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		float tc = state.ageInTicks;
		poseStack.translate(0.0F, 0.15F, 0.0F);
		poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(tc * 0.1F) * 180.0F));
		poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(tc * 0.1F) * 180.0F));
		poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(tc * 0.15F) * 360.0F));
		poseStack.scale(-0.5F, -0.5F, 0.5F);
		submitNodeCollector.submitModel(
				this.model, state, poseStack, this.model.renderType(TEXTURE_LOCATION), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null, ARGB.opaque(this.color), null
		);
		poseStack.scale(1.5F, 1.5F, 1.5F);
		submitNodeCollector.order(1)
				.submitModel(this.model, state, poseStack, RENDER_TYPE, state.lightCoords, OverlayTexture.NO_OVERLAY, ARGB.color(64, this.color), null, state.outlineColor, null);
		poseStack.popPose();
		super.submit(state, poseStack, submitNodeCollector, camera);

		//    poseStack.pushPose();
//    float yRot = Mth.rotLerp(state.xRot, state.yRot, state.partialTick);
//    float xRot = Mth.lerp(state.partialTick, state.xRot, state.yRot);
//    float tickModifier = state.ageInTicks + state.partialTick;
//    poseStack.translate(0.0D, 0.15000000596046448D, 0.0D);
//    poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(tickModifier * 0.1F) * 180.0F));
//    poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(tickModifier * 0.1F) * 180.0F));
//    poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(tickModifier * 0.15F) * 360.0F));
//    poseStack.scale(-0.5F, -0.5F, 0.5F);
//    this.model.setupAnim(new ColorizedBulletRenderState(color, xRot, yRot));
//    // argb range(0, 255)
//    // 0 transparent 255 opaque.
//    // so need calculate percent to argb range: 100% * 255 = 255, 15% * 255 = 38.25 ~= 38
//    submitNodeCollector.submitModel(
//      this.model,
//      state,
//      poseStack,
//      this.model.renderType(TEXTURE_LOCATION),
//      state.lightCoords,
//      OverlayTexture.NO_OVERLAY,
//      state.outlineColor,
//      null,
//      ARGB.opaque(this.color),
//      null
//    );
//    poseStack.scale(1.5F, 1.5F, 1.5F);
//    submitNodeCollector.order(1)
//      .submitModel(
//        this.model, state, poseStack, RENDER_TYPE, state.lightCoords, OverlayTexture.NO_OVERLAY, ARGB.color(ARGB.alpha(15 * 255), this.color), null, state.outlineColor, null
//      );
//
//    poseStack.popPose();
//    super.submit(state, poseStack, submitNodeCollector, camera);
	}

	@Override
	public ColorizedBulletRenderState createRenderState() {
		return new ColorizedBulletRenderState(this.color, 0.0f, 0.0f);
	}

	@Override
	public void extractRenderState(ShulkerBullet entity, ColorizedBulletRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.yRot = entity.getYRot(partialTicks);
		state.xRot = entity.getXRot(partialTicks);
	}
}
