package top.theillusivec4.champions.integration.jade;

import net.minecraft.world.entity.Entity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import top.theillusivec4.champions.integration.jade.component.EntityAffixesComponent;
import top.theillusivec4.champions.integration.jade.component.EntityChampionPropertyComponent;

@WailaPlugin
public class ChampionsJadePlugin implements IWailaPlugin {
  private final EntityChampionPropertyComponent entityChampion = new EntityChampionPropertyComponent();
  private final EntityAffixesComponent entityAffixes = new EntityAffixesComponent();

  @Override
  public void register(IWailaCommonRegistration registration) {
    IWailaPlugin.super.register(registration);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    registration.registerEntityComponent(this.entityChampion, Entity.class);
    registration.registerEntityComponent(this.entityAffixes, Entity.class);
  }
}
