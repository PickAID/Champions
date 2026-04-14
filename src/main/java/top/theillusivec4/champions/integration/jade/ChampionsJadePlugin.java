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

  @Override
  public void register(IWailaCommonRegistration registration) {
    IWailaPlugin.super.register(registration);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    registration.registerEntityComponent(EntityChampionPropertyComponent.create(), Entity.class);
    registration.registerEntityComponent(EntityAffixesComponent.create(), Entity.class);
  }
}
