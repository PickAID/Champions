package top.theillusivec4.champions.champion;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.server.champion.config.ChampionDefaultConfigs;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * 冠军处理程序
 */
public interface ChampionHandler {
  /**
   * 运行初始化词缀效果
   *
   * @param serverLevel 服务端维度
   * @param entity      实体
   * @param origin      位置
   */
  void runInitializeEffects(ServerLevel serverLevel, Entity entity, Vec3 origin);

  /**
   * 停止初始化词缀效果
   *
   * @param serverLevel 服务端维度
   * @param entity      实体
   * @param origin      位置
   */
  void stopInitializeEffects(ServerLevel serverLevel, Entity entity, Vec3 origin);

  /**
   * 是否免疫伤害
   *
   * @param serverLevel  服务端维度
   * @param damageSource 伤害源
   * @return 是 | 否
   */
  boolean isImmuneToDamage(ServerLevel serverLevel, DamageSource damageSource);

  /**
   * 获取伤害减免值，允许返回负数以实现受到更多伤害
   *
   * @param serverLevel  服务端维度
   * @param damageSource 伤害源
   * @return 伤害减免值
   */
  float getDamageProtection(ServerLevel serverLevel, DamageSource damageSource);

  /**
   * 修改击退值
   *
   * @param serverLevel  服务端维度
   * @param damageSource 伤害源
   * @param knockback    原始击退值
   * @return 修改后的击退值
   */
  float modifyKnockback(ServerLevel serverLevel, DamageSource damageSource, float knockback);

  /**
   * 修改伤害值
   *
   * @param serverLevel  服务端维度
   * @param target       攻击目标
   * @param damageSource 伤害源
   * @param amount       原始伤害量
   * @return 修改后的伤害量
   */
  float modifyDamage(ServerLevel serverLevel, Entity target, DamageSource damageSource, float amount);

  /**
   * 修改治疗值
   *
   * @param serverLevel 服务端维度
   * @param amount      原始治疗值
   * @return 修改后的治疗值
   */
  float modifyHeal(ServerLevel serverLevel, float amount);

  /**
   * 执行攻击后的效果
   *
   * @param serverLevel  服务端维度
   * @param targetType   作用目标类型，对受伤者 | 攻击者 | 直接攻击者 依次触发
   * @param victim       受伤者
   * @param damageSource 伤害源
   */
  void doPostAttackEffects(ServerLevel serverLevel, AffixTarget targetType, Entity victim, DamageSource damageSource);

  /**
   * 执行每个刻度的效果
   *
   * @param serverLevel 服务端维度
   */
  void tickEffects(ServerLevel serverLevel);

  /**
   * 迭代词缀以执行方法
   *
   * @param consumer 执行的方法
   */
  void runIteration(Consumer<Holder<Affix>> consumer);

  /**
   * 更新词缀
   * 为了确保更新词缀后再进行序列化与网络同步等操作，以及确保数据对象的不可变性，故采用此法
   *
   * @param consumer 更新
   */
  void updateAffixes(Consumer<Affixes.Mutable> consumer);

  /**
   * 对当前处理程序应用冠军配置数据。
   */
  default void applyConfig(ChampionConfig config) {
    config.rank().ifPresent(this::setRank);
    config.prefixName().ifPresent(this::setPrefixName);
    config.level().ifPresent(this::setLevel);
    config.color().ifPresent(this::setColor);
    config.boss().ifPresent(this::setBoss);
    config.affixes().ifPresent(affixes -> this.updateAffixes(mutable -> mutable.addAll(affixes.getAffixes())));
  }

  /**
   * 派生当前处理程序的冠军配置数据。
   */
  default ChampionConfig deriveConfig() {
    return new ChampionConfig(
      this.getRank(),
      this.getPrefixName(),
      this.getAffixes(),
      this.getLevel(),
      this.getColor(),
      this.isBoss()
    );
  }

  /**
   * 获取全部词缀数据
   */
  Optional<Affixes> getAffixes();

  /**
   * 获取全部词缀数据
   */
  Affixes getAffixesOrDefault();

  /**
   * 获取等级
   */
  Optional<Integer> getLevel();

  /**
   * 设置当前等级
   * 等级会钳制在默认最小最大值之间
   */
  void setLevel(int level);

  /**
   * 获得当前等级 如果未设置会返回默认值
   */
  int getLevelOrDefault();

  /**
   * 是否为首领怪物
   */
  Optional<Boolean> isBoss();

  /**
   * 是否为首领怪物，如果是，当作为刷怪蛋生成生物时会为其设置服务端BossBar数据。
   */
  boolean isBossOrDefault();

  /**
   * 设置是否为首领怪物。
   */
  void setBoss(boolean boss);

  /**
   * 获取颜色值
   */
  Optional<Integer> getColor();

  /**
   * 设置颜色值，内部自动调用ARGB.opaque(color)
   */
  void setColor(int color);

  /**
   * 获取可能为默认值的颜色
   */
  default int getColorOrDefault() {
    return getColor().orElse(ChampionDefaultConfigs.DEFAULT_COLOR);
  }

  /**
   * 获取头衔
   */
  Optional<Holder<Rank>> getRank();

  /**
   * 设置头衔，如果头衔是EMPTY则无效果
   */
  void setRank(Holder<Rank> rank);

  /**
   * 获取前缀名
   */
  Optional<Component> getPrefixName();

  /**
   * 设置前缀名
   */
  void setPrefixName(@Nullable Component name);

  /**
   * 一般指该对象是否真的具有数据，如果所有数据均为空则为无效
   */
  default boolean isValid() {
    return !this.getAffixesOrDefault().isEmpty() || this.getLevel().isPresent() || this.getColor().isPresent() || this.isBoss().isPresent();
  }
}
