package top.theillusivec4.champions.champion.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.component.TypedEntityData;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.ChampionData;
import top.theillusivec4.champions.champion.ChampionHandler;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.data.lang.LanguageKeys;
import top.theillusivec4.champions.data.lang.LanguageUtil;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 专用于物品的冠军处理程序
 */
public interface ChampionHandlerItem extends ChampionHandler, TooltipProvider {
	ItemStack itemStack();

	@Override
	default boolean isBoss() {
		return this.itemStack().getOrDefault(top.theillusivec4.champions.component.DataComponents.BOSS, false);
	}

	@Override
	default void setBoss(boolean boss) {
		this.itemStack().set(top.theillusivec4.champions.component.DataComponents.BOSS, boss);
	}

	@Override
	default Affixes getAffixes() {
		return this.itemStack().getOrDefault(top.theillusivec4.champions.component.DataComponents.AFFIXES, Affixes.EMPTY);
	}

	@Override
	default void setAffixes(Affixes affixes) {
		this.itemStack().set(top.theillusivec4.champions.component.DataComponents.AFFIXES, affixes);
	}

	@Override
	default int getLevel() {
		return this.itemStack().getOrDefault(top.theillusivec4.champions.component.DataComponents.LEVEL, 1);
	}

	@Override
	default void setLevel(int level) {
		this.itemStack().set(top.theillusivec4.champions.component.DataComponents.LEVEL, level);
	}

	@Override
	default int getColor() {
		return this.itemStack().getOrDefault(top.theillusivec4.champions.component.DataComponents.COLOR, -1);
	}

	@Override
	default void setColor(int color) {
		this.itemStack().set(top.theillusivec4.champions.component.DataComponents.COLOR, color);
	}

	@Override
	default Component getPrefixName() {
		return this.itemStack().getOrDefault(top.theillusivec4.champions.component.DataComponents.PREFIX_NAME, Component.empty());
	}

	@Override
	default void setPrefixName(Component name) {
		this.itemStack().set(top.theillusivec4.champions.component.DataComponents.PREFIX_NAME, name);
	}

	@Override
	default ChampionData save() {
		return new ChampionData(
				Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.PREFIX_NAME)),
				Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.AFFIXES)),
				Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.LEVEL)),
				Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.COLOR)),
				Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.BOSS))
		);
	}

	@Override
	default void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
		if (this.itemStack().has(top.theillusivec4.champions.component.DataComponents.AFFIXES)) {
			Affixes affixes = this.getAffixes();
			int level = this.getLevel();
			int color = this.getColor();
			Component prefixName = this.getPrefixName();
			boolean boss = this.isBoss();

			Component.translatable(LanguageKeys.TOOLTIP_LEVEL_KEY).withStyle(ChatFormatting.GRAY)
					.append(LanguageUtil.getLevelComponent(level).withColor(color));

			Component.translatable(LanguageKeys.TOOLTIP_COLOR_KEY).withStyle(ChatFormatting.GRAY)
					.append(LanguageUtil.getColorComponent(color));

			Component.translatable(LanguageKeys.TOOLTIP_PREFIX_NAME_KEY).withStyle(ChatFormatting.GRAY)
					.append(prefixName.copy().withColor(color));

			Component.translatable(LanguageKeys.TOOLTIP_BOSS_KEY).withStyle(ChatFormatting.GRAY)
					.append(boss ? Component.translatable(LanguageKeys.TOOLTIP_IS_BOSS_KEY) : Component.translatable(LanguageKeys.TOOLTIP_NOT_BOSS_KEY));

			consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_AFFIXES_KEY).withStyle(ChatFormatting.GRAY));
			for (Holder<Affix> affix : affixes.getAffixes()) {
				consumer.accept(CommonComponents.space().append(affix.value().description()));
			}
		}
	}

	default Optional<Component> getDisplayName() {
		if (this.isValid()) {
			TypedEntityData<EntityType<?>> data = this.itemStack().get(DataComponents.ENTITY_DATA);
			if (this.itemStack().has(DataComponents.CUSTOM_NAME)) {
				return Optional.empty();
			}

			if (data == null) {
				return Optional.empty();
			}

			Component prefixName = this.getPrefixName();
			if (prefixName.getSiblings().isEmpty()) {
				return Optional.of(Component.translatable(LanguageKeys.ITEM_CHAMPION_SPAWN_EGG_KEY, data.type().getDescription()).withStyle(ChatFormatting.WHITE));
			} else {
				return Optional.of(prefixName.copy().append(CommonComponents.space()).append(Component.translatable(LanguageKeys.ITEM_CHAMPION_SPAWN_EGG_KEY, data.type().getDescription()).withStyle(ChatFormatting.WHITE)));
			}
		}

		return Optional.empty();
	}
}
