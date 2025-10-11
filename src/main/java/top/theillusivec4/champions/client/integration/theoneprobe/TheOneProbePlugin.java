package top.theillusivec4.champions.client.integration.theoneprobe;


import mcjty.theoneprobe.api.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.util.Utils;

import java.util.function.Function;

public class TheOneProbePlugin implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return Champions.MODID + ":entity.champion";
    }

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player,
                                   World level, Entity entity,
                                   IProbeHitEntityData probeHitEntityData) {

        if (ChampionsConfig.enableTOPIntegration) {
            ChampionCapability.getCapability(entity).ifPresent(champion -> {
                IChampion.Server serverChampion = champion.getServer();
                serverChampion.getRank().ifPresent(rank -> {
                    if (rank.getTier() == 0) {
                        return;
                    }
                    net.minecraft.util.text.Color color = rank.getDefaultColor();
                    int r = ColorHelper.PackedColor.red(color.getValue());
                    int g = ColorHelper.PackedColor.green(color.getValue());
                    int b = ColorHelper.PackedColor.blue(color.getValue());
                    Color rankColor = new Color(r, g, b);
                    IProbeInfo horizontal;
                    IProbeInfo vertical = probeInfo.vertical(
                            probeInfo.defaultLayoutStyle().borderColor(rankColor).spacing(3).padding(3));
                    vertical.mcText(
                            Utils.translatable("rank.champions.title." + rank.getTier()).append(
                                            " (" + rank.getTier() + ")")
                                    .setStyle(Style.EMPTY.withUnderlined(true).withColor(rank.getDefaultColor())));

                    for (IAffix affix : serverChampion.getAffixes()) {
                        horizontal = vertical.horizontal();
                        horizontal.mcText(
		                        Utils.translatable("affix." + Champions.MODID + "." + affix.getIdentifier()));
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
