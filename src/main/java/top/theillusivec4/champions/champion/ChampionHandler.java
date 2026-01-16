package top.theillusivec4.champions.champion;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.AffixEffectComponents;
import top.theillusivec4.champions.champion.affix.effect.AffixTarget;
import top.theillusivec4.champions.champion.affix.effect.ConditionalEffect;
import top.theillusivec4.champions.champion.affix.effect.DamageImmunity;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.world.loot.parameters.LootContextParamSets;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 冠军处理程序
 */
public interface ChampionHandler {
  /**
   * 是否为首领怪物
   */
  Optional<Boolean> isBoss();

  /**
   * 删除头衔
   *
   */
  void removeRank();

  /**
   * 删除前缀名
   *
   */
  void removePrefixName();

  /**
   * 更新词缀
   *
   * @param updater 更新
   */
  default void updateAffixes(Consumer<Affixes.Mutable> updater) {
    Affixes.Mutable mutable = this.getAffixesOrDefault().toMutable();
    updater.accept(mutable);
    this.setAffixes(mutable.toImmutable());
  }

  /**
   * 停止初始化词缀效果
   *
   * @param level  服务端维度
   * @param entity 实体
   * @param origin 位置
   */
  default void stopLocationChangedEffects(ServerLevel level, Entity entity, Vec3 origin) {
    this.runIteration(affix -> affix.value().stopLocationChangedEffects(level, this.getLevelOrDefault(), entity, origin));
  }

  /**
   * 运行初始化词缀效果
   *
   * @param level        服务端维度
   * @param entity       实体
   * @param origin       位置
   * @param becameActive
   */
  default void runLocationChangedEffects(ServerLevel level, Entity entity, Vec3 origin, boolean becameActive) {
    this.runIteration(affix -> affix.value().runLocationChangedEffects(level, this.getLevelOrDefault(), entity, origin, becameActive));
  }

  default void forEachModifier(ServerLevel level, BiConsumer<Holder<Attribute>, AttributeModifier> action) {
    this.runIteration(affix -> affix.value().forEachModifier(this.getLevelOrDefault(), action));
  }

