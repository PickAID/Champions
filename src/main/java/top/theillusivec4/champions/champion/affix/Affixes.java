package top.theillusivec4.champions.champion.affix;

import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.advancements.criterion.TagPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;
import top.theillusivec4.champions.champion.affix.effect.*;
import top.theillusivec4.champions.champion.value.based.lootcontext.LevelBasedValue;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.tag.AffixTags;
import top.theillusivec4.champions.util.Util;
import top.theillusivec4.champions.world.loot.predicates.LatestDamageCondition;

@SuppressWarnings("SpellCheckingInspection")
public interface Affixes {
  ResourceKey<Affix> ADAPTABLE = register("adaptable");
  ResourceKey<Affix> ARCTIC = register("arctic");
  ResourceKey<Affix> DAMPENING = register("dampening");
  ResourceKey<Affix> DESECRATING = register("desecrating");
  ResourceKey<Affix> ENKINDLING = register("enkindling");
  ResourceKey<Affix> HASTY = register("hasty");
  ResourceKey<Affix> INFESTED = register("infested");
  ResourceKey<Affix> KNOCKING = register("knocking");
  ResourceKey<Affix> LIVELY = register("lively");
  ResourceKey<Affix> MAGNETIC = register("magnetic");
  ResourceKey<Affix> MOLTEN = register("molten");
  ResourceKey<Affix> PARALYZING = register("paralyzing");
  ResourceKey<Affix> PLAGUED = register("plagued");
  ResourceKey<Affix> REFLECTIVE = register("reflective");
  ResourceKey<Affix> SHIELDING = register("shielding");
  ResourceKey<Affix> WOUNDING = register("wounding");
  ResourceKey<Affix> TEST_1 = register("test_1");
  ResourceKey<Affix> TEST_2 = register("test_2");
  ResourceKey<Affix> TEST_3 = register("test_3");
  ResourceKey<Affix> TEST_4 = register("test_4");
  ResourceKey<Affix> TEST_5 = register("test_5");
  ResourceKey<Affix> TEST_6 = register("test_6");
  ResourceKey<Affix> TEST_7 = register("test_7");

