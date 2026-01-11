package top.theillusivec4.champions.server.champion.config;

/**
 * 所有的冠军默认值应引用自此
 * 布尔值除外
 */
public final class ChampionDefaultConfigs {
  public static final int MIN_LEVEL = 1; // 最小的冠军等级
  public static final int MAX_LEVEL = 5; // 最大的冠军等级
  public static final int EMPTY_LEVEL = 0; // 这意味着空等级
  public static final int DEFAULT_COLOR = -1; // 默认颜色

  private ChampionDefaultConfigs() {
  }
}
