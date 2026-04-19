package top.theillusivec4.champions.world.entity.affix.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.ProjectileTemplate;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.api.affix.effect.AffixEntityEffect;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public final class AffixEntityEffects {
	private static final DeferredRegister<MapCodec<? extends AffixEntityEffect>> DEFERRED_REGISTER = DeferredRegister.create(ChampionsRegistries.AFFIX_ENTITY_EFFECT_TYPE, Champions.MOD_ID);
	public static final Supplier<MapCodec<DamageEntityEffect>> DAMAGE_ENTITY = register("damage_entity", DamageEntityEffect.MAP_CODEC);
	public static final Supplier<MapCodec<AllOf.EntityEffects>> ALL_OF = register("all_of", AllOf.EntityEffects.MAP_CODEC);
	public static final Supplier<MapCodec<ApplyMobEffect>> APPLY_MOB_EFFECT = register("apply_mob_effect", ApplyMobEffect.MAP_CODEC);
	public static final Supplier<MapCodec<Ignite>> IGNITE = register("ignite", Ignite.MAP_CODEC);
	public static final Supplier<MapCodec<SpawnParticlesEffect>> SPAWN_PARTICLES = register("spawn_particles", SpawnParticlesEffect.MAP_CODEC);
	public static final Supplier<MapCodec<IterationEntity>> ITERATION_ENTITY = register("iteration_entity", IterationEntity.MAP_CODEC);
	public static final Supplier<MapCodec<ExplodeEffect>> EXPLODE = register("explode", ExplodeEffect.MAP_CODEC);
	public static final Supplier<MapCodec<PlaySoundEffect>> PLAY_SOUND = register("play_sound", PlaySoundEffect.MAP_CODEC);
	public static final Supplier<MapCodec<ProjectionEffect>> PROJECTION = register("projection", ProjectionEffect.MAP_CODEC);
	public static final Supplier<MapCodec<SummonEntityEffect>> SUMMON_ENTITY = register("summon_entity", SummonEntityEffect.MAP_CODEC);
	public static final Supplier<MapCodec<MovementEffect>> MOVEMENT = register("movement", MovementEffect.MAP_CODEC);

	private AffixEntityEffects() {
	}

	public static void register(IEventBus modEventBus) {
		DEFERRED_REGISTER.register(modEventBus);
	}

	private static <T extends AffixEntityEffect> Supplier<MapCodec<T>> register(String name, MapCodec<T> mapCodec) {
		return DEFERRED_REGISTER.register(name, () -> mapCodec);
	}

	public record DamageEntityEffect(LevelBasedValue minDamage, LevelBasedValue maxDamage, Holder<DamageType> damageType) implements AffixEntityEffect {
		public static final MapCodec<DamageEntityEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				LevelBasedValue.CODEC.fieldOf("min_damage").forGetter(DamageEntityEffect::minDamage),
				LevelBasedValue.CODEC.fieldOf("max_damage").forGetter(DamageEntityEffect::maxDamage),
				DamageType.CODEC.fieldOf("damage_type").forGetter(DamageEntityEffect::damageType)
		).apply(instance, DamageEntityEffect::new));

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			float damage = Mth.randomBetween(level.getRandom(), this.minDamage.calculate(affixLevel), this.maxDamage.calculate(affixLevel));
			target.hurtServer(level, new DamageSource(damageType, source), damage);
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return MAP_CODEC;
		}

	}

	public record ApplyMobEffect(
			HolderSet<MobEffect> toApply,
			LevelBasedValue minDuration,
			LevelBasedValue maxDuration,
			LevelBasedValue minAmplifier,
			LevelBasedValue maxAmplifier
	) implements AffixEntityEffect {
		public static final Codec<ApplyMobEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.MOB_EFFECT).fieldOf("to_apply").forGetter(ApplyMobEffect::toApply),
				LevelBasedValue.CODEC.fieldOf("min_duration").forGetter(ApplyMobEffect::minDuration),
				LevelBasedValue.CODEC.fieldOf("max_amplifier").forGetter(ApplyMobEffect::maxDuration),
				LevelBasedValue.CODEC.fieldOf("min_amplifier").forGetter(ApplyMobEffect::minAmplifier),
				LevelBasedValue.CODEC.fieldOf("max_amplifier").forGetter(ApplyMobEffect::maxAmplifier)
		).apply(instance, ApplyMobEffect::new));

		public static final MapCodec<ApplyMobEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.MOB_EFFECT).fieldOf("to_apply").forGetter(ApplyMobEffect::toApply),
				LevelBasedValue.CODEC.fieldOf("min_duration").forGetter(ApplyMobEffect::minDuration),
				LevelBasedValue.CODEC.fieldOf("max_amplifier").forGetter(ApplyMobEffect::maxDuration),
				LevelBasedValue.CODEC.fieldOf("min_amplifier").forGetter(ApplyMobEffect::minAmplifier),
				LevelBasedValue.CODEC.fieldOf("max_amplifier").forGetter(ApplyMobEffect::maxAmplifier)
		).apply(instance, ApplyMobEffect::new));

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			if (target instanceof LivingEntity living) {
				RandomSource random = living.getRandom();
				Optional<Holder<MobEffect>> selected = this.toApply.getRandomElement(random);
				if (selected.isPresent()) {
					int ticks = Math.round(
							Mth.randomBetween(random, this.minDuration.calculate(affixLevel), this.maxDuration.calculate(affixLevel)) * 20.0F
					);
					int amplifier = Math.max(
							0, Math.round(Mth.randomBetween(random, this.minAmplifier.calculate(affixLevel), this.maxAmplifier.calculate(affixLevel)))
					);
					living.addEffect(new MobEffectInstance(selected.get(), ticks, amplifier));
				}
			}
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return MAP_CODEC;
		}

	}

	public record Ignite(LevelBasedValue duration) implements AffixEntityEffect {
		public static final MapCodec<Ignite> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				LevelBasedValue.CODEC.fieldOf("duration").forGetter(Ignite::duration)
		).apply(instance, Ignite::new));

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			target.igniteForSeconds(this.duration.calculate(affixLevel));
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return MAP_CODEC;
		}

	}

	public record SpawnParticlesEffect(
			ParticleOptions particle,
			int count,
			PositionSource horizontalPosition,
			PositionSource verticalPosition,
			VelocitySource horizontalVelocity,
			VelocitySource verticalVelocity,
			FloatProvider speed
	) implements AffixEntityEffect {
		public static final MapCodec<SpawnParticlesEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ParticleTypes.CODEC.fieldOf("particle").forGetter(SpawnParticlesEffect::particle),
				Codec.INT.optionalFieldOf("count", 0).forGetter(SpawnParticlesEffect::count),
				PositionSource.MAP_CODEC.fieldOf("horizontal_position").forGetter(SpawnParticlesEffect::horizontalPosition),
				PositionSource.MAP_CODEC.fieldOf("vertical_position").forGetter(SpawnParticlesEffect::verticalPosition),
				VelocitySource.MAP_CODEC.fieldOf("horizontal_velocity").forGetter(SpawnParticlesEffect::horizontalVelocity),
				VelocitySource.MAP_CODEC.fieldOf("vertical_velocity").forGetter(SpawnParticlesEffect::verticalVelocity),
				FloatProviders.CODEC.optionalFieldOf("speed", ConstantFloat.ZERO).forGetter(SpawnParticlesEffect::speed)
		).apply(instance, SpawnParticlesEffect::new));

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			RandomSource random = target.getRandom();
			Vec3 movement = target.getKnownMovement();
			float bbWidth = target.getBbWidth();
			float bbHeight = target.getBbHeight();
			level.sendParticles(
					this.particle,
					this.horizontalPosition.getCoordinate(position.x(), position.x(), bbWidth, random),
					this.verticalPosition.getCoordinate(position.y(), position.y() + bbHeight / 2.0F, bbHeight, random),
					this.horizontalPosition.getCoordinate(position.z(), position.z(), bbWidth, random),
					this.count,
					this.horizontalVelocity.getVelocity(movement.x(), random),
					this.verticalVelocity.getVelocity(movement.y(), random),
					this.horizontalVelocity.getVelocity(movement.z(), random),
					this.speed.sample(random)
			);
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return MAP_CODEC;
		}

		public enum PositionSourceType implements StringRepresentable {
			ENTITY_POSITION("entity_position", (pos, center, bbSpan, random) -> pos),
			BOUNDING_BOX("in_bounding_box", (pos, center, bbSpan, random) -> center + (random.nextDouble() - 0.5) * bbSpan);
			public static final Codec<PositionSourceType> CODEC = StringRepresentable.fromEnum(PositionSourceType::values);

			private final String name;
			private final CoordinateSource source;

			PositionSourceType(String name, CoordinateSource source) {
				this.name = name;
				this.source = source;
			}

			public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) {
				return this.source.getCoordinate(position, center, boundingBoxSpan, random);
			}

			@Override
			public String getSerializedName() {
				return this.name;
			}

			@FunctionalInterface
			private interface CoordinateSource {
				double getCoordinate(double pos, double center, float boundingBoxSpan, RandomSource random);
			}
		}

		public record PositionSource(PositionSourceType type, float offset, float scale) {
			public static final MapCodec<PositionSource> MAP_CODEC = RecordCodecBuilder.<PositionSource>mapCodec(instance -> instance.group(
					PositionSourceType.CODEC.fieldOf("type").forGetter(PositionSource::type),
					Codec.FLOAT.optionalFieldOf("offset", 0.0f).forGetter(PositionSource::offset),
					Codec.FLOAT.optionalFieldOf("scale", 1.0f).forGetter(PositionSource::scale)
			).apply(instance, PositionSource::new)).validate(source -> {
				if (source.type == PositionSourceType.ENTITY_POSITION && source.scale() != 1.0f) {
					return DataResult.error(() -> "Cannot scale an entity position coordinate source");
				}

				return DataResult.success(source);
			});

			public static PositionSource inBoundingBox() {
				return inBoundingBox(0.0f);
			}

			public static PositionSource inBoundingBox(float offset) {
				return inBoundingBox(offset, 1.0f);
			}

			public static PositionSource inBoundingBox(float offset, float scale) {
				return new PositionSource(SpawnParticlesEffect.PositionSourceType.BOUNDING_BOX, offset, scale);
			}

			public static PositionSource entityPosition() {
				return entityPosition(0.0f);
			}

			public static PositionSource entityPosition(float offset) {
				return new PositionSource(SpawnParticlesEffect.PositionSourceType.BOUNDING_BOX, offset, 1.0f);
			}

			public double getCoordinate(double position, double center, float boundingBoxSpan, RandomSource random) {
				return this.type.getCoordinate(position, center, boundingBoxSpan * this.scale, random) + this.offset;
			}
		}

		public record VelocitySource(float movementScale, FloatProvider base) {
			public static final VelocitySource INSTANCE = new VelocitySource(0.0f, ConstantFloat.ZERO);
			public static final MapCodec<VelocitySource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					Codec.FLOAT.optionalFieldOf("movement_scale", 0.0f).forGetter(VelocitySource::movementScale),
					FloatProviders.CODEC.optionalFieldOf("base", ConstantFloat.ZERO).forGetter(VelocitySource::base)
			).apply(instance, VelocitySource::new));

			public static VelocitySource create(float movementScale) {
				return create(movementScale, ConstantFloat.ZERO);
			}

			public static VelocitySource create(float movementScale, FloatProvider base) {
				return new VelocitySource(movementScale, base);
			}

			public double getVelocity(double movement, RandomSource random) {
				return movement * this.movementScale + this.base.sample(random);
			}
		}
	}

	/**
	 * 迭代实体效果组件
	 * 用于迭代过滤维度内全部实体并执行效果
	 *
	 * @param horizontalScale
	 * @param verticalScale
	 * @param predicate
	 * @param effect
	 */
	public record IterationEntity(double horizontalScale, double verticalScale, Optional<EntityPredicate> predicate, AffixEntityEffect effect) implements AffixEntityEffect {
		public static final MapCodec<IterationEntity> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.doubleRange(0.0, 1024.0).fieldOf("horizontal_scale").forGetter(IterationEntity::horizontalScale),
				Codec.doubleRange(0.0, 1024.0).fieldOf("vertical_scale").forGetter(IterationEntity::verticalScale),
				EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(IterationEntity::predicate),
				AffixEntityEffect.CODEC.fieldOf("effect").forGetter(IterationEntity::effect)
		).apply(instance, IterationEntity::new));

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			AABB aabb = target.getBoundingBox().inflate(this.horizontalScale, this.verticalScale, this.horizontalScale);
			for (Entity target1 : level.getEntities(target, aabb)) {
				if (this.predicate.isEmpty() || this.predicate.get().matches(level, position, target1)) {
					this.effect.apply(level, affixLevel, source, target1, source.position());
				}
			}
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return MAP_CODEC;
		}

	}

	public record ExplodeEffect(
			boolean attributeToUser,
			Optional<Holder<DamageType>> damageType,
			Optional<LevelBasedValue> knockbackMultiplier,
			Optional<HolderSet<Block>> immuneBlocks,
			Vec3 offset,
			LevelBasedValue radius,
			boolean createFire,
			Level.ExplosionInteraction blockInteraction,
			ParticleOptions smallParticle,
			ParticleOptions largeParticle,
			WeightedList<ExplosionParticleInfo> blockParticles,
			Holder<SoundEvent> sound
	) implements AffixEntityEffect {
		public static final MapCodec<ExplodeEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.BOOL.optionalFieldOf("attribute_to_user", false).forGetter(ExplodeEffect::attributeToUser),
				DamageType.CODEC.optionalFieldOf("damage_type").forGetter(ExplodeEffect::damageType),
				LevelBasedValue.CODEC.optionalFieldOf("knockback_multiplier").forGetter(ExplodeEffect::knockbackMultiplier),
				RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.BLOCK).optionalFieldOf("immune_blocks").forGetter(ExplodeEffect::immuneBlocks),
				Vec3.CODEC.optionalFieldOf("offset", Vec3.ZERO).forGetter(ExplodeEffect::offset),
				LevelBasedValue.CODEC.fieldOf("radius").forGetter(ExplodeEffect::radius),
				Codec.BOOL.optionalFieldOf("create_fire", false).forGetter(ExplodeEffect::createFire),
				Level.ExplosionInteraction.CODEC.fieldOf("block_interaction").forGetter(ExplodeEffect::blockInteraction),
				ParticleTypes.CODEC.fieldOf("small_particle").forGetter(ExplodeEffect::smallParticle),
				ParticleTypes.CODEC.fieldOf("large_particle").forGetter(ExplodeEffect::largeParticle),
				WeightedList.codec(ExplosionParticleInfo.CODEC).optionalFieldOf("block_particles", WeightedList.of()).forGetter(ExplodeEffect::blockParticles),
				SoundEvent.CODEC.fieldOf("sound").forGetter(ExplodeEffect::sound)
		).apply(instance, ExplodeEffect::new));

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			Vec3 pos = position.add(this.offset);
			level.explode(
					this.attributeToUser ? target : null,
					this.getDamageSource(target, pos),
					new SimpleExplosionDamageCalculator(
							this.blockInteraction != Level.ExplosionInteraction.NONE,
							this.damageType.isPresent(),
							this.knockbackMultiplier.map(value -> value.calculate(affixLevel)),
							this.immuneBlocks
					),
					pos.x(),
					pos.y(),
					pos.z(),
					Math.max(this.radius.calculate(affixLevel), 0.0F),
					this.createFire,
					this.blockInteraction,
					this.smallParticle,
					this.largeParticle,
					this.blockParticles,
					this.sound
			);
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return null;
		}

		private @Nullable DamageSource getDamageSource(Entity entity, Vec3 position) {
			return this.damageType.map(damageTypeHolder -> this.attributeToUser ? new DamageSource(damageTypeHolder, entity) : new DamageSource(damageTypeHolder, position)).orElse(null);
		}
	}

	public record PlaySoundEffect(List<Holder<SoundEvent>> soundEvents, FloatProvider volume, FloatProvider pitch) implements AffixEntityEffect {
		public static final MapCodec<PlaySoundEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(
				i -> i.group(
								ExtraCodecs.compactListCodec(SoundEvent.CODEC, SoundEvent.CODEC.sizeLimitedListOf(255))
										.fieldOf("sound")
										.forGetter(PlaySoundEffect::soundEvents),
								FloatProviders.codec(1.0E-5F, 10.0F).fieldOf("volume").forGetter(PlaySoundEffect::volume),
								FloatProviders.codec(1.0E-5F, 2.0F).fieldOf("pitch").forGetter(PlaySoundEffect::pitch)
						)
						.apply(i, PlaySoundEffect::new)
		);

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			if (!target.isSilent()) {
				RandomSource random = target.getRandom();
				int index = Mth.clamp(affixLevel - 1, 0, this.soundEvents.size() - 1);
				level.playSound(
						null,
						position.x(),
						position.y(),
						position.z(),
						this.soundEvents.get(index),
						target.getSoundSource(),
						this.volume.sample(random),
						this.pitch.sample(random)
				);
			}
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return MAP_CODEC;
		}
	}

	public record ProjectionEffect(
			ProjectileTemplate projectile,
			ItemStackTemplate projectileItem,
			LevelBasedValue power,
			LevelBasedValue uncertainty,
			Holder<SoundEvent> sound
	) implements AffixEntityEffect {
		public static final MapCodec<ProjectionEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ProjectileTemplate.CODEC.fieldOf("projectile").forGetter(ProjectionEffect::projectile),
				ItemStackTemplate.CODEC.fieldOf("projectile_item").forGetter(ProjectionEffect::projectileItem),
				LevelBasedValue.CODEC.fieldOf("power").forGetter(ProjectionEffect::power),
				LevelBasedValue.CODEC.fieldOf("uncertainty").forGetter(ProjectionEffect::uncertainty),
				SoundEvent.CODEC.fieldOf("sound").forGetter(ProjectionEffect::sound)
		).apply(instance, ProjectionEffect::new));

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			ItemStack itemStack = this.projectileItem.create();
			Projectile projectile = this.projectile.provide(level, source, itemStack);
			double x = source.getX();
			double y = source.getEyeY() - 0.1f;
			double z = source.getZ();
			projectile.setOwner(source);
			projectile.setPos(x, y, z);

			double xd = target.getX() - source.getX();
			double yd = target.getY(0.3333333333333333) - projectile.getY();
			double zd = target.getZ() - source.getZ();
			double distanceToTarget = Math.sqrt(xd * xd + zd * zd);

			Projectile.spawnProjectileUsingShoot(
					projectile,
					level,
					itemStack,
					xd,
					yd + distanceToTarget * 0.2f,
					zd,
					this.power.calculate(affixLevel),
					this.uncertainty.calculate(affixLevel)
			);

			source.playSound(this.sound.value(), 1.0F, 1.0F / (source.getRandom().nextFloat() * 0.4F + 0.8F));
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return MAP_CODEC;
		}

	}

	public record SummonEntityEffect(HolderSet<EntityType<?>> entityTypes) implements AffixEntityEffect {
		public static final MapCodec<SummonEntityEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.ENTITY_TYPE).fieldOf("entity").forGetter(SummonEntityEffect::entityTypes)
		).apply(instance, SummonEntityEffect::new));

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			BlockPos blockPos = BlockPos.containing(position);
			if (Level.isInSpawnableBounds(blockPos)) {
				Optional<Holder<EntityType<?>>> entityType = this.entityTypes().getRandomElement(level.getRandom());
				if (entityType.isPresent()) {
					Entity spawned = entityType.get().value().spawn(level, blockPos, EntitySpawnReason.TRIGGERED);
					if (spawned != null) {
						if (spawned instanceof LightningBolt lightningBolt && source instanceof ServerPlayer player) {
							lightningBolt.setCause(player);
						}

						spawned.snapTo(position.x, position.y, position.z, spawned.getYRot(), spawned.getXRot());
					}
				}
			}
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return MAP_CODEC;
		}
	}

	public record MovementEffect(double speed) implements AffixEntityEffect {
		public static final MapCodec<MovementEffect> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Codec.DOUBLE.fieldOf("speed").forGetter(MovementEffect::speed)
		).apply(instance, MovementEffect::new));

		@Override
		public void apply(ServerLevel level, int affixLevel, Entity source, Entity target, Vec3 position) {
			Vec3 vec3 = new Vec3(source.getX(), source.getY(), source.getZ()).subtract(target.position()).normalize().scale(this.speed);
			target.setDeltaMovement(vec3);
		}

		@Override
		public MapCodec<? extends AffixEntityEffect> codec() {
			return MAP_CODEC;
		}
	}
}
