package top.theillusivec4.champions.client.gui;

import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.util.Util;

public final class ChampionsGuiLayers {
  public static final ResourceLocation HEALTH_OVERLAY = register("health_overlay");

  private ChampionsGuiLayers() {
  }

  private static ResourceLocation register(String name) {
    return Util.id(name);
  }
}
