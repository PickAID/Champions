package top.theillusivec4.champions.world.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.apache.commons.lang3.mutable.MutableFloat;
import top.theillusivec4.champions.champion.ChampionHelper;
import top.theillusivec4.champions.stats.Stats;
import top.theillusivec4.champions.world.effect.MobEffects;

public final class EntityEventListener {

	private EntityEventListener() {
	}

	public static void register() {
		NeoForge.EVENT_BUS.register(new EntityEventListener());
	}

	/**
	 * 处理治疗值
	 *
	 * @param event LivingHealEvent
	 */
	@SubscribeEvent
	public void onLivingHeal(LivingHealEvent event) {
		Entity entity = event.getEntity();
		MutableFloat heal = new MutableFloat(event.getAmount());
		if (entity.level() instanceof ServerLevel level) {
			if (entity instanceof LivingEntity livingEntity) {
				MobEffectInstance instance = livingEntity.getEffect(MobEffects.WOUND);
				if (instance != null) {
					heal.setValue(heal.floatValue() - heal.floatValue() * 0.1f * instance.getAmplifier());
				}
			}

//			ChampionUtil.getHandler(entity).ifPresent(handler -> {
//				float result = handler.modifyHeal(level, entity, heal.floatValue());
//				// 治疗不应该出现负数
//				event.setAmount(Math.max(result, 0.0f));
//				// BossBar
//				handler.getBossEvent().ifPresent(bossEvent -> bossEvent.setProgress(handler.getHealth() / handler.getMaxHealth()));
//			});

			float result = ChampionHelper.modifyHeal(level, entity, heal.floatValue());
			event.setAmount(Math.max(result, 0.0f));
//			if (entity instanceof LivingEntity livingEntity) {
//				Optional.ofNullable(ChampionHelper.getChampionEvent(livingEntity)).ifPresent(bossEvent -> bossEvent.setProgress(livingEntity.getHealth() / livingEntity.getMaxHealth()));
//			}
			ChampionHelper.updateChampionEvent(level, entity, ChampionHelper.ChampionEventOperation.PROGRESS);
		}

	}

	/**
	 * 处理动态的伤害免疫事件
	 *
	 * @param event LivingIncomingDamageEvent
	 */
	@SubscribeEvent
	public void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		Entity victim = event.getEntity();
		DamageSource source = event.getSource();

