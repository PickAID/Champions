package top.theillusivec4.champions.integration.jade.component;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.AffixHelper;

import java.util.Map;

public class EntityAffixesComponent implements IEntityComponentProvider {
  public static EntityAffixesComponent create() {
    return new EntityAffixesComponent();
  }
  @Override
  public void appendTooltip(ITooltip tip, EntityAccessor accessor, IPluginConfig config) {
    for (Map.Entry<Holder<Affix>, Integer> entry : AffixHelper.get(accessor.getEntity()).entrySet()) {
      tip.add(Affix.getFullName(entry.getKey(), entry.getValue()));
    }
  }

  @Override
  public ResourceLocation getUid() {
    return ChampionsComponents.ENTITY_AFFIXES;
  }
}
