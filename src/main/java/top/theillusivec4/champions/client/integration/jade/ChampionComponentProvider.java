package top.theillusivec4.champions.client.integration.jade;

import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElement;
import mcp.mobius.waila.impl.ui.TextElement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Tuple;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.client.config.ClientChampionsConfig;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.common.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum ChampionComponentProvider implements IEntityComponentProvider {
	INSTANCE;

	private static Component getChampionName(Tuple<Integer, String> rank, IChampion champion) {
		return Utils.translatable("rank.champions.title." + rank.getA()).append(" " + champion.getLivingEntity().getName().getString()).withStyle(Style.EMPTY.withColor(Rank.getColor(rank.getB())));
	}

	private static Component getChampionDescription(IAffix affix) {
		return Utils.translatable(affix.toLanguageKey());
	}

	@Override
	public void appendTooltip(ITooltip iTooltip, mcp.mobius.waila.api.EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
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
				int lineCount = ClientChampionsConfig.lineCount;
				List<Component> components = new ArrayList<>();

				for (int i = 0; i < affixes.size(); i += lineCount) {
					int end = Math.min(i + lineCount, affixes.size());
					String line = affixes.subList(i, end).stream()
							.map(a -> getChampionDescription(a).getString())
							.collect(Collectors.joining(" ")); // 自动加空格
					components.add(Utils.literal(line));
				}
				for (Component component : components) {
					iTooltip.add(3, component);
				}
			}
		});
	}
}