		if (victim instanceof LivingEntity livingEntity) {
			MobEffectInstance mobEffectInstance = livingEntity.getEffect(MobEffects.SHIELD);
			if (mobEffectInstance != null) {
				livingEntity.level().playSound(null, livingEntity.blockPosition(), SoundEvents.SHIELD_BLOCK.value(), SoundSource.HOSTILE, 1.0f, 1.0f);
				livingEntity.removeEffect(MobEffects.SHIELD);
				event.setCanceled(true);
				return;
			}
		}

//		ChampionUtil.getHandler(victim).ifPresent(handler -> {
//			boolean invulnerable = handler.isImmuneToDamage((ServerLevel) victim.level(), victim, source);
//			if (invulnerable) {
//				event.setCanceled(true);
//			}
//		});
		boolean immune = ChampionHelper.isImmuneToDamage((ServerLevel) victim.level(), victim, source);
		if (immune) {
			event.setCanceled(true);
		}
	}

	/**
	 * 处理伤害修改、伤害减免等
	 *
	 * @param event LivingDamageEvent.Pre
	 */
	@SubscribeEvent
	public void onLivingDamagePre(LivingDamageEvent.Pre event) {
		Entity victim = event.getEntity();
		MutableFloat damage = new MutableFloat(event.getOriginalDamage());
		DamageSource source = event.getSource();

		if (victim.level() instanceof ServerLevel level) {
      /*
      MobEffect
     */
			if (victim instanceof LivingEntity livingEntity) {
				MobEffectInstance mobEffectInstance = livingEntity.getEffect(MobEffects.WOUND);
				if (mobEffectInstance != null) {
					damage.setValue(damage.floatValue() + damage.floatValue() * 0.1f * mobEffectInstance.getAmplifier());
				}
			}

    /*
      伤害增幅
     */
			if (source.getEntity() != null) {
				Entity attacker = source.getEntity();
				damage.setValue(
//						ChampionUtil.getHandler(attacker).map(handler -> handler.modifyDamage((ServerLevel) attacker.level(), victim, source, damage.floatValue())).orElse(damage.floatValue())
						ChampionHelper.modifyDamage(level, victim, source, damage.floatValue())
				);
			}

    /*
      伤害减免
      伤害值 = 原值 - 原值*减免比例
      减免比例取值范围: [-1024.0f, 1.0]
     */

			if (damage.floatValue() > 0.0f) {
//				float protection = ChampionUtil.getHandler(victim).map(handler -> {
//					float originProtection = handler.getDamageProtection(level, victim, event.getSource());
//					return Math.clamp(originProtection, -1024.0f, Champions.getInstance().getCommonConfig().getMaxDamageProtection());
//				}).orElse(0.0f);
				float protection = ChampionHelper.getDamageProtection(level, victim, event.getSource());
				damage.setValue(damage.floatValue() * (1.0f - protection));
			}

    /*
    防止造成负伤害，预期外的行为
     */
			event.setNewDamage(Math.max(damage.floatValue(), 0.0f));
		}

	}

	/**
	 * 处理受伤后的相关逻辑
	 * 记录上次攻击的相关数据
	 *
	 * @param event LivingDamageEvent.Post
	 */
	@SubscribeEvent
	public void onLivingDamagePost(LivingDamageEvent.Post event) {
		ServerLevel level = (ServerLevel) event.getEntity().level();
		Entity entity = event.getEntity();
		DamageSource source = event.getSource();
		float origin = event.getOriginalDamage();
//		ChampionUtil.getHandler(entity).ifPresent(handler -> {
//			DamageSource damageSource = event.getSource();
//			Holder<DamageType> damageType = damageSource.typeHolder();
//			handler.updateLatestDamage(damageType, event.getOriginalDamage());
//
//			// BossBar
//			handler.getBossEvent().ifPresent(bossEvent -> bossEvent.setProgress(handler.getHealth() / handler.getMaxHealth()));
//		});
		ChampionHelper.updateLatestDamage(entity, source, origin);
		ChampionHelper.updateChampionEvent(level, entity, ChampionHelper.ChampionEventOperation.PROGRESS);

	}


	/**
	 * 处理 Tick 效果
	 *
	 * @param event EntityTickEvent.Pre
	 */
	@SubscribeEvent
	public void onEntityTickPre(EntityTickEvent.Pre event) {
		Entity entity = event.getEntity();
		if (entity.level() instanceof ServerLevel level) {
			ChampionHelper.tickEffects(level, entity);
			ChampionHelper.targetEffects(level, entity);
			ChampionHelper.updateChampionEvent(level, entity, ChampionHelper.ChampionEventOperation.PLAYERS);
//			ChampionUtil.getHandler(entity).ifPresent(handler -> {
//				handler.tickEffects(level, entity);
//				if (entity instanceof Mob mob && mob.getTarget() != null) {
//					LivingEntity target = mob.getTarget();
//					handler.targetEffects(level, entity, target);
//				}
//
//				// BossBar
//				handler.getBossEvent().ifPresent(bossEvent -> {
//					for (ServerPlayer player : level.players()) {
//						if (player.blockPosition().distSqr(entity.blockPosition()) <= BOSS_EVENT_DISTANCE_SQR) {
//							bossEvent.addPlayer(player);
//						} else {
//							bossEvent.removePlayer(player);
//						}
//					}
//				});
//
//			});

		} else {
			ChampionHelper.doParticlesEffects(entity);
//			ChampionUtil.getHandler(entity).ifPresent(handler -> {
//				if (handler.spawnParticles()) {
//					RandomSource randomSource = entity.getRandom();
//					Vec3 position = entity.position();
//					double x = position.x() + (randomSource.nextDouble() - 0.5) * entity.getBbWidth();
//					double y = position.y() + randomSource.nextDouble() * entity.getBbHeight();
//					double z = position.z() + (randomSource.nextDouble() - 0.5) * entity.getBbWidth();
//					int color = handler.getColor();
//					entity.level().addParticle(ParticleTypes.rank(color), x, y, z, 1.0f, 1.0f, 1.0f);
//				}
//			});
		}
	}

	@SubscribeEvent
	public void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
		Level level = event.getLevel();
		Entity entity = event.getEntity();
		if (level instanceof ServerLevel serverLevel) {
			ChampionHelper.updateChampionEvent(serverLevel, entity, ChampionHelper.ChampionEventOperation.REMOVE_ALL_PLAYERS);
		}
