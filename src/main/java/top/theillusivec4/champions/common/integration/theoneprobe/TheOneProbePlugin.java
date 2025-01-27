package top.theillusivec4.champions.common.integration.theoneprobe;

import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.config.ChampionsConfig;

import java.util.function.Function;

public class TheOneProbePlugin implements IProbeInfoEntityProvider {

    @Override
    public String getID() {
        return Champions.MODID + ":entity.champion";
    }

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player,
                                   Level level, Entity entity,
                                   IProbeHitEntityData probeHitEntityData) {

        if (ChampionsConfig.enableTOPIntegration) {
            ChampionCapability.getCapability(entity).ifPresent(champion -> {
                IChampion.Server serverChampion = champion.getServer();
                serverChampion.getRank().ifPresent(rank -> {
                    if (rank.getTier() == 0) {
                        return;
                    }
                    var color = rank.getDefaultColor();
                    int r = FastColor.ARGB32.red(color.getValue());
                    int g = FastColor.ARGB32.green(color.getValue());
                    int b = FastColor.ARGB32.blue(color.getValue());
                    Color rankColor = new Color(r, g, b);
                    IProbeInfo horizontal;
                    IProbeInfo vertical = probeInfo.vertical(
                            probeInfo.defaultLayoutStyle().borderColor(rankColor).spacing(3).padding(3));
                    vertical.mcText(
                            Component.translatable("rank.champions.title." + rank.getTier()).append(
                                            " (" + rank.getTier() + ")")
                                    .setStyle(Style.EMPTY.withUnderlined(true).withColor(rank.getDefaultColor())));

                    for (IAffix affix : serverChampion.getAffixes()) {
                        horizontal = vertical.horizontal();
                        horizontal.mcText(
                                Component.translatable("affix." + Champions.MODID + "." + affix.getIdentifier()));
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
