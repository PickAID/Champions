package top.theillusivec4.champions.integration.jade.component;

import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.util.ChampionsUtil;

public final class ChampionsComponents {
  public static final ResourceLocation ENTITY_AFFIXES = register("entity_affixes");
  public static final ResourceLocation ENTITY_CHAMPION_PROPERTY = register("entity_champion_property");

  private ChampionsComponents() {
  }

  private static ResourceLocation register(String name) {
    return ChampionsUtil.id(name);
  }
}