//		if (!level.isClientSide()) {
//			ChampionUtil.getHandler(entity).flatMap(ChampionHandlerEntity::getBossEvent).ifPresent(ServerChampionBossEvent::removeAllPlayers);
//		}
	}

	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		DamageSource source = event.getSource();
		Entity attacker = source.getEntity();
		LivingEntity victim = event.getEntity();
		Level level = victim.level();
		if (!ChampionHelper.getAffixContainer(victim).isEmpty() && attacker instanceof Player player) {
			player.awardStat(Stats.CHAMPION_MOBS_KILLED.get());
		}
//		boolean flag = ChampionUtil.getHandler(victim).map(ChampionHandler::isValid).orElse(false);
//		if (!level.isClientSide() && flag && attacker != null && attacker.getType() == EntityType.PLAYER) {
//			((Player) attacker).awardStat(Stats.CHAMPION_MOBS_KILLED.get());
//		}
	}

	/**
	 * 处理实体转换的词条复制
	 *
	 * @param event LivingConversionEvent
	 */
	@SubscribeEvent
	public void onLivingConversionPost(LivingConversionEvent.Post event) {
		ServerLevel level = (ServerLevel) event.getEntity().level();
		Entity from = event.getEntity();
		Entity to = event.getOutcome();
		ChampionHelper.updateFromEntity(level, to, from);
//		ChampionUtil.getHandler(to).ifPresent(handler -> ChampionUtil.getHandler(from).ifPresent(handler1 -> handler.load(handler1.save())));
	}

	/**
	 * 处理实体分裂的数据复制
	 *
	 * @param event MobSplitEvent
	 */
	@SubscribeEvent
	public void onMobSplit(MobSplitEvent event) {
		ServerLevel level = (ServerLevel) event.getParent().level();
		Entity parent = event.getParent();
		for (Mob child : event.getChildren()) {
			ChampionHelper.updateFromEntity(level, child, parent);
		}
//		ChampionUtil.getHandler(parent).ifPresent(handler -> {
//			for (Mob child : event.getChildren()) {
//				ChampionUtil.getHandler(child).ifPresent(handler1 -> handler1.load(handler.save()));
//			}
//		});
	}

	/*
	 * 注入到Mob初始化过程。
	 * 对于刷怪蛋，如果刷怪蛋不附带实体数据，会触发FinalizeSpawnEvent事件，我不知道这是否合乎预期。
	 */
	@SubscribeEvent
	public void onFinalizeSpawn(FinalizeSpawnEvent event) {
		if (event.getLevel() instanceof ServerLevel level) {
			Mob mob = event.getEntity();
			double x = event.getX();
			double y = event.getY();
			double z = event.getZ();
			DifficultyInstance instance = event.getDifficulty();
			EntitySpawnReason reason = event.getSpawnType();
			ChampionHelper.doFinalizeSpawn(level, mob, x, y, z, instance, reason);
//			ChampionUtil.getHandler(mob).ifPresent(handler -> handler.doFinalizeSpawn(level, event.getX(), event.getY(), event.getZ(), level.getCurrentDifficultyAt(mob.blockPosition()), event.getSpawnType()));
		}
	}

}
