package top.theillusivec4.champions.integration.jade.component;

import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class ChampionsComponents {
  public static final ResourceLocation ENTITY_AFFIXES = register("entity_affixes");
  public static final ResourceLocation ENTITY_CHAMPION = register("entity_champion");

  private ChampionsComponents() {
  }

  private static ResourceLocation register(String name) {
    return ChampionsUtil.id(name);
  }
}
