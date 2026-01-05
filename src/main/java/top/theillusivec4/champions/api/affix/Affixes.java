package top.theillusivec4.champions.api.affix;

import net.minecraft.advancements.criterion.DamageSourcePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;
import top.theillusivec4.champions.api.affix.effect.AffixEffectComponentTypes;
import top.theillusivec4.champions.api.affix.effect.AffixTarget;
import top.theillusivec4.champions.api.affix.effect.AllOf;
import top.theillusivec4.champions.api.affix.effect.AttributeEffect;
import top.theillusivec4.champions.api.affix.effect.entity.AffixEntityEffectTypes;
import top.theillusivec4.champions.api.affix.effect.entity.SpawnParticlesEffect;
import top.theillusivec4.champions.api.affix.effect.value.AffixValueEffectTypes;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootContextBasedValueTypes;
import top.theillusivec4.champions.api.affix.lootcontextbasedvalue.LootParamSourceTypes;
import top.theillusivec4.champions.common.loot.predicates.LatestDamageCondition;
import top.theillusivec4.champions.common.registries.Registries;
import top.theillusivec4.champions.common.util.Utils;

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
          AffixEffectComponentTypes.ATTRIBUTES,
          new AttributeEffect(
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
            HolderSet.direct(MobEffects.SLOWNESS),
            LootContextBasedValueTypes.constant(100),
            LootContextBasedValueTypes.constant(100),
            LootContextBasedValueTypes.constant(2),
            LootContextBasedValueTypes.constant(2)
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
            HolderSet.direct(MobEffects.INSTANT_HEALTH),
            LootContextBasedValueTypes.constant(1),
            LootContextBasedValueTypes.constant(1),
            LootContextBasedValueTypes.constant(1),
            LootContextBasedValueTypes.constant(1)
          ),
          new TimeCheck.Builder(IntRange.exact(0)).setPeriod(60)
        )
    );
    // 熔融
    register(
      context,
      MOLTEN,
      Affix.builder()
        .withConditionalEffects(
          AffixEffectComponentTypes.SPAWN,
          AffixEntityEffectTypes.applyMobEffect(
            HolderSet.direct(MobEffects.FIRE_RESISTANCE),
            LootContextBasedValueTypes.constant(-1),
            LootContextBasedValueTypes.constant(-1),
            LootContextBasedValueTypes.constant(0),
            LootContextBasedValueTypes.constant(0)
          )
        )
        .withTargetedConditionalEffects(
          AffixEffectComponentTypes.POST_ATTACK,
          AffixTarget.ATTACKER,
          AffixTarget.ATTACKER,
          AllOf.entityEffects(
            AffixEntityEffectTypes.damageEntity(
              LootContextBasedValueTypes.constant(5.0f), // 这里本应该造成所受到伤害量的攻击
              LootContextBasedValueTypes.constant(5.0f),
              damageTypes.getOrThrow(DamageTypes.IN_FIRE)
            ),
            AffixEntityEffectTypes.ignite(
              LootContextBasedValueTypes.constant(200)
            )
          )
        )
    );
    // 瘫痪
    register(
      context,
      PARALYZING,
      Affix.builder()
        .withTargetedConditionalEffects(
          AffixEffectComponentTypes.POST_ATTACK,
          AffixTarget.ATTACKER,
          AffixTarget.VICTIM,
          AffixEntityEffectTypes.applyMobEffect(
            HolderSet.direct(top.theillusivec4.champions.common.effect.MobEffects.PARALYSIS_EFFECT_TYPE.getDelegate()),
            LootContextBasedValueTypes.constant(60),
            LootContextBasedValueTypes.constant(60),
            LootContextBasedValueTypes.constant(0),
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
    // 瘟疫
    register(
      context,
      PLAGUED,
      Affix.builder()
        .withTargetedConditionalEffects(
          AffixEffectComponentTypes.POST_ATTACK,
          AffixTarget.VICTIM,
          AffixTarget.ATTACKER,
          AffixEntityEffectTypes.applyMobEffect(
            HolderSet.direct(MobEffects.POISON),
            LootContextBasedValueTypes.constant(300),
            LootContextBasedValueTypes.constant(300),
            LootContextBasedValueTypes.constant(1),
            LootContextBasedValueTypes.constant(1)
          )
        )
        .withConditionalEffects(
          AffixEffectComponentTypes.TICK,
          AffixEntityEffectTypes.allOf(
            AffixEntityEffectTypes.spawnParticles(
              ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(125, ARGB.red(8889187), ARGB.green(8889187), ARGB.blue(8889187))),
              0,
              new SpawnParticlesEffect.PositionSource(SpawnParticlesEffect.PositionSourceType.BOUNDING_BOX, 0.0f, 1.0f),
              new SpawnParticlesEffect.PositionSource(SpawnParticlesEffect.PositionSourceType.BOUNDING_BOX, 0.0f, 1.0f),
              new SpawnParticlesEffect.VelocitySource(1.0f, ConstantFloat.ZERO),
              new SpawnParticlesEffect.VelocitySource(1.0f, ConstantFloat.ZERO),
              ConstantFloat.of(1.0f)
            )
          )
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
