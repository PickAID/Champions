package top.theillusivec4.champions.api.affix;

import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;
import top.theillusivec4.champions.api.affix.effect.AffixLocationBasedEffectTypes;
import top.theillusivec4.champions.api.affix.effect.AffixTarget;
import top.theillusivec4.champions.api.affix.effect.AllOf;
import top.theillusivec4.champions.api.affix.effect.DamageImmunity;
import top.theillusivec4.champions.api.affix.effect.entity.AffixEntityEffectTypes;
import top.theillusivec4.champions.api.affix.effect.entity.SpawnParticlesEffect;
import top.theillusivec4.champions.api.affix.effect.value.AffixValueEffectTypes;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootContextBasedValueTypes;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootParamSourceTypes;
import top.theillusivec4.champions.common.loot.predicates.LatestDamageCondition;
import top.theillusivec4.champions.common.registries.Registries;
import top.theillusivec4.champions.common.util.Utils;

import java.util.List;

public final class Affixes {
  public static final ResourceKey<Affix> ADAPTABLE = register("adaptable");
  public static final ResourceKey<Affix> ARCTIC = register("arctic");
  public static final ResourceKey<Affix> DAMPENING = register("dampening");
  public static final ResourceKey<Affix> DESECRATING = register("desecrating");
  public static final ResourceKey<Affix> ENKINDLING = register("enkindling");
  public static final ResourceKey<Affix> HASTY = register("hasty");
  public static final ResourceKey<Affix> INFESTED = register("infested");
  public static final ResourceKey<Affix> KNOCKING = register("knocking");
  public static final ResourceKey<Affix> LIVELY = register("lively");
  public static final ResourceKey<Affix> MAGNETIC = register("magnetic");
  public static final ResourceKey<Affix> MOLTEN = register("molten");
  public static final ResourceKey<Affix> PARALYZING = register("paralyzing");
  public static final ResourceKey<Affix> PLAGUED = register("plagued");
  public static final ResourceKey<Affix> REFLECTIVE = register("reflective");
  public static final ResourceKey<Affix> SHIELDING = register("shielding");
  public static final ResourceKey<Affix> WOUNDING = register("wounding");

