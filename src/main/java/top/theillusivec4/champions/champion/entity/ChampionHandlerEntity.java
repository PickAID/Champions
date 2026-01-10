package top.theillusivec4.champions.champion.entity;

import net.minecraft.world.item.ItemStack;
import top.theillusivec4.champions.champion.ChampionHandler;
import top.theillusivec4.champions.champion.affix.LatestDamage;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 专用于实体的冠军处理程序
 */
public interface ChampionHandlerEntity extends ChampionHandler {
  /**
   * 更新最近一次受伤的数据，用于作为战利品表上下文参数传递
   */
  void updateLatestDamage(Consumer<LatestDamage.Mutable> consumer);

  /**
   * 获取服务端的 BossBar
   */
  Optional<ServerChampionBossEvent> getBossEvent();

  /**
   * 获取冠军实体的刷怪蛋，可能为EMPTY
   */
  ItemStack getSpawnEgg();

  /**
   * 冠军实体是否已经经过生成处理
   * 这个方法命名不太好 也许会改动
   */
  boolean isSpawned();

  /**
   * 设置冠军实体的生成处理标记
   * 这个方法命名不太好 也许会改动
   */
  void setSpawned(boolean spawned);

  /**
   * 返回该实体的当前生命值
   */
  float getHealth();

  /**
   * 返回该实体的最大生命值，用于BossBar
   */
  float getMaxHealth();

  /**
   * 是否应该在视线触及时显示生命值覆盖层
   */
  default boolean isDisplayHealthOverlay() {
    return this.isValid() && !this.isBoss();
  }
}
