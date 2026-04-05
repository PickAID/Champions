package top.theillusivec4.champions.champion.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import top.theillusivec4.champions.champion.*;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.data.lang.LanguageKeys;
import top.theillusivec4.champions.data.lang.LanguageUtil;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 专用于物品的冠军处理程序
 */
@Deprecated
public interface ChampionHandlerItem extends ChampionHandler, TooltipProvider {
	ItemStack itemStack();

	@Override
	default boolean isBoss() {
		return this.itemStack().getOrDefault(top.theillusivec4.champions.component.DataComponents.BOSS, false);
	}

	@Override
	default void setBoss(boolean boss) {
		if (boss) {
			this.itemStack().set(top.theillusivec4.champions.component.DataComponents.BOSS, true);
		} else {
			this.itemStack().remove(top.theillusivec4.champions.component.DataComponents.BOSS);
		}
	}

	@Override
	default AffixContainer getAffixes() {
		return this.itemStack().getOrDefault(top.theillusivec4.champions.component.DataComponents.AFFIX_CONTAINER_STORED, AffixContainer.EMPTY);
	}

	@Override
	default void setAffixes(AffixContainer affixContainer) {
		this.itemStack().set(top.theillusivec4.champions.component.DataComponents.AFFIX_CONTAINER_STORED, affixContainer);
	}

	@Override
	default int getLevel() {
		return this.itemStack().getOrDefault(top.theillusivec4.champions.component.DataComponents.LEVEL, 1);
	}

	@Override
	default void setLevel(int level) {
		if (level >= 1) {
			this.itemStack().set(top.theillusivec4.champions.component.DataComponents.LEVEL, level);
		}
	}

	@Override
	default int getColor() {
		return this.itemStack().getOrDefault(top.theillusivec4.champions.component.DataComponents.COLOR, ChampionHelper.byLevelColor(this.getLevel()));
	}

	@Override
	default void setColor(int color) {
		this.itemStack().set(top.theillusivec4.champions.component.DataComponents.COLOR, color);
	}

	@Override
	default Component getPrefix() {
		return this.itemStack().getOrDefault(top.theillusivec4.champions.component.DataComponents.PREFIX, ChampionHelper.getPrefixComponent(this.getLevel()));
	}

	@Override
	default void setPrefixName(Component name) {
		this.itemStack().set(top.theillusivec4.champions.component.DataComponents.PREFIX, name);
	}

	@Override
	default ChampionData save() {
		return new ChampionData(
				Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.PREFIX)),
				Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.AFFIX_CONTAINER_STORED)),
				this.itemStack().has(top.theillusivec4.champions.component.DataComponents.AFFIX_CONTAINER_STORED) ? Optional.of(this.getLevel()) : Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.LEVEL)),
				Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.COLOR)),
				Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.BOSS))
		);
	}

	@Override
	default void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
		if (this.itemStack().has(top.theillusivec4.champions.component.DataComponents.AFFIX_CONTAINER_STORED)) {
			AffixContainer affixContainer = this.getAffixes();
			int level = this.getLevel();
			int color = this.getColor();
			Component prefixName = this.getPrefix();
			boolean boss = this.isBoss();

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_LEVEL).withStyle(ChatFormatting.GRAY)
					.append(ChampionHelper.getLevelComponent(level)));

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_COLOR).withStyle(ChatFormatting.GRAY)
					.append(ChampionHelper.getColorComponent(color)));

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_PREFIX_NAME).withStyle(ChatFormatting.GRAY)
					.append(prefixName));

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_BOSS).withStyle(ChatFormatting.GRAY)
					.append(LanguageUtil.getBossStatusComponent(boss)));

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_AFFIXES).withStyle(ChatFormatting.GRAY));
			for (Holder<Affix> affix : affixContainer.getAffixList()) {
				consumer.accept(CommonComponents.space().append(affix.value().description()));
			}
		}
	}

}
