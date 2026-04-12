package top.theillusivec4.champions.integration.jade.component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import top.theillusivec4.champions.champion.ChampionHelper;
import top.theillusivec4.champions.integration.jade.element.StarElement;

import java.util.ArrayList;
import java.util.List;

public class EntityChampionPropertyComponent implements IEntityComponentProvider {
  @Override
  public void appendTooltip(ITooltip tip, EntityAccessor accessor, IPluginConfig config) {
    Entity entity = accessor.getEntity();
    int tier = ChampionHelper.getTier(entity);
    if (tier > 0) {
      List<IElement> list = new ArrayList<>();
      for (int i = 0; i < tier; i++) {
        list.add(new StarElement(ChampionHelper.getColor(entity)));
      }
      tip.add(list);
      tip.add(IElementHelper.get().text(ChampionHelper.getDisplayName(entity)));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ChampionsComponents.ENTITY_CHAMPION_PROPERTY;
  }
}
