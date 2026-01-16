package top.theillusivec4.champions.champion.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandom;
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
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.attachment.Attachments;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.ChampionDefaultConfigs;
import top.theillusivec4.champions.champion.ChampionHandler;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.server.champion.ChampionConfig;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        this.setBossEvent(new ServerChampionBossEvent(Mth.createInsecureUUID(this.entity().getRandom()), name, this.getHealth() / this.getMaxHealth(), this.getLevelOrDefault(), this.getColorOrDefault().getValue(), this.getAffixesOrDefault().getAffixes()));
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

  default void doFinalizeSpawn(ServerLevel level, double x, double y, double z, DifficultyInstance difficultyInstance, EntitySpawnReason reason) {
          /*
            思路：区域难度 -> 等级 -> Rank（提供前缀名，每等级颜色，等级区间（根据前一步计算出的等级判断是否可用），词缀，战利品表等）
            简述：生物（Mob）在生成初始化阶段时（FinalizeSpawn，处理生物装备盔甲，僵尸鸡骑士，灾厄队长等的阶段）
                  获取所在位置的区域难度，从区域难度计算该生物的冠军等级，从等级寻找可用的Rank，并从所有可用的Rank中随机抽取并应用于生物。

            例：猎杀者
                等级：[1, 3]
                颜色：[color_1, color_2, color_3] , default_color
                词缀：[]
                战利品表：LootTableId 用于额外的掉落

            难度映射等级计算方式：
              Minecraft区域难度，简单：[0.75-1.5]，普通：[1.5-4.0]，困难：[2.25-6.75]
              将区域难度进行取整得到原始等级，并将原始等级钳制在最小最大等级之间，目前为[1, 5]
              而后根据计算得出的等级，匹配可用的Rank，并为实体应用。

            等级映射词缀数量计算方式：
              总词缀数量 = 等级依赖函数：
                              最小值：基础词缀数量（默认为1） + 除去第一级外的每级数量（默认为1）
                              最大值：5
       */
    if (!this.isValid()) {
      RandomSource random = level.getRandom();
      // 等级
      MutableInt mutableInt = new MutableInt(Math.clamp((int) difficultyInstance.getEffectiveDifficulty(), 1, 5));
      Optional.ofNullable(Champions.getInstance().getLevelConfigManager().byLevel(level)).ifPresent(championConfig -> mutableInt.setValue(championConfig.calculateLevel(difficultyInstance.getDifficulty(), mutableInt.get().intValue())));
      this.getConfig().ifPresent(championConfig -> mutableInt.setValue(championConfig.calculateLevel(difficultyInstance.getDifficulty(), mutableInt.get().intValue())));
      int championLevel = mutableInt.get().intValue();
      this.setLevel(championLevel);

      // 头衔
      Registry<Rank> ranks = level.registryAccess().lookupOrThrow(Registries.RANK);
      Registry<Affix> affixes = level.registryAccess().lookupOrThrow(Registries.AFFIX);
      Holder<Rank> rank = WeightedRandom.getRandomItem(random, ranks.stream().filter(rank1 -> rank1.matches(championLevel)).toList(), Rank::weight).map(ranks::wrapAsHolder).orElse(null);
      if (rank != null) {
        this.setRank(rank);
        this.setColor(rank.value().getColor(championLevel));
        // 词缀
        List<Holder<Affix>> list = affixes.stream().map(affixes::wrapAsHolder).collect(Collectors.toList());
        Collections.shuffle(list);
        this.updateAffixes(mutable -> {
          int i = 0;
          for (Holder<Affix> affix : list) {
            mutable.add(affix);
            i++;
            if (i >= championLevel) {
              break;
            }
          }
        });
        this.setBoss(rank.value().boss());
      }

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


