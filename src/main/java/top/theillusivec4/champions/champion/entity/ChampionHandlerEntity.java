package top.theillusivec4.champions.champion.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.attachment.Attachments;
import top.theillusivec4.champions.champion.*;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.server.champion.ChampionConfig;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

import java.util.List;
import java.util.Optional;

/**
 * 专用于实体的冠军处理程序
 */
@SuppressWarnings("unused")
public interface ChampionHandlerEntity extends ChampionHandler {
  Entity entity();

  @Override
  default Optional<Boolean> isBoss() {
    return this.entity().getExistingData(Attachments.BOSS);
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
        this.forEachModifier((attribute, modifier) -> {
          AttributeInstance attributeModifier = livingEntity.getAttribute(attribute);
          if (attributeModifier != null) {
            attributeModifier.removeModifier(modifier);
          }
        });
      }
      this.stopLocationChangedEffects(level, this.entity(), this.entity().position());
      this.entity().setData(Attachments.AFFIXES, Optional.of(affixes));
      if (this.entity() instanceof LivingEntity livingEntity) {
        this.forEachModifier((attribute, modifier) -> {
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
    return this.entity().getExistingData(Attachments.LEVEL);
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
  default Optional<TextColor> getColor() {
    return this.entity().getData(Attachments.COLOR);
  }

  @Override
  default void setColor(TextColor color) {
    this.entity().setData(Attachments.COLOR, Optional.of(color));
  }

  @Override
  default Optional<Component> getPrefixName() {
    return this.entity().getData(Attachments.PREFIX_NAME);
  }

  @Override
  default void setPrefixName(Component name) {
    this.entity().setData(Attachments.PREFIX_NAME, Optional.of(name));
  }

  @Override
  default void setBoss(boolean boss) {
    // 对客户端来说，该数据是只读的
    if (!this.entity().level().isClientSide()) {
      this.entity().setData(Attachments.BOSS, boss);
      if (boss && this.getBossEvent().isEmpty()) {
        Component name = this.getPrefixName()
          .map(component -> (Component) component.copy()
            .append(CommonComponents.space())
            .append(this.entity().getDisplayName())
          ).orElse(this.entity().getDisplayName());
        this.setBossEvent(
          new ServerChampionBossEvent(
            Mth.createInsecureUUID(this.entity().getRandom()),
            name,
            this.getHealth() / this.getMaxHealth(),
            this.getLevelOrDefault(),
            this.getColorOrDefault().getValue(),
            this.getAffixesOrDefault().getAffixes()
          )
        );
      } else if (!boss && this.getBossEvent().isPresent()) {
        this.removeBossEvent();
      }
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

  default void onFinalizeSpawn(ServerLevel level, double x, double y, double z, DifficultyInstance difficultyInstance, EntitySpawnReason reason) {
    // 召唤
    if (reason == EntitySpawnReason.TRIGGERED) return;
    if (this.isValid()) return;

    int championLevel = ChampionHelper.calculateChampionLevel(level.getRandom(), difficultyInstance);
    if (championLevel > 0) {
      ChampionHelper.selectRank(this.entity(), championLevel, level.registryAccess().lookupOrThrow(Registries.RANK).listElements().map(rankReference -> (Holder<Rank>) rankReference)).ifPresent(rank -> {

        List<Holder<Affix>> list = ChampionHelper.selectAffixes(entity(), championLevel, level.registryAccess().lookupOrThrow(Registries.AFFIX).listElements().map(affix -> (Holder<Affix>) affix));

        this.setLevel(championLevel);

        Affixes.Mutable mutable = this.getAffixes().map(Affixes::toMutable).orElse(new Affixes.Mutable());
        list.forEach(mutable::add);
        this.setAffixes(mutable.toImmutable());

        this.setColor(ChampionHelper.selectColor(championLevel));

        this.setPrefixName(rank.value().description());
        if (rank.value().boss()) {
          this.setBoss(true);
        }
      });
    }
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

  default void removeBossEvent() {
    Optional<ServerChampionBossEvent> optional = this.entity().removeData(Attachments.SERVER_CHAMPION_BOSS_EVENT);
    if (optional != null) {
      optional.ifPresent(ServerChampionBossEvent::removeAllPlayers);
    }
  }

  default Optional<@Nullable ChampionConfig> getConfig() {
    return Optional.ofNullable(Champions.getInstance().getEntityConfigManager().byEntity(this.entity()));
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
   * 获取BossBar，仅服务端有效。
   */
  default Optional<ServerChampionBossEvent> getBossEvent() {
    if (!this.entity().level().isClientSide()) {
      return this.entity().getData(Attachments.SERVER_CHAMPION_BOSS_EVENT);
    }
    return Optional.empty();
  }

  default void setBossEvent(ServerChampionBossEvent event) {
    Optional<ServerChampionBossEvent> optional = this.entity().setData(Attachments.SERVER_CHAMPION_BOSS_EVENT, Optional.of(event));
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

      ChampionUtil.getHandler(itemStack).ifPresent(handlerItem -> handlerItem.applyData(this.deriveData()));

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


