package top.theillusivec4.champions.champion.item;

import top.theillusivec4.champions.champion.ChampionHandler;

/**
 * 专用于物品的冠军处理程序
 */
public interface ChampionHandlerItem extends ChampionHandler {
  default boolean isDisplayHoverName() {
    return this.isValid();
  }

  default boolean isDisplayTooltip() {
    return this.isValid();
  }
}
