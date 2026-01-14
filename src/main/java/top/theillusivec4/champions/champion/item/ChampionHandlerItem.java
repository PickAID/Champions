package top.theillusivec4.champions.champion.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.component.TypedEntityData;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.ChampionHandler;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.data.lang.LanguageKeys;
import top.theillusivec4.champions.data.lang.LanguageUtil;
import top.theillusivec4.champions.server.champion.config.ChampionDefaultConfigs;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 专用于物品的冠军处理程序
 */
public interface ChampionHandlerItem extends ChampionHandler, TooltipProvider {
  ItemStack itemStack();

  @Override
  default Optional<Boolean> isBoss() {
    return Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.BOSS));
  }

  @Override
  default void removeRank() {
    this.itemStack().remove(top.theillusivec4.champions.component.DataComponents.RANK);
  }

  @Override
  default void removePrefixName() {
    this.itemStack().remove(top.theillusivec4.champions.component.DataComponents.PREFIX_NAME);
  }

  @Override
  default Optional<Affixes> getAffixes() {
    return Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.AFFIXES));
  }

  @Override
  default void setAffixes(Affixes affixes) {
    this.itemStack().set(top.theillusivec4.champions.component.DataComponents.AFFIXES, affixes);
  }

  @Override
  default Optional<Integer> getLevel() {
    if (this.itemStack().has(top.theillusivec4.champions.component.DataComponents.LEVEL)) {
      return Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.LEVEL));
    }

    return this.getRank().map(rank -> rank.value().level());
  }

  @Override
  default void setLevel(int level) {
    if (level <= ChampionDefaultConfigs.DEFAULT_LEVEL) {
      this.itemStack().remove(top.theillusivec4.champions.component.DataComponents.LEVEL);
    } else {
      this.itemStack().set(top.theillusivec4.champions.component.DataComponents.LEVEL, Math.clamp(level, ChampionDefaultConfigs.MIN_LEVEL, ChampionDefaultConfigs.MAX_LEVEL));
    }
  }

  @Override
  default Optional<Integer> getColor() {
    if (this.itemStack().has(top.theillusivec4.champions.component.DataComponents.COLOR)) {
      return Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.COLOR));
    }

    return this.getRank().map(rank -> rank.value().color());
  }

  @Override
  public default void setColor(int color) {
    this.itemStack().set(top.theillusivec4.champions.component.DataComponents.COLOR, ARGB.opaque(color));
  }

  @Override
  default Optional<Holder<Rank>> getRank() {
    return Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.RANK));
  }

  @Override
  default void setRank(Holder<Rank> rank) {
    this.itemStack().set(top.theillusivec4.champions.component.DataComponents.RANK, rank);
  }

  @Override
  default Optional<Component> getPrefixName() {
    if (this.itemStack().has(top.theillusivec4.champions.component.DataComponents.PREFIX_NAME)) {
      return Optional.ofNullable(this.itemStack().get(top.theillusivec4.champions.component.DataComponents.PREFIX_NAME));
    }

    return this.getRank().map(rank -> rank.value().description());
  }

  @Override
  default void setPrefixName(Component name) {
    this.itemStack().set(top.theillusivec4.champions.component.DataComponents.PREFIX_NAME, name);
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
  default void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
    // 等级
    Optional<Integer> optional = this.getLevel();
    optional.ifPresent(level -> consumer.accept(
      Component.translatable(LanguageKeys.TOOLTIP_LEVEL_KEY).withStyle(ChatFormatting.GRAY)
        .append(LanguageUtil.getLevelComponent(level).withColor(this.getColorOrDefault()))
    ));
    // 颜色
    Optional<Integer> optional1 = this.getColor();
    optional1.ifPresent(color -> consumer.accept(
      Component.translatable(LanguageKeys.TOOLTIP_COLOR_KEY).withStyle(ChatFormatting.GRAY)
        .append(LanguageUtil.getColorComponent(color))
    ));
    // 前缀
    Optional<Component> optional2 = this.getPrefixName();
    optional2.ifPresent(component -> consumer.accept(
      Component.translatable(LanguageKeys.TOOLTIP_PREFIX_NAME_KEY).withStyle(ChatFormatting.GRAY)
        .append(component.copy().withColor(this.getColorOrDefault()))
    ));
    // 首领
    Optional<Boolean> optional3 = this.isBoss();
    optional3.ifPresent(boss -> consumer.accept(
      Component.translatable(LanguageKeys.TOOLTIP_BOSS_KEY).withStyle(ChatFormatting.GRAY)
        .append(boss ? Component.translatable(LanguageKeys.TOOLTIP_IS_BOSS_KEY) : Component.translatable(LanguageKeys.TOOLTIP_NOT_BOSS_KEY))
    ));
    // 已有词缀
    Optional<Affixes> optional4 = this.getAffixes();
    optional4.ifPresent(affixes -> {
      if (!affixes.isEmpty()) {
        consumer.accept(Component.translatable(LanguageKeys.TOOLTIP_AFFIXES_KEY).withStyle(ChatFormatting.GRAY));
        for (Holder<Affix> affix : affixes.getAffixes()) {
          consumer.accept(CommonComponents.space().append(affix.value().description()));
        }
      }
    });
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

      return this.getPrefixName().map(component -> component.copy()
        .append(CommonComponents.space())
        .append(Component.translatable(LanguageKeys.ITEM_CHAMPION_SPAWN_EGG_KEY, data.type().getDescription()).withStyle(ChatFormatting.WHITE)));
    }

    return Optional.empty();
  }
}
