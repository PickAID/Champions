package top.theillusivec4.champions.common.integration.kubejs;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.api.AffixCategory;
import top.theillusivec4.champions.api.BasicAffixBuilder;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.common.affix.AdaptableAffix;

public class AffixBuilderJS extends BuilderBase<IAffix> {

  public AffixBuilderJS(ResourceLocation id) {
    super(id);
  }

  @Override
  public IAffix createObject() {
    return new BasicAffixBuilder<>(AdaptableAffix::new).setCategory(AffixCategory.CC).build();
  }
}
