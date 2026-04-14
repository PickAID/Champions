package top.theillusivec4.champions.deprecated.common.integration.theoneprobe;

import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.deprecated.api.IAffix;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.common.capability.ChampionAttachment;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;

import java.util.function.Function;

public class TheOneProbePlugin implements IProbeInfoEntityProvider {

  @Override
  public String getID() {
    return ChampionsMod.MOD_ID + ":entity.champion";
  }

  @Override
  public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player,
                                 Level level, Entity entity,
                                 IProbeHitEntityData probeHitEntityData) {

    if (ChampionsConfig.enableTOPIntegration) {
      ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
        IChampion.Server serverChampion = champion.getServer();
        serverChampion.getRank().ifPresent(rank -> {
          if (rank.getTier() == 0) {
            return;
          }
          int color = rank.getDefaultColor();
          int r = (color >> 16) & 0xFF;
          int g = (color >> 8) & 0xFF;
          int b = (color) & 0xFF;
          Color rankColor = new Color(r, g, b);
          IProbeInfo horizontal;
          IProbeInfo vertical = probeInfo.vertical(
            probeInfo.defaultLayoutStyle().borderColor(rankColor).spacing(3).padding(3)
          );
          vertical.mcText(
            Component.translatable("rank.champions.title." + rank.getTier()).append(
                " (" + rank.getTier() + ")")
              .setStyle(Style.EMPTY.withUnderlined(true).withColor(rank.getDefaultColor())));

          for (IAffix affix : serverChampion.getAffixes()) {
            horizontal = vertical.horizontal();
            horizontal.mcText(
              Component.translatable("affix." + ChampionsMod.MOD_ID + "." + affix.getIdentifier()));
          }
        });
      });
    }
  }

  public static final class GetTheOneProbe implements Function<ITheOneProbe, Void> {

    @Override
    public Void apply(final ITheOneProbe input) {
      final IProbeInfoEntityProvider instance = new TheOneProbePlugin();
      input.registerEntityProvider(instance);
      return null;
    }
  }
}
