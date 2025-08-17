package top.theillusivec4.champions.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class EntitySettingScreen extends Screen {

  public LivingEntity entity = null;
  protected EntitySettingScreen(Component title) {
    super(title);
  }


  @Override
  public void init() {
    super.init();
  }
}
