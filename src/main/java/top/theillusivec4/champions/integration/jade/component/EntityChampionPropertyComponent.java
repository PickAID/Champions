package top.theillusivec4.champions.integration.jade.component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.JadeIds;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import top.theillusivec4.champions.integration.jade.element.StarElement;
import top.theillusivec4.champions.world.entity.champion.property.ChampionPropertyHelper;

import java.util.ArrayList;
import java.util.List;

public class EntityChampionPropertyComponent implements IEntityComponentProvider {
  public static EntityChampionPropertyComponent create() {
    return new EntityChampionPropertyComponent();
  }

  @Override
  public void appendTooltip(ITooltip tip, EntityAccessor accessor, IPluginConfig config) {
    Entity entity = accessor.getEntity();
    int tier = ChampionPropertyHelper.getTier(entity);
    if (tier > 0) {
      List<IElement> list = new ArrayList<>();
      for (int i = 0; i < tier; i++) {
        list.add(new StarElement(ChampionPropertyHelper.getColor(entity)));
      }
      tip.add(list);
      tip.replace(JadeIds.CORE_OBJECT_NAME, ChampionPropertyHelper.getDisplayName(entity));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ChampionsComponents.ENTITY_CHAMPION_PROPERTY;
  }
}
