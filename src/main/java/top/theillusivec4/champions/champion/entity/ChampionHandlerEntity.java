package top.theillusivec4.champions.champion.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.attachment.Attachments;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.ChampionHandler;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.Damage;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.champion.ChampionDefaultConfigs;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

import java.util.Optional;

/**
 * 专用于实体的冠军处理程序
 */
public interface ChampionHandlerEntity extends ChampionHandler {
  Entity entity();

  @Override
  default Optional<Boolean> isBoss() {
    return this.entity().getExistingData(Attachments.BOSS);
  }

  @Override
  default void removeRank() {
    this.entity().removeData(Attachments.RANK);
  }

  @Override
  default void removePrefixName() {
    this.entity().removeData(Attachments.PREFIX_NAME);
  }

  @Override
  default Optional<Affixes> getAffixes() {
    return this.entity().getData(Attachments.AFFIXES);
  }

  @Override
  default void setAffixes(Affixes affixes) {
    if (this.entity().level() instanceof ServerLevel level) {
      if (this.entity() instanceof LivingEntity livingEntity) {
        this.forEachModifier(level, (attribute, modifier) -> {
          AttributeInstance attributeModifier = livingEntity.getAttribute(attribute);
          if (attributeModifier != null) {
            attributeModifier.removeModifier(modifier);
          }
        });
      }
      this.stopLocationChangedEffects(level, this.entity(), this.entity().position());
      this.entity().setData(Attachments.AFFIXES, Optional.of(affixes));
      if (this.entity() instanceof LivingEntity livingEntity) {
        this.forEachModifier(level, (attribute, modifier) -> {
          AttributeInstance attributeModifier = livingEntity.getAttribute(attribute);
          if (attributeModifier != null) {
            attributeModifier.addTransientModifier(modifier);
          }
        });
      }
      this.runLocationChangedEffects(level, entity(), entity().position(), true);
    }
  }

  @Override
  default Optional<Integer> getLevel() {
    if (this.entity().hasData(Attachments.LEVEL)) {
      return Optional.of(this.entity().getData(Attachments.LEVEL));
    } else {
      return this.getRank().map(rank -> rank.value().level());
    }
  }

  @Override
  default void setLevel(int level) {
    if (level <= ChampionDefaultConfigs.DEFAULT_LEVEL) {
      this.entity().removeData(Attachments.LEVEL);
    } else {
      this.entity().setData(Attachments.LEVEL, Math.clamp(level, ChampionDefaultConfigs.MIN_LEVEL, ChampionDefaultConfigs.MAX_LEVEL));
    }
  }

  @Override
  default Optional<Integer> getColor() {
    if (this.entity().hasData(Attachments.COLOR)) {
      return Optional.of(this.entity().getData(Attachments.COLOR));
    }

    return this.getRank().map(rank -> rank.value().color());
  }

  @Override
  default void setColor(int color) {
    this.entity().setData(Attachments.COLOR, ARGB.opaque(color));
  }

  @Override
  default Optional<Holder<Rank>> getRank() {
    return this.entity().getData(Attachments.RANK);
  }

  @Override
  default void setRank(Holder<Rank> rank) {
    this.entity().setData(Attachments.RANK, Optional.of(rank));
  }

  @Override
  default Optional<Component> getPrefixName() {
    if (this.entity().hasData(Attachments.PREFIX_NAME)) {
      return this.entity().getData(Attachments.PREFIX_NAME);
    }

    return this.getRank().map(rank -> rank.value().description());
  }

  @Override
  default void setPrefixName(Component name) {
    this.entity().setData(Attachments.PREFIX_NAME, Optional.of(name));
  }

  @Override
  default void setBoss(boolean boss) {
    // 对客户端来说，该数据是只读的
    if (!this.entity().level().isClientSide()) {
      if (boss) {
        Component name = this.getPrefixName()
          .map(component -> (Component) component.copy()
            .append(CommonComponents.space())
            .append(this.entity().getDisplayName())
          ).orElse(this.entity().getDisplayName());
        this.setBossEvent(new ServerChampionBossEvent(Mth.createInsecureUUID(this.entity().getRandom()), name, this.getHealth() / this.getMaxHealth(), this.getLevelOrDefault(), this.getColorOrDefault(), this.getAffixesOrDefault().getAffixes()));
      } else {
        this.setBossEvent(null);
      }

      this.entity().setData(Attachments.BOSS, boss);
    }
  }

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
   * 获取该冠军实体的刷怪蛋，只有在该实体的冠军配置不为空时才该返回具有数据的刷怪蛋
   *
   * @return 刷怪蛋
   */
  default ItemStack getSpawnEgg() {
    EntityType<?> entityType = this.entity().getType();
    Identifier id = EntityType.getKey(entityType).withSuffix("_spawn_egg");
    Item item = BuiltInRegistries.ITEM.getValue(id);
    //noinspection ConstantValue
    if (item != null && item != Items.AIR) {
      ItemStack itemStack = new ItemStack(item);

      ChampionUtil.getHandler(itemStack).ifPresent(handlerItem -> handlerItem.applyConfig(this.deriveConfig()));

      return itemStack;
    }

    return ItemStack.EMPTY;
  }

  /**
   * 返回该实体的当前生命值
   */
  default float getHealth() {
    return this.entity() instanceof LivingEntity livingEntity ? livingEntity.getHealth() : 1.0f;
  }

  /**
   * 返回该实体的最大生命值，用于BossBar
   */
  default float getMaxHealth() {
    return this.entity() instanceof LivingEntity livingEntity ? livingEntity.getMaxHealth() : 1.0f;
  }
}


