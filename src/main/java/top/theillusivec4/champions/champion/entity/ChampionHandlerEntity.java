package top.theillusivec4.champions.champion.entity;

import net.minecraft.world.entity.Entity;
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
  Entity entity();
  /**
   * 更新最近一次受伤的数据，用于作为战利品表上下文参数传递
   */
  void updateLatestDamage(Consumer<LatestDamage.Mutable> consumer);

  /**
   * 是否应该在视线触及时显示生命值覆盖层
   */
  default boolean displayHealthOverlay() {
    return this.isValid() && !this.isBossOrDefault();
  }

  /**
   * 应该生成粒子效果吗
   */
  default boolean spawnParticles() {
    return this.isValid();
  }

  /**
   * 应该在生成时选择冠军配置数据并应用吗
   */
  default boolean finalizeSpawn() {
    return !this.isValid();
  }

  /**
   * 获取BossBar，仅服务端有效。
   */
  Optional<ServerChampionBossEvent> getBossEvent();

  /**
   * 获取冠军实体的刷怪蛋，可能为EMPTY
   */
  ItemStack getSpawnEgg();

  /**
   * 返回该实体的当前生命值
   */
  float getHealth();

  /**
   * 返回该实体的最大生命值，用于BossBar
   */
  float getMaxHealth();
}


