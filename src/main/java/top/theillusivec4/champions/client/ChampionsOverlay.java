package top.theillusivec4.champions.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.client.gui.GuiLayer;
import top.theillusivec4.champions.client.util.HUDHelper;
import top.theillusivec4.champions.client.util.MouseHelper;
import top.theillusivec4.champions.common.config.ChampionsConfig;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;

@Deprecated
public class ChampionsOverlay implements GuiLayer {

  public static boolean isRendering = false;
  public static int startX = 0;
  public static int startY = 0;

  public static boolean isBlackListEntity(LivingEntity entity) {
    return ChampionsConfig.bossBarBlackList.contains(Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType())).toString());
  }

  @Override
  @ParametersAreNonnullByDefault
  public void render(GuiGraphics pGuiGraphics, DeltaTracker pDeltaTracker) {
    if (ChampionsConfig.showHud) {
      Minecraft mc = Minecraft.getInstance();
      Optional<LivingEntity> livingEntity = MouseHelper.getMouseOverChampion(mc, pDeltaTracker.getGameTimeDeltaTicks());
      livingEntity.ifPresent(entity -> isRendering = !isBlackListEntity(entity) && HUDHelper.renderHealthBar(pGuiGraphics, entity));

      if (livingEntity.isEmpty()) {
        isRendering = false;
      }
    }
  }
}
