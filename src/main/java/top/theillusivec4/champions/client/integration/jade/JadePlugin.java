//package top.theillusivec4.champions.client.integration.jade;
//
//import net.minecraft.world.entity.LivingEntity;
//import snownee.jade.api.IWailaClientRegistration;
//import snownee.jade.api.IWailaCommonRegistration;
//import snownee.jade.api.IWailaPlugin;
//import snownee.jade.api.WailaPlugin;
//import top.theillusivec4.champions.client.ChampionsOverlay;
//
//@WailaPlugin
//public class JadePlugin implements IWailaPlugin {
//
//  @Override
//  public void register(IWailaCommonRegistration registration) {
//    //TODO register data provider
//  }
//
//  @Override
//  public void registerClient(IWailaClientRegistration registration) {
//    //TODO register component providers, icon providers, callbacks, and config options here
//    registration.registerEntityComponent(ChampionComponentProvider.INSTANCE, LivingEntity.class);
//    registration.addBeforeRenderCallback(((iBoxElement, tooltipRect, guiGraphics, accessor) -> {
//      if (ChampionsOverlay.isRendering) {
//        tooltipRect.rect.setY(ChampionsOverlay.startY + 38);
//      }
//      return false;
//    }));
//  }
//}