  public static void bootstrap(BootstrapContext<Affix> context) {
    HolderGetter<DamageType> damageTypes = context.lookup(net.minecraft.core.registries.Registries.DAMAGE_TYPE);
    // 适应 提供伤害减免=0.15*受到该伤害类型的伤害次数
    register(
      context,
      ADAPTABLE,
      Affix.builder()
        .withConditionalEffects(
          AffixEffectComponentTypes.DAMAGE_PROTECTION,
          AffixValueEffectTypes.add(
            LootContextBasedValueTypes.product(
              LootContextBasedValueTypes.constant(0.15f),
              LootContextBasedValueTypes.lootParam(LootParamSourceTypes.Floats.LATEST_DAMAGE_COUNT)
            )),
          LatestDamageCondition.builder()
        ));
    // 抑制 直接伤害具有0.8免伤
    register(
      context,
      DAMPENING,
      Affix.builder()
        .withConditionalEffects(
          AffixEffectComponentTypes.DAMAGE_PROTECTION,
          AffixValueEffectTypes.add(
            LootContextBasedValueTypes.constant(0.8f)
          ),
          DamageSourceCondition.hasDamageSource(
            new DamageSourcePredicate.Builder()
              .isDirect(true)
          )
        )
    );
    // 仓促 0.25移动速度属性 0.5额外击退
    register(
      context,
      HASTY,
      Affix.builder()
        .withEffects(
          AffixEffectComponentTypes.INITIALIZE,
          AffixLocationBasedEffectTypes.attribute(
            HASTY.identifier(),
            Attributes.MOVEMENT_SPEED,
            LootContextBasedValueTypes.constant(0.25f),
            AttributeModifier.Operation.ADD_VALUE
          )
        ).withConditionalEffects(
          AffixEffectComponentTypes.KNOCKBACK,
          AffixValueEffectTypes.add(
            LootContextBasedValueTypes.constant(0.5f)
          )
        )
    );
    // 击退
    register(
      context,
      KNOCKING,
      Affix.builder()
        .withTargetedConditionalEffects(
          AffixEffectComponentTypes.POST_ATTACK,
          AffixTarget.ATTACKER,
          AffixTarget.VICTIM,
          AffixEntityEffectTypes.applyMobEffect(
            MobEffects.SLOWNESS,
            LootContextBasedValueTypes.constant(100),
            LootContextBasedValueTypes.constant(2),
            false
          )
        )
    );
    // 活性
    register(
      context,
      LIVELY,
      Affix.builder()
        .withConditionalEffects(
          AffixEffectComponentTypes.TICK,
          AffixEntityEffectTypes.applyMobEffect(
            MobEffects.INSTANT_HEALTH,
            LootContextBasedValueTypes.constant(1),
            LootContextBasedValueTypes.constant(1)
          ),
          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(60)
        )
    );
    /*
    熔融
    获得永久0级火焰抗性状态效果
    攻击后点燃目标实体20秒，造成上一次所受伤害值(未计算伤害减免的原伤害值)的伤害
     */
    register(
      context,
      MOLTEN,
      Affix.builder()
        .withEffects(
          AffixEffectComponentTypes.INITIALIZE,
          AffixLocationBasedEffectTypes.applyMobEffect(
            MobEffects.FIRE_RESISTANCE,
            LootContextBasedValueTypes.constant(-1),
            LootContextBasedValueTypes.constant(0),
            true
          )
        )
        .withTargetedConditionalEffects(
          AffixEffectComponentTypes.POST_ATTACK,
          AffixTarget.ATTACKER,
          AffixTarget.VICTIM,
          AllOf.entityEffects(
            AffixEntityEffectTypes.damageEntity(
              LootContextBasedValueTypes.lootParam(LootParamSourceTypes.Floats.LATEST_ORIGINAL_DAMAGE_AMOUNT),
              LootContextBasedValueTypes.lootParam(LootParamSourceTypes.Floats.LATEST_ORIGINAL_DAMAGE_AMOUNT),
              damageTypes.getOrThrow(DamageTypes.IN_FIRE)
            ),
            AffixEntityEffectTypes.ignite(
              LootContextBasedValueTypes.constant(20)
            )
          )
        )
    );
    /*
    瘫痪
    攻击后如果受伤实体没有瘫痪状态效果，则对被攻击的目标造成60秒0级的瘫痪状态效果
     */
    register(
      context,
      PARALYZING,
      Affix.builder()
        .withTargetedConditionalEffects(
          AffixEffectComponentTypes.POST_ATTACK,
          AffixTarget.ATTACKER,
          AffixTarget.VICTIM,
          AffixEntityEffectTypes.applyMobEffect(
            top.theillusivec4.champions.common.effect.MobEffects.PARALYSIS_EFFECT_TYPE,
            LootContextBasedValueTypes.constant(60),
            LootContextBasedValueTypes.constant(0)
          ),
          LootItemEntityPropertyCondition.hasProperties(
            LootContext.EntityTarget.ATTACKER,
            new EntityPredicate.Builder()
              .effects(
                new MobEffectsPredicate.Builder()
                  .and(top.theillusivec4.champions.common.effect.MobEffects.PARALYSIS_EFFECT_TYPE.getDelegate())
              )
          ).invert()
        )
    );
    /*
    瘟疫
    受到攻击后对攻击者造成10-15秒的1级中毒效果（从0级起计数）
    每20刻在自身位置生成ParticleTypes.ENTITY_EFFECT（中毒状态效果）粒子
    每20刻对周身水平距离5个距离内实体造成1-5秒的0级中毒效果
     */
    register(
      context,
      PLAGUED,
      Affix.builder()
        .withTargetedConditionalEffects(
          AffixEffectComponentTypes.POST_ATTACK,
          AffixTarget.VICTIM,
          AffixTarget.ATTACKER,
          AffixEntityEffectTypes.applyMobEffect(
            MobEffects.POISON,
            LootContextBasedValueTypes.constant(10),
            LootContextBasedValueTypes.constant(1)
          )
        )
        .withConditionalEffects(
          AffixEffectComponentTypes.TICK,
          AffixEntityEffectTypes.allOf(
            AffixEntityEffectTypes.spawnParticles(
              ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(125, ARGB.red(8889187), ARGB.green(8889187), ARGB.blue(8889187))),
              0,
              SpawnParticlesEffect.PositionSource.inBoundingBox(),
              SpawnParticlesEffect.PositionSource.inBoundingBox(),
              SpawnParticlesEffect.VelocitySource.INSTANCE,
              SpawnParticlesEffect.VelocitySource.INSTANCE,
              ConstantFloat.of(1.0f)
            ),
            AffixEntityEffectTypes.iterationEntity(
              5.0,
              1.0,
              AffixEntityEffectTypes.applyMobEffect(
                MobEffects.POISON,
                LootContextBasedValueTypes.constant(5),
                LootContextBasedValueTypes.constant(0)
              )
            )
          ),
          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(20)
        )
    );
    /*
      保护
      每40刻生成一次ParticleTypes.ENTITY_EFFECT粒子
      每40刻具有一次伤害免疫
      每40刻具有一次受到攻击时播放SoundEvents.PLAYER_ATTACK_NODAMAGE声音
     */
    register(
      context,
      SHIELDING,
      Affix.builder()
        .withConditionalEffects(
          AffixEffectComponentTypes.TICK,
          AffixEntityEffectTypes.spawnParticles(
            ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 1),
            0,
            SpawnParticlesEffect.PositionSource.inBoundingBox(),
            SpawnParticlesEffect.PositionSource.inBoundingBox(),
            SpawnParticlesEffect.VelocitySource.INSTANCE,
            SpawnParticlesEffect.VelocitySource.INSTANCE,
            ConstantFloat.of(1.0f)
          ),
          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(40)
        )
        .withTargetedConditionalEffects(
          AffixEffectComponentTypes.POST_ATTACK,
          AffixTarget.VICTIM,
          AffixTarget.VICTIM,
          AffixEntityEffectTypes.playSound(
            List.of(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.PLAYER_ATTACK_NODAMAGE)),
            ConstantFloat.of(1.0f),
            ConstantFloat.of(1.0f)
          ),
          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(40)
        )
        .withConditionalEffects(
          AffixEffectComponentTypes.DAMAGE_IMMUNITY,
          DamageImmunity.INSTANCE,
          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(40)
        )
    );
    /*
      创伤
      受到治疗时，如果自身存在MobEffects.WOUND_EFFECT_TYPE状态效果，则治疗量减半
      受到攻击时，如果自身存在MobEffects.WOUND_EFFECT_TYPE状态效果，则受到1.5倍的伤害
      受到攻击后，有0.4的几率使自身获得20秒0级的MobEffects.WOUND_EFFECT_TYPE状态效果
     */
    register(
      context,
      WOUNDING,
      Affix.builder()
        .withConditionalEffects(
          AffixEffectComponentTypes.HEAL,
          AffixValueEffectTypes.multiply(LootContextBasedValueTypes.constant(0.5f)),
          LootItemEntityPropertyCondition.hasProperties(
            LootContext.EntityTarget.THIS,
            new EntityPredicate.Builder()
              .effects(
                MobEffectsPredicate.Builder.effects()
                  .and(top.theillusivec4.champions.common.effect.MobEffects.WOUND_EFFECT_TYPE)
              )
          )
        )
        .withConditionalEffects(
          AffixEffectComponentTypes.DAMAGE_PROTECTION,
          AffixValueEffectTypes.add(LootContextBasedValueTypes.constant(-0.5f)),
          LootItemEntityPropertyCondition.hasProperties(
            LootContext.EntityTarget.THIS,
            new EntityPredicate.Builder()
              .effects(
                MobEffectsPredicate.Builder.effects()
                  .and(top.theillusivec4.champions.common.effect.MobEffects.WOUND_EFFECT_TYPE)
              )
          )
        )
        .withTargetedConditionalEffects(
          AffixEffectComponentTypes.POST_ATTACK,
          AffixTarget.VICTIM,
          AffixTarget.VICTIM,
          AffixEntityEffectTypes.applyMobEffect(
            top.theillusivec4.champions.common.effect.MobEffects.WOUND_EFFECT_TYPE,
            LootContextBasedValueTypes.constant(20),
            LootContextBasedValueTypes.constant(0)
          ),
          LootItemRandomChanceCondition.randomChance(0.4f)
        )
    );
  }

  private static void register(BootstrapContext<Affix> context, ResourceKey<Affix> key, Affix.Builder builder) {
    context.register(key, builder.build(key.identifier()));
  }

  private static ResourceKey<Affix> register(String name) {
    return ResourceKey.create(Registries.AFFIX, Utils.id(name));
  }

  private Affixes() {
  }
}
