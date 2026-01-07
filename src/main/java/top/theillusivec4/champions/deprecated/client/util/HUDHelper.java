package top.theillusivec4.champions.deprecated.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.deprecated.api.affix.IAffix;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.client.ChampionsOverlay;
import top.theillusivec4.champions.deprecated.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.deprecated.common.capabilities.ChampionAttachment;
import top.theillusivec4.champions.deprecated.common.rank.Rank;
import top.theillusivec4.champions.deprecated.common.util.ChampionHelper;
import top.theillusivec4.champions.util.Utils;

import java.util.Set;
import java.util.stream.Collectors;

public class HUDHelper {

  private static final Identifier GUI_BAR_TEXTURES = Utils.id("textures/gui/bars.png");
  private static final Identifier GUI_STAR = Utils.id("textures/gui/staricon.png");

  public static boolean renderHealthBar(GuiGraphics guiGraphics, final LivingEntity livingEntity) {
    return ChampionAttachment.getAttachment(livingEntity).map(champion -> {
      IChampion.Client clientChampion = champion.getClient();
      return ChampionHelper.isValidChampion(clientChampion) && clientChampion.getRank().map(rank -> {
        int championLevel = rank.getA();
        Set<String> affixSet = clientChampion.getAffixes().stream().map(IAffix::toLanguageKey)
          .collect(Collectors.toSet());

        if (championLevel >= 1 || !affixSet.isEmpty()) {
          Minecraft client = Minecraft.getInstance();
          // calculate render position
          int windowWidth = client.getWindow().getGuiScaledWidth();
          int k = windowWidth / 2 - 91;
          int j = 21;
          int xOffset = ClientChampionsConfig.hudXOffset;
          int yOffset = ClientChampionsConfig.hudYOffset;
          String colorCode = rank.getB();
          int color = ARGB.opaque(Rank.getColor(colorCode));

          ChampionsOverlay.startX = xOffset + k;
          ChampionsOverlay.startY = yOffset + 1;

          guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GUI_BAR_TEXTURES, xOffset + k, yOffset + j, 0, 60, 182, 5, 256, 256, color);
          int healthOffset =
            (int) ((livingEntity.getHealth() / livingEntity.getMaxHealth()) * 183.0F);

          if (healthOffset > 0) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GUI_BAR_TEXTURES, xOffset + k, yOffset + j, 0, 65, healthOffset, 5, 256,
              256, color);
          }

          if (championLevel <= 18) {
            int startStarsX = xOffset + windowWidth / 2 - 5 - 5 * (championLevel - 1);

            for (int tier = 0; tier < championLevel; tier++) {
              guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GUI_STAR, startStarsX, yOffset + 1, 0, 0, 9, 9, 9, 9, color);
              startStarsX += 10;
            }
          } else {
            int startStarsX = xOffset + windowWidth / 2 - 5;
            String count = "x" + championLevel;
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GUI_STAR, startStarsX - client.font.width(count) / 2,
              yOffset + 1, 0, 0, 9, 9, 9, 9, color);
            guiGraphics.drawString(client.font, count,
              (int) (startStarsX + 10 - client.font.width(count) / 2.0F), yOffset + 2,
              16777215, true);
          }
          Component customName = livingEntity.getCustomName();
          String name;

          if (customName == null) {
            name = Component.translatable("rank.champions.title." + championLevel).getString();
            name += " " + livingEntity.getName().getString();
          } else {
            name = customName.getString();
          }
          guiGraphics.drawString(client.font, name,
            (int) (xOffset + (float) (windowWidth / 2 - client.font.width(name) / 2)),
            (int) (yOffset + (float) (j - 9)), color, true);
          StringBuilder builder = new StringBuilder();

          for (var affix : affixSet) {
            builder.append(
              Component.translatable(affix).getString());
            builder.append(" ");
          }
          String affixes = builder.toString().trim();
          guiGraphics.drawString(client.font, affixes,
            (int) (xOffset + (float) (windowWidth / 2 - client.font.width(affixes) / 2)),
            (int) (yOffset + (float) (j + 6)), 16777215, true);
          return true;
        }
        return false;
      }).orElse(false);
    }).orElse(false);
  }

  public static Identifier getGuiStar() {
    return GUI_STAR;
  }

}
