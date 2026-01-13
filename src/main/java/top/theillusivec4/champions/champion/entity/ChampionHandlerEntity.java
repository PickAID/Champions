package top.theillusivec4.champions.champion.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.attachment.Attachments;
import top.theillusivec4.champions.champion.ChampionHandler;
import top.theillusivec4.champions.champion.affix.Damage;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

import java.util.Optional;

/**
 * 专用于实体的冠军处理程序
 */
public interface ChampionHandlerEntity extends ChampionHandler {
  Entity entity();

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
   * 更新最近一次伤害数据
   *
   * @param damageType 伤害类型
   * @param amount     伤害量
   */
  default void updateLatestDamage(Holder<DamageType> damageType, float amount) {
    if (this.isLatestDamageType(damageType)) {
      this.setLatestDamageAmount(this.getLatestDamageAmountOrDefault() + amount);
      this.setLatestDamageCount(this.getLatestDamageCountOrDefault() + 1);
    } else {
      this.setLatestDamageType(damageType);
      this.setLatestDamageAmount(amount);
      this.setLatestDamageCount(1);
    }
  }

  default boolean isLatestDamageType(Holder<DamageType> damageType) {
    return this.getLatestDamageType().map(damageType1 -> damageType1 == damageType).orElse(false);
  }

  /**
   * 最近一次受到伤害的类型
   */
  default Optional<Holder<DamageType>> getLatestDamageType() {
    return this.entity().getData(Attachments.DAMAGE_TYPE);
  }

  default void setLatestDamageType(@Nullable Holder<DamageType> damageType) {
    this.entity().setData(Attachments.DAMAGE_TYPE, Optional.ofNullable(damageType));
  }

  /**
   * 最近一次受到伤害的量
   */
  default Optional<Float> getLatestDamageAmount() {
    return this.entity().getData(Attachments.DAMAGE_AMOUNT);
  }

  default void setLatestDamageAmount(float amount) {
    if (amount > 0.0f) {
      this.entity().setData(Attachments.DAMAGE_AMOUNT, Optional.of(amount));
    }
  }

  default float getLatestDamageAmountOrDefault() {
    return this.getLatestDamageAmount().orElse(0.0f);
  }

  /**
   * 最近一次受到相同伤害类型伤害的计数
   */
  default Optional<Integer> getLatestDamageCount() {
    return this.entity().getData(Attachments.DAMAGE_COUNT);
  }

  default void setLatestDamageCount(int count) {
    if (count > 0) {
      this.entity().setData(Attachments.DAMAGE_COUNT, Optional.of(count));
    }
  }

  /**
   * 最近一次受到相同伤害类型伤害的计数
   */
  default int getLatestDamageCountOrDefault() {
    return this.getLatestDamageCount().orElse(0);
  }

  /**
   * 距离最近一次受到伤害的经过的刻
   */
  default Optional<Integer> getLatestDamageTime() {
    return this.entity().getData(Attachments.DAMAGE_TIME);
  }

  default void setLatestDamageTime(int time) {
    if (time > 0) {
      this.entity().setData(Attachments.DAMAGE_TIME, Optional.of(time));
    }
  }

  /**
   * 距离最近一次受到伤害的经过的刻
   */
  default int getLatestDamageTimeOrDefault() {
    return this.getLatestDamageTime().orElse(-1);
  }

  /**
   *
   * @return 获取最近一次伤害数据
   */
  default @Nullable Damage getLatestDamage() {
    return this.getLatestDamageType()
      .map(damageType -> new Damage(damageType, this.getLatestDamageCountOrDefault(), this.getLatestDamageTimeOrDefault(), this.getLatestDamageAmountOrDefault()))
      .orElse(null);
  }

  /**
   * 获取BossBar，仅服务端有效。
   */
  default Optional<ServerChampionBossEvent> getBossEvent() {
    if (!this.entity().level().isClientSide()) {
      return this.entity().getData(Attachments.SERVER_CHAMPION_BOSS_EVENT);
    }
    return Optional.empty();
  }

  default void setBossEvent(@Nullable ServerChampionBossEvent event) {
    Optional<ServerChampionBossEvent> optional = this.entity().setData(Attachments.SERVER_CHAMPION_BOSS_EVENT, Optional.ofNullable(event));
    if (optional != null) {
      optional.ifPresent(ServerChampionBossEvent::removeAllPlayers);
    }
  }

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