  /**
   * 是否免疫伤害
   *
   * @param serverLevel 服务端维度
   * @param victim      受伤实体
   * @param source      伤害源
   * @return 是 | 否
   */
  default boolean isImmuneToDamage(ServerLevel serverLevel, Entity victim, DamageSource source) {
    for (Holder<Affix> affix : this.getAffixesOrDefault().getAffixes()) {
      LootContext context = LootContextParamSets.damageImmunity(serverLevel, victim, this.getLevelOrDefault(), source, null, null, null);
      for (ConditionalEffect<DamageImmunity> effect : affix.value().getEffects(AffixEffectComponents.DAMAGE_IMMUNITY)) {
        if (effect.matches(context)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 获取伤害减免值，允许返回负数以实现受到更多伤害
   *
   * @param level  服务端维度
   * @param victim 受伤实体
   * @param source 伤害源
   * @return 伤害减免值
   */
  default float getDamageProtection(ServerLevel level, Entity victim, DamageSource source) {
    MutableFloat mutableFloat = new MutableFloat(0.0f);
    this.runIteration(affix -> affix.value().modifyDamageProtection(level, this.getLevelOrDefault(), victim, source, mutableFloat));
    return mutableFloat.floatValue();
  }

  /**
   * 修改击退值
   *
   * @param level     服务端维度
   * @param victim    受伤实体
   * @param source    伤害源
   * @param knockback 原始击退值
   * @return 修改后的击退值
   */
  default float modifyKnockback(ServerLevel level, Entity victim, DamageSource source, float knockback) {
    MutableFloat mutableFloat = new MutableFloat(knockback);
    this.runIteration(affix -> affix.value().modifyKnockback(level, this.getLevelOrDefault(), victim, source, mutableFloat));
    return mutableFloat.floatValue();
  }

  /**
   * 修改伤害值
   *
   * @param level  服务端维度
   * @param victim 受伤实体
   * @param source 伤害源
   * @param amount 原始伤害量
   * @return 修改后的伤害量
   */
  default float modifyDamage(ServerLevel level, Entity victim, DamageSource source, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    this.runIteration(affix -> affix.value().modifyDamage(level, this.getLevelOrDefault(), victim, source, mutableFloat));
    return mutableFloat.floatValue();
  }

  /**
   * 修改治疗值
   *
   * @param serverLevel 服务端维度
   * @param victim      受伤的实体
   * @param amount      原始治疗值
   * @return 修改后的治疗值
   */
  default float modifyHeal(ServerLevel serverLevel, Entity victim, float amount) {
    MutableFloat mutableFloat = new MutableFloat(amount);
    this.runIteration(affix -> affix.value().modifyHeal(serverLevel, this.getLevelOrDefault(), victim, mutableFloat));
    return mutableFloat.floatValue();
  }

  /**
   * 执行攻击后的效果
   *
   * @param level  服务端维度
   * @param target 作用目标类型：受伤者 | 攻击者 | 直接攻击者
   * @param victim 受伤者
   * @param source 伤害源
   */
  default void doPostAttackEffects(ServerLevel level, AffixTarget target, Entity victim, DamageSource source) {
    this.runIteration(affix -> affix.value().doPostAttack(level, this.getLevelOrDefault(), target, victim, source));
  }

  /**
   * 执行每个刻度的效果
   *
   * @param level  服务端维度
   * @param entity
   */
  default void tickEffects(ServerLevel level, Entity entity) {
    this.runIteration(affix -> affix.value().tick(level, this.getLevelOrDefault(), entity));
  }

  /**
   * 迭代词缀以执行方法
   *
   * @param consumer 执行的方法
   */
  default void runIteration(Consumer<Holder<Affix>> consumer) {
    this.getAffixes().ifPresent(affixes -> affixes.getAffixes().forEach(consumer));
  }

  /**
   * 对当前处理程序应用冠军配置数据。
   */
  default void applyData(ChampionData data) {
    data.rank().ifPresent(this::setRank);
    data.prefixName().ifPresent(this::setPrefixName);
    data.level().ifPresent(this::setLevel);
    data.color().ifPresent(this::setColor);
    data.affixes().ifPresent(affixes -> this.updateAffixes(mutable -> mutable.addAll(affixes.getAffixes())));
    data.boss().ifPresent(this::setBoss); // 这里需要用到词条数据
  }

  /**
   * 派生当前处理程序的冠军配置数据。
   */
  default ChampionData deriveData() {
    return new ChampionData(
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
   * 设置词缀对象
   */
  void setAffixes(Affixes affixes);

  /**
   * 获取全部词缀数据
   */
  default Affixes getAffixesOrDefault() {
    return this.getAffixes().orElse(Affixes.EMPTY);
  }

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
  default int getLevelOrDefault() {
    return this.getLevel().orElse(1);
  }

  /**
   * 是否为首领怪物，如果是，当作为刷怪蛋生成生物时会为其设置服务端BossBar数据。
   */
  default boolean isBossOrDefault() {
    return this.isBoss().orElse(false);
  }

  /**
   * 获取颜色值
   */
  Optional<TextColor> getColor();

  /**
   * 设置颜色值
   */
  void setColor(TextColor color);

  /**
   * 获取可能为默认值的颜色
   */
  default TextColor getColorOrDefault() {
    return Objects.requireNonNull(getColor().orElse(TextColor.fromLegacyFormat(ChatFormatting.WHITE)));
  }

  /**
   * 获取头衔
   */
  Optional<Holder<Rank>> getRank();

  /**
   * 设置头衔
   */
  void setRank(Holder<Rank> rank);

  /**
   * 获取前缀名
   */
  Optional<Component> getPrefixName();

  /**
   * 设置前缀名
   */
  void setPrefixName(Component name);

  /**
   * 一般指该对象是否真的具有数据，如果所有数据均为空则为无效
   */
  default boolean isValid() {
    return !this.getAffixesOrDefault().isEmpty() || this.getLevel().isPresent() || this.getColor().isPresent() || this.isBoss().isPresent();
  }

  /**
   * 设置是否为首领怪物。
   */
  void setBoss(boolean boss);
}
