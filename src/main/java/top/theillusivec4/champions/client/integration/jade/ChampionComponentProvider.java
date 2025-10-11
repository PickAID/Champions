package top.theillusivec4.champions.client.integration.jade;

import mcp.mobius.waila.api.IEntityAccessor;
import mcp.mobius.waila.api.IEntityComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.RenderableTextComponent;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
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

	private static TextComponent getChampionName(Tuple<Integer, String> rank, IChampion champion) {
		return (TextComponent) Utils.translatable("rank.champions.title." + rank.getA()).append(" " + champion.getLivingEntity().getName().getString()).withStyle(Style.EMPTY.withColor(Color.fromRgb(Rank.getColor(rank.getB()))));
	}

	private static TextComponent getChampionDescription(IAffix affix) {
		return Utils.translatable(affix.toLanguageKey());
	}

	@Override
	public void appendHead(List<ITextComponent> tooltip, IEntityAccessor entityAccessor, IPluginConfig config) {
		ChampionCapability.getCapability(entityAccessor.getEntity()).ifPresent(champion -> {
			IChampion.Client clientChampion = champion.getClient();
			if (ChampionHelper.isValidChampion(clientChampion)) {
				champion.getClient().getRank().ifPresent(rank -> {
					// replace champion name from original name
					tooltip.set(0, getChampionName(rank, champion));
				});
			}
		});
	}

	@Override
	public void appendBody(List<ITextComponent> iTooltip, IEntityAccessor entityAccessor, IPluginConfig config) {
		ChampionCapability.getCapability(entityAccessor.getEntity()).ifPresent(champion -> {
			IChampion.Client clientChampion = champion.getClient();
			if (ChampionHelper.isValidChampion(clientChampion)) {
				champion.getClient().getRank().ifPresent(rank -> {
					// replace champion name from original name
					// iTooltip.set(1, getChampionName(rank, champion)); // moved to append HEAD
					// add star to jade, based on rank
					CompoundNBT starElement = StarElement.of(rank.getA(), rank.getB(), ClientChampionsConfig.jadeStarSpacing);
					// add new line for star element(bellow name, at heart info top)
					iTooltip.add(1, createIconComponent(starElement));
				});
				List<IAffix> affixes = champion.getClient().getAffixes();
				int lineCount = ClientChampionsConfig.lineCount;
				List<ITextComponent> components = new ArrayList<>();

				for (int i = 0; i < affixes.size(); i += lineCount) {
					int end = Math.min(i + lineCount, affixes.size());
					String line = affixes.subList(i, end).stream()
							.map(a -> getChampionDescription(a).getString())
							.collect(Collectors.joining(" ")); // 自动加空格
					components.add(Utils.literal(line));
				}
				for (ITextComponent component : components) {
					iTooltip.add(3, component);
				}
			}
		});
	}
	private RenderableTextComponent createIconComponent(CompoundNBT data) {
		return new RenderableTextComponent(JadePlugin.STAR_RENDERER, data);
	}

}
