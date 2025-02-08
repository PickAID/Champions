package top.theillusivec4.champions.client.integration.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.impl.ui.TextElement;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.common.util.Utils;

import java.util.ArrayList;

public enum ChampionComponentProvider implements IEntityComponentProvider {
    INSTANCE;

    private static Component getChampionName(Tuple<Integer, String> rank, IChampion champion) {
        return Component.translatable("rank.champions.title." + rank.getA()).append(" " + champion.getLivingEntity().getName().getString()).withStyle(Style.EMPTY.withColor(Rank.getColor(rank.getB())));
    }

    private static Component getChampionDescription(IAffix affix) {
        return Component.translatable(affix.toLanguageKey());
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        ChampionCapability.getCapability(entityAccessor.getEntity()).ifPresent(champion -> {
            var clientChampion = champion.getClient();
            if (ChampionHelper.isValidChampion(clientChampion)) {
                champion.getClient().getRank().ifPresent(rank -> {
                    // replace champion name from original name
                    iTooltip.get(0, IElement.Align.LEFT).set(0, new TextElement(getChampionName(rank, champion)));
                    // add star to jade, based on rank
                    var starElement = StarElement.of(rank.getA(), rank.getB(), ClientChampionsConfig.jadeStarSpacing);
                    // add new line for star element(bellow name, at heart info top)
                    iTooltip.add(1, starElement);
                });
                var affixes = champion.getClient().getAffixes();
                ArrayList<Component> components = new ArrayList<>();
                StringBuilder line = new StringBuilder();
                for (int i = 0; i < affixes.size(); i++) {
                    line.append(getChampionDescription(affixes.get(i)).getString());
                    if ((i + 1) % ClientChampionsConfig.lineCount == 0 || i == affixes.size() - 1) {
                        // 达到指定数量或是最后一个词条时添加行并清空
                        components.add(Component.literal(line.toString()));
                        line.setLength(0); // 清空 StringBuilder
                    } else {
                        line.append(" "); // 添加空格分隔符
                    }
                }
                iTooltip.addAll(components);
            }
        });
    }

    @Override
    public ResourceLocation getUid() {
        return Utils.getLocation("enable_affix_compact");
    }
}
