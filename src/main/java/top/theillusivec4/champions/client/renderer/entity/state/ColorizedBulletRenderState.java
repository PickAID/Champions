package top.theillusivec4.champions.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.ShulkerBulletRenderState;

public class ColorizedBulletRenderState extends ShulkerBulletRenderState {
  public int color;

  public ColorizedBulletRenderState(int color, float xRot, float yRot) {
    this.color = color;
    this.xRot = xRot;
    this.yRot = yRot;
  }
}
