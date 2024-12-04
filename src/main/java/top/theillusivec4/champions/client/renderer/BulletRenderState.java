package top.theillusivec4.champions.client.renderer;

import net.minecraft.client.renderer.entity.state.ShulkerBulletRenderState;

public class BulletRenderState extends ShulkerBulletRenderState {
  int color;

  public BulletRenderState (int color, float xRot, float yRot){
    this.color = color;
    this.xRot = xRot;
    this.yRot = yRot;
  }
}
