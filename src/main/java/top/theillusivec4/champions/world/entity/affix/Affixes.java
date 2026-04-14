package top.theillusivec4.champions.world.entity.affix;

import net.minecraft.advancements.criterion.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import top.theillusivec4.champions.world.damagesource.ChampionsDamageTypes;
import top.theillusivec4.champions.world.effect.ChampionsMobEffects;
import top.theillusivec4.champions.world.entity.affix.effects.*;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.tags.AffixTags;
import top.theillusivec4.champions.util.ChampionsUtil;

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

	private Affixes() {
	}

	public static void bootstrap(BootstrapContext<Affix> context) {
		HolderGetter<Affix> affixes = context.lookup(ChampionsRegistries.AFFIX);
		HolderGetter<DamageType> damages = context.lookup(Registries.DAMAGE_TYPE);
		HolderGetter<WorldClock> clocks = context.lookup(Registries.WORLD_CLOCK);
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
						.withEffect(
								AffixEffectComponents.DAMAGE_PROTECTION,
								AffixValueEffect.add(
										LevelBasedValue.linear(
												LevelBasedValue.constant(0.15f),
												LevelBasedValue.constant(0.15f)
										)
								)// Condition
						)
						.exclusiveWith(affixes.getOrThrow(AffixTags.DAMAGE_PROTECTION_EXCLUSIVE))
		);
    /*
      严寒
        旧：每60刻发射一个会追踪目标的寒冰子弹
     */
		register(
				context,
				ARCTIC,
				Affix.affix(
								Affix.definition(
										null,
										1,
										5
								)
						)
						.withEffect(
								AffixEffectComponents.TARGET,
								AffixEntityEffect.projection(
										ProjectileTemplate.arcticBullet(),
										new ItemStackTemplate(Items.ARROW),
										LevelBasedValue.constant(0.0f),
										LevelBasedValue.constant(0.0f),
										Holder.direct(SoundEvents.SHULKER_SHOOT)
								),
								TimeCheck.time(clocks.getOrThrow(WorldClocks.OVERWORLD), IntRange.exact(0)).setPeriod(60)
						)
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
						.withEffect(
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
      亵渎
      旧：约60tick在目标实体处生成一次持续10秒，半径4，起效时刻1秒，瞬间伤害区域效果云

      新：60tick向目标实体投掷具有上述效果的滞留型药水瓶
     */
		register(
				context,
				DESECRATING,
				Affix.affix(
								Affix.definition(
										null,
										1,
										5
								)
						)
						.withEffect(
								AffixEffectComponents.TARGET,
								AffixEntityEffect.projection(
										ProjectileTemplate.thrownLingeringPotion(),
										new ItemStackTemplate(Items.LINGERING_POTION.builtInRegistryHolder(), 1, DataComponentPatch.builder().set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.HARMING)).build()),
										LevelBasedValue.constant(1.2f),
										LevelBasedValue.constant(0.0f),
										Holder.direct(SoundEvents.LINGERING_POTION_THROW)
								),
								TimeCheck.time(clocks.getOrThrow(WorldClocks.OVERWORLD),IntRange.exact(0)).setPeriod(60)
						)
		);
    /*
      点燃
        旧：每60刻发射一个会追踪的会点燃实体的子弹攻击目标
     */
		register(
				context,
				ENKINDLING,
				Affix.affix(
								Affix.definition(
										null,
										1,
										5
								)
						)
						.withEffect(
								AffixEffectComponents.TARGET,
								AffixEntityEffect.projection(
										ProjectileTemplate.enkindlingBullet(),
										new ItemStackTemplate(Items.ARROW),
										LevelBasedValue.constant(0.0f),
										LevelBasedValue.constant(0.0f),
										Holder.direct(SoundEvents.SHULKER_SHOOT)
								),
								TimeCheck.time(clocks.getOrThrow(WorldClocks.OVERWORLD),IntRange.exact(0)).setPeriod(60)
						)
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
						.withEffect(
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
						).withEffect(
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
      感染：
        旧：被攻击、治疗、杀死都有概率生成蠹虫
     */
		register(
				context,
				INFESTED,
				Affix.affix(
						Affix.definition(
								null,
								1,
								5
						)
				).withEffect(
						AffixEffectComponents.POST_ATTACK,
						AffixTarget.VICTIM,
						AffixTarget.VICTIM,
						AffixEntityEffect.summonEntity(HolderSet.direct(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SILVERFISH))),
						ValueCheckCondition.hasValue(UniformGenerator.between(0.0f, 10.0f), IntRange.range(5, 10))
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
										1,
										5
								)
						)
						.withEffect(
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
						.withEffect(
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
						.withEffect(
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
								new TimeCheck.Builder(clocks.getOrThrow(WorldClocks.OVERWORLD), IntRange.exact(0)).setPeriod(60)
						)
		);
    /*
      磁性：
        旧：每40刻对目标实体产生吸引力
     */
		register(
				context,
				MAGNETIC,
				Affix.affix(
								Affix.definition(
										null,
										1,
										5
								)
						)
						.withEffect(
								AffixEffectComponents.TARGET,
								AffixEntityEffect.movement(0.05),
								LootItemEntityPropertyCondition.hasProperties(
										LootContext.EntityTarget.THIS,
										new EntityPredicate.Builder()
												.distance(DistancePredicate.absolute(MinMaxBounds.Doubles.atMost(5.0)))
								)
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
						.withEffect(
								AffixEffectComponents.DAMAGE_IMMUNITY,
								DamageImmunity.INSTANCE,
								DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().tag(TagPredicate.is(DamageTypeTags.IS_FIRE)))
						)
						.withEffect(
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
												damages.getOrThrow(DamageTypes.ON_FIRE)
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
						.withEffect(
								AffixEffectComponents.POST_ATTACK,
								AffixTarget.ATTACKER,
								AffixTarget.VICTIM,
								AffixEntityEffect.applyMobEffect(
										HolderSet.direct(ChampionsMobEffects.PARALYSIS),
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
																.and(ChampionsMobEffects.PARALYSIS.getDelegate())
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
						.withEffect(
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
						.withEffect(
								AffixEffectComponents.TICK,
								AffixEntityEffect.allOf(
										AffixEntityEffect.spawnParticles(
												ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, ARGB.color(125, 8889187, 8889187, 8889187)),
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
								new TimeCheck.Builder(clocks.getOrThrow(WorldClocks.OVERWORLD),IntRange.exact(0)).setPeriod(20)
						)
		);
    /*
      反射
        旧：遭遇近战攻击时将攻击反弹回攻击者
        新：反弹伤害值改为等级相关的线性递增函数
     */
		register(
				context,
				REFLECTIVE,
				Affix.affix(
						Affix.definition(
								null,
								5,
								5
						)
				).withEffect(
						AffixEffectComponents.POST_ATTACK,
						AffixTarget.VICTIM,
						AffixTarget.ATTACKER,
						AffixEntityEffect.damageEntity(
								LevelBasedValue.linear(
										LevelBasedValue.constant(1.0f),
										LevelBasedValue.constant(0.5f)
								),
								LevelBasedValue.linear(
										LevelBasedValue.constant(2.0f),
										LevelBasedValue.constant(1.0f)
								),
								damages.getOrThrow(ChampionsDamageTypes.REFLECTION_DAMAGE)
						),
						DamageSourceCondition.hasDamageSource(new DamageSourcePredicate.Builder().isDirect(true))
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
						.withEffect(
								AffixEffectComponents.TICK,
								AffixEntityEffect.applyMobEffect(
										HolderSet.direct(ChampionsMobEffects.SHIELD),
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
								new TimeCheck.Builder(clocks.getOrThrow(WorldClocks.OVERWORLD),IntRange.exact(0)).setPeriod(40)
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
						.withEffect(
								AffixEffectComponents.POST_ATTACK,
								AffixTarget.VICTIM,
								AffixTarget.VICTIM,
								AffixEntityEffect.applyMobEffect(
										HolderSet.direct(ChampionsMobEffects.WOUND),
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
		return ResourceKey.create(ChampionsRegistries.AFFIX, ChampionsUtil.id(name));
	}
}
