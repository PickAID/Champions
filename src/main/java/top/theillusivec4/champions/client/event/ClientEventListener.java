package top.theillusivec4.champions.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.joml.Matrix4f;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.util.ChampionsUtil;
import top.theillusivec4.champions.world.entity.champion.property.ChampionMobPropertyHelper;

@EventBusSubscriber(modid = ChampionsMod.MOD_ID, value = Dist.CLIENT)
public final class ClientEventListener {
  private static final ResourceLocation TEXTURE = ChampionsUtil.id("textures/gui/star.png");

  private ClientEventListener() {
  }

  @SubscribeEvent
  private static void onEntityTickPre(EntityTickEvent.Pre event) {
    Entity entity = event.getEntity();
    Level level = event.getEntity().level();
    if (level.isClientSide()) {
      ChampionMobPropertyHelper.doParticlesEffects(entity);
    }
  }

  @SubscribeEvent
  private static void onRenderNameTag(RenderNameTagEvent event) {
//    PoseStack poseStack = event.getPoseStack();
//    MultiBufferSource buffer = event.getMultiBufferSource();
//    Entity entity = event.getEntity();
//
//    if (entity.getType() != EntityType.HUSK) {
//      return;
//    }
//    int color = ChampionHelper.getColor(entity).getValue();
//    int light = event.getPackedLight();
//    renderStarIcon(poseStack, entity, buffer, light, 0.5, color);
//    renderStarIcon(poseStack, entity, buffer, light, 0.0, color);
//    renderStarIcon(poseStack, entity, buffer, light, -0.5, color);
  }

  private static void renderStar(PoseStack poseStack, Entity entity, MultiBufferSource buffer, int light, double xzOffset, int color) {
    // 保存当前矩阵状态
    poseStack.pushPose();

    // 让面片始终面向玩家
    EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
    poseStack.mulPose(dispatcher.cameraOrientation());
    poseStack.mulPose(Axis.YP.rotationDegrees(180));

    // 平移到实体头顶（根据实体高度调整 Y 值）
    poseStack.translate(xzOffset, entity.getBbHeight() + 1.0, xzOffset);

    // 缩放一下，避免物品过大
    poseStack.scale(0.25f, 0.25f, 0.25f);

    int a = (color >> 24) & 0xFF; // Alpha
    int r = (color >> 16) & 0xFF; // Red
    int g = (color >> 8) & 0xFF; // Green
    int b = color & 0xFF; // Blue

    Matrix4f matrix = poseStack.last().pose();

    // 渲染类型
    RenderType renderType = RenderType.entityCutout(TEXTURE);
    VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
    // 绘制一个正方形面片
    vertexConsumer
      .addVertex(matrix, -0.5F, 0.5F, 0.0F)
      .setColor(r, g, b, a)
      .setUv(0.0F, 0.0F)
      .setOverlay(OverlayTexture.NO_OVERLAY)
      .setLight(light)
      .setNormal(0, 0, 1);
    vertexConsumer
      .addVertex(matrix, 0.5F, 0.5F, 0.0F)
      .setColor(r, g, b, a)
      .setUv(1.0F, 0.0F)
      .setOverlay(OverlayTexture.NO_OVERLAY)
      .setLight(light)
      .setNormal(0, 0, 1);
    vertexConsumer
      .addVertex(matrix, 0.5F, -0.5F, 0.0F)
      .setColor(r, g, b, a)
      .setUv(1.0F, 1.0F)
      .setOverlay(OverlayTexture.NO_OVERLAY)
      .setLight(light)
      .setNormal(0, 0, 1);
    vertexConsumer
      .addVertex(matrix, -0.5F, -0.5F, 0.0F)
      .setColor(r, g, b, a)
      .setUv(0.0F, 1.0F)
      .setOverlay(OverlayTexture.NO_OVERLAY)
      .setLight(light)
      .setNormal(0, 0, 1);

    // 恢复矩阵
    poseStack.popPose();
  }

}