  static void bootstrap(BootstrapContext<Affix> context) {
    HolderGetter<Affix> affixes = context.lookup(Registries.AFFIX);
    HolderGetter<DamageType> damageTypes = context.lookup(net.minecraft.core.registries.Registries.DAMAGE_TYPE);
    /*
      测试新功能用例。
     */
    register(
      context,
      TEST_1,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.TARGET,
          AffixEntityEffect.projection(
            ProjectileProvider.arrow(),
            new ItemStack(Items.ARROW),
            LevelBasedValue.constant(1.2f),
            LevelBasedValue.constant(2),
            Holder.direct(SoundEvents.ARROW_SHOOT)
          ),
          TimeCheck.time(IntRange.exact(0)).setPeriod(40)
        )
    );
    register(
      context,
      TEST_2,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.TARGET,
          AffixEntityEffect.projection(
            ProjectileProvider.smallFireball(),
            new ItemStack(Items.ARROW),
            LevelBasedValue.constant(1.2f),
            LevelBasedValue.constant(2),
            Holder.direct(SoundEvents.ARROW_SHOOT)
          ),
          TimeCheck.time(IntRange.exact(0)).setPeriod(40)
        )
    );
    register(
      context,
      TEST_3,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.TARGET,
          AffixEntityEffect.projection(
            ProjectileProvider.fireworkRocket(),
            new ItemStack(Items.ARROW),
            LevelBasedValue.constant(1.2f),
            LevelBasedValue.constant(2),
            Holder.direct(SoundEvents.ARROW_SHOOT)
          ),
          TimeCheck.time(IntRange.exact(0)).setPeriod(40)
        )
    );
    register(
      context,
      TEST_4,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.TARGET,
          AffixEntityEffect.projection(
            ProjectileProvider.shulkerBullet(),
            new ItemStack(Items.ARROW),
            LevelBasedValue.constant(0),
            LevelBasedValue.constant(2),
            Holder.direct(SoundEvents.ARROW_SHOOT)
          ),
          TimeCheck.time(IntRange.exact(0)).setPeriod(40)
        )
    );
    register(
      context,
      TEST_5,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.TARGET,
          AffixEntityEffect.projection(
            ProjectileProvider.largeFireball(),
            new ItemStack(Items.ARROW),
            LevelBasedValue.constant(1.2f),
            LevelBasedValue.constant(2),
            Holder.direct(SoundEvents.ARROW_SHOOT)
          ),
          TimeCheck.time(IntRange.exact(0)).setPeriod(40)
        )
    );
    /*
      适应
      旧：当伤害类型与上一次所受伤害类型相同时，伤害减免 = 0.15*所受同一伤害类型的伤害次数

      新：当伤害类型与上一次所受伤害类型相同时，每一级提供0.15伤害减免
     */
    register(
      context,
      ADAPTABLE,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.DAMAGE_PROTECTION,
          AffixValueEffect.add(
            LevelBasedValue.linear(
              LevelBasedValue.constant(0.15f),
              LevelBasedValue.constant(0.15f)
            )
          ),
          LatestDamageCondition.builder()
        )
        .exclusiveWith(affixes.getOrThrow(AffixTags.DAMAGE_PROTECTION_EXCLUSIVE))
    );
    /*
      抑制
        旧：对直接伤害具有0.8伤害减免

        新：每一级对直接伤害具有0.2伤害减免
     */
    register(
      context,
      DAMPENING,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.DAMAGE_PROTECTION,
          AffixValueEffect.add(
            LevelBasedValue.linear(
              LevelBasedValue.constant(0.2f),
              LevelBasedValue.constant(0.2f)
            )
          ),
          DamageSourceCondition.hasDamageSource(
            new DamageSourcePredicate.Builder()
              .isDirect(true)
          )
        )
        .exclusiveWith(affixes.getOrThrow(AffixTags.DAMAGE_PROTECTION_EXCLUSIVE))
    );
    /*
      仓促
        旧：0.25移动速度属性 0.5击退属性

        新：每一级增加0.05移动速度属性 0.1击退属性
     */
    register(
      context,
      HASTY,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withEffects(
          AffixEffectComponents.ATTRIBUTES,
          new AffixAttributeEffect(
            HASTY.identifier(),
            Attributes.MOVEMENT_SPEED,
            LevelBasedValue.linear(
              LevelBasedValue.constant(0.05f),
              LevelBasedValue.constant(0.05f)
            ),
            AttributeModifier.Operation.ADD_VALUE
          )
        ).withConditionalEffects(
          AffixEffectComponents.KNOCKBACK,
          AffixValueEffect.add(
            LevelBasedValue.linear(
              LevelBasedValue.constant(0.1f),
              LevelBasedValue.constant(0.1f)
            )
          )
        )
    );
    /*
      爆震
        旧：带有该词缀的实体攻击后，对被攻击实体造成缓慢效果100刻的缓慢III效果，造成5.0强度的击退

        新：带有该词缀的实体攻击后，每级对被攻击实体造成1-2秒的I至III级缓慢效果，每级造成1.0强度的击退
     */
    register(
      context,
      KNOCKING,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withTargetedConditionalEffects(
          AffixEffectComponents.POST_ATTACK,
          AffixTarget.ATTACKER,
          AffixTarget.VICTIM,
          AffixEntityEffect.applyMobEffect(
            HolderSet.direct(MobEffects.SLOWNESS),
            LevelBasedValue.linear(
              LevelBasedValue.constant(1),
              LevelBasedValue.constant(2)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(1),
              LevelBasedValue.constant(2)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(0),
              LevelBasedValue.constant(2)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(0),
              LevelBasedValue.constant(2)
            )
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.KNOCKBACK,
          AffixValueEffect.add(
            LevelBasedValue.linear(
              LevelBasedValue.constant(1.0f),
              LevelBasedValue.constant(1.0f)
            )
          )
        )
        .exclusiveWith(affixes.getOrThrow(AffixTags.DAMAGE_EXCLUSIVE))
    );
    /*
      活力
        旧：每60刻，实体获得1刻治疗状态效果

        新：每60刻，实体获得自身等级的生命恢复状态1-3秒
     */
    register(
      context,
      LIVELY,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.TICK,
          AffixEntityEffect.applyMobEffect(
            HolderSet.direct(MobEffects.REGENERATION),
            LevelBasedValue.linear(
              LevelBasedValue.constant(1),
              LevelBasedValue.constant(3)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(1),
              LevelBasedValue.constant(3)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(0),
              LevelBasedValue.constant(1)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(0),
              LevelBasedValue.constant(1)
            )
          ),
          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(60)
        )
    );
    register(
      context,
      TEST_6,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.TARGET,
          AffixEntityEffect.projection(
            ProjectileProvider.arcticBullet(),
            new ItemStack(Items.ARROW),
            LevelBasedValue.constant(0),
            LevelBasedValue.constant(2),
            Holder.direct(SoundEvents.SHULKER_SHOOT)
          ),
          TimeCheck.time(IntRange.exact(0)).setPeriod(40)
        )
    );
    register(
      context,
      TEST_7,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.TARGET,
          AffixEntityEffect.movement(0.05)
        )
    );
    /*
      熔融
        旧：获得永久0级火焰抗性状态效果，攻击后点燃目标实体20秒，造成上一次所受伤害值(未计算伤害减免的原伤害值)的伤害

        新：免疫火焰伤害，拥有该词条的实体攻击后点燃被攻击实体等级*2秒，每级造成2点火焰伤害
     */
    register(
      context,
      MOLTEN,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.DAMAGE_IMMUNITY,
          DamageImmunity.INSTANCE,
//          AffixLocationBasedEffect.applyMobEffect(
//            MobEffects.FIRE_RESISTANCE,
//            LevelBasedValue.constant(-1),
//            LevelBasedValue.constant(0),
//            true
//          ),
          DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_FIRE)))
        )
        .withTargetedConditionalEffects(
          AffixEffectComponents.POST_ATTACK,
          AffixTarget.ATTACKER,
          AffixTarget.VICTIM,
          AllOf.entityEffects(
            AffixEntityEffect.damageEntity(
              LevelBasedValue.linear(
                LevelBasedValue.constant(2.0f),
                LevelBasedValue.constant(2.0f)
              ),
              LevelBasedValue.linear(
                LevelBasedValue.constant(2.0f),
                LevelBasedValue.constant(2.0f)
              ),
//              LevelBasedValues.lootParam(LootParamSourceTypes.Floats.LATEST_DAMAGE_AMOUNT),
//              LevelBasedValues.lootParam(LootParamSourceTypes.Floats.LATEST_DAMAGE_AMOUNT),
              damageTypes.getOrThrow(DamageTypes.ON_FIRE)
            ),
            AffixEntityEffect.ignite(
              LevelBasedValue.linear(
                LevelBasedValue.constant(2.0f),
                LevelBasedValue.constant(2.0f)
              )
            )
          )
        )
        .exclusiveWith(affixes.getOrThrow(AffixTags.DAMAGE_IMMUNITY_EXCLUSIVE))
    );
    /*
      瘫痪
        旧：攻击后如果受伤实体没有瘫痪状态效果，则对被攻击的目标造成60秒0级的瘫痪状态效果

        新：攻击后如果受伤实体没有瘫痪状态效果，则对被攻击的目标造成等级*1秒0级的瘫痪状态效果
     */
    register(
      context,
      PARALYZING,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withTargetedConditionalEffects(
          AffixEffectComponents.POST_ATTACK,
          AffixTarget.ATTACKER,
          AffixTarget.VICTIM,
          AffixEntityEffect.applyMobEffect(
            HolderSet.direct(top.theillusivec4.champions.world.effect.MobEffects.PARALYSIS),
            LevelBasedValue.linear(
              LevelBasedValue.constant(1),
              LevelBasedValue.constant(1)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(1),
              LevelBasedValue.constant(1)
            ),
            LevelBasedValue.constant(0),
            LevelBasedValue.constant(0)
          ),
          LootItemEntityPropertyCondition.hasProperties(
            LootContext.EntityTarget.ATTACKER,
            new EntityPredicate.Builder()
              .effects(
                new MobEffectsPredicate.Builder()
                  .and(top.theillusivec4.champions.world.effect.MobEffects.PARALYSIS.getDelegate())
              )
          ).invert()
        )
        .exclusiveWith(affixes.getOrThrow(AffixTags.DAMAGE_EXCLUSIVE))
    );
    /*
      瘟疫
      旧：受到攻击后对攻击者造成10-15秒的1级中毒效果（从0级起计数）
          每20刻在自身位置生成ParticleTypes.ENTITY_EFFECT（中毒状态效果）粒子
          每20刻对周身水平距离5个距离内实体造成1-5秒的0级中毒效果

      新：受到攻击后每级对攻击者造成等级*2秒的与等级同级的中毒效果
          每20刻在自身位置生成ParticleTypes.ENTITY_EFFECT（中毒状态效果）粒子
          每20刻对周身水平距离5个距离内实体造成等级*1秒的0级中毒效果
     */
    register(
      context,
      PLAGUED,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withTargetedConditionalEffects(
          AffixEffectComponents.POST_ATTACK,
          AffixTarget.VICTIM,
          AffixTarget.ATTACKER,
          AffixEntityEffect.applyMobEffect(
            HolderSet.direct(MobEffects.POISON),
            LevelBasedValue.linear(
              LevelBasedValue.constant(2),
              LevelBasedValue.constant(2)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(2),
              LevelBasedValue.constant(2)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(0),
              LevelBasedValue.constant(1)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(0),
              LevelBasedValue.constant(1)
            )
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.TICK,
          AffixEntityEffect.allOf(
            AffixEntityEffect.spawnParticles(
              ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(125, ARGB.red(8889187), ARGB.green(8889187), ARGB.blue(8889187))),
              0,
              AffixEntityEffects.SpawnParticlesEffect.PositionSource.inBoundingBox(),
              AffixEntityEffects.SpawnParticlesEffect.PositionSource.inBoundingBox(),
              AffixEntityEffects.SpawnParticlesEffect.VelocitySource.INSTANCE,
              AffixEntityEffects.SpawnParticlesEffect.VelocitySource.INSTANCE,
              ConstantFloat.of(1.0f)
            ),
            AffixEntityEffect.iterationEntity(
              5.0,
              0.0,
              null,
              AffixEntityEffect.applyMobEffect(
                HolderSet.direct(MobEffects.POISON),
                LevelBasedValue.linear(
                  LevelBasedValue.constant(1),
                  LevelBasedValue.constant(1)
                ),
                LevelBasedValue.linear(
                  LevelBasedValue.constant(1),
                  LevelBasedValue.constant(1)
                ),
                LevelBasedValue.constant(0),
                LevelBasedValue.constant(0)
              )
            )
          ),
          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(20)
        )
    );
    /*
      保护
        旧：每40刻生成一次ParticleTypes.ENTITY_EFFECT粒子
          每40刻具有一次伤害免疫
          每40刻具有一次受到攻击时播放SoundEvents.PLAYER_ATTACK_NODAMAGE（轻击）声音

        新：每40刻获得一次持续0-2秒保护状态效果，每个等级增加0.5秒最小持续时间
            行为迁移至该状态效果，该状态效果免疫一次伤害后移除，该状态效果的等级暂无效果
     */
    register(
      context,
      SHIELDING,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
        .withConditionalEffects(
          AffixEffectComponents.TICK,
          AffixEntityEffect.applyMobEffect(
            HolderSet.direct(top.theillusivec4.champions.world.effect.MobEffects.SHIELD),
            LevelBasedValue.linear(
              LevelBasedValue.constant(0.5f),
              LevelBasedValue.constant(0.5f)
            ),
            LevelBasedValue.constant(2.0f),
            LevelBasedValue.constant(0.0f),
            LevelBasedValue.constant(0.0f)
          ),
//          AffixEntityEffect.spawnParticles(
//            ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1),
//            0,
//            AffixEntityEffects.SpawnParticlesEffect.PositionSource.inBoundingBox(),
//            AffixEntityEffects.SpawnParticlesEffect.PositionSource.inBoundingBox(),
//            AffixEntityEffects.SpawnParticlesEffect.VelocitySource.INSTANCE,
//            AffixEntityEffects.SpawnParticlesEffect.VelocitySource.INSTANCE,
//            ConstantFloat.of(1.0f)
//          ),
          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(40)
        )
        .exclusiveWith(affixes.getOrThrow(AffixTags.DAMAGE_PROTECTION_EXCLUSIVE))
//        .withTargetedConditionalEffects(
//          AffixEffectComponents.POST_ATTACK,
//          AffixTarget.VICTIM,
//          AffixTarget.VICTIM,
//          AffixEntityEffect.playSound(
//            List.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.PLAYER_ATTACK_NODAMAGE)),
//            ConstantFloat.of(1.0f),
//            ConstantFloat.of(1.0f)
//          ),
//          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(40)
//        )
//        .withConditionalEffects(
//          AffixEffectComponents.DAMAGE_IMMUNITY,
//          DamageImmunity.INSTANCE,
//          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(40)
//        )
    );
    /*
      创伤
        旧：受到治疗时，如果自身存在MobEffects.WOUND_EFFECT_TYPE（创伤）状态效果，则治疗量减半
        受到攻击时，如果自身存在MobEffects.WOUND_EFFECT_TYPE（创伤）状态效果，则受到1.5倍的伤害
        受到攻击后，有0.4的几率使自身获得20秒0级的MobEffects.WOUND_EFFECT_TYPE（创伤）状态效果

        新：受到攻击后，有0.5的几率使自身获得等级*2秒与等级同级的创伤状态效果
        其他行为迁移至该状态效果
     */
    register(
      context,
      WOUNDING,
      Affix.affix(
          Affix.definition(
            null,
            5,
            5
          )
        )
//        .withConditionalEffects(
//          AffixEffectComponents.HEAL,
//          AffixValueEffect.multiply(LevelBasedValue.constant(0.5f)),
//          LootItemEntityPropertyCondition.hasProperties(
//            LootContext.EntityTarget.THIS,
//            new EntityPredicate.Builder()
//              .effects(
//                MobEffectsPredicate.Builder.effects()
//                  .and(top.theillusivec4.champions.world.effect.MobEffects.WOUND_EFFECT_TYPE)
//              )
//          )
//        )
//        .withConditionalEffects(
//          AffixEffectComponents.DAMAGE_PROTECTION,
//          AffixValueEffect.add(LevelBasedValue.constant(-0.5f)),
//          LootItemEntityPropertyCondition.hasProperties(
//            LootContext.EntityTarget.THIS,
//            new EntityPredicate.Builder()
//              .effects(
//                MobEffectsPredicate.Builder.effects()
//                  .and(top.theillusivec4.champions.world.effect.MobEffects.WOUND_EFFECT_TYPE)
//              )
//          )
//        )
        .withTargetedConditionalEffects(
          AffixEffectComponents.POST_ATTACK,
          AffixTarget.VICTIM,
          AffixTarget.VICTIM,
          AffixEntityEffect.applyMobEffect(
            HolderSet.direct(top.theillusivec4.champions.world.effect.MobEffects.WOUND),
            LevelBasedValue.linear(
              LevelBasedValue.constant(2),
              LevelBasedValue.constant(2)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(2),
              LevelBasedValue.constant(2)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(0),
              LevelBasedValue.constant(1)
            ),
            LevelBasedValue.linear(
              LevelBasedValue.constant(0),
              LevelBasedValue.constant(1)
            )
          ),
          LootItemRandomChanceCondition.randomChance(0.5f)
        )
    );
  }

  private static void register(BootstrapContext<Affix> context, ResourceKey<Affix> key, Affix.Builder builder) {
    context.register(key, builder.build(key.identifier()));
  }

  private static ResourceKey<Affix> register(String name) {
    return ResourceKey.create(Registries.AFFIX, Util.id(name));
  }
}
