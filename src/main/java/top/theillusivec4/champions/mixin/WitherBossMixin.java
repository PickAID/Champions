//package top.theillusivec4.champions.mixin;
//
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerBossEvent;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.boss.wither.WitherBoss;
//import net.minecraft.world.entity.monster.Monster;
//import net.minecraft.world.entity.monster.RangedAttackMob;
//import net.minecraft.world.level.Level;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Redirect;
//import top.theillusivec4.champions.champion.ChampionHelper;
//
//@Mixin(value = WitherBoss.class)
//public abstract class WitherBossMixin extends Monster implements RangedAttackMob {
//	protected WitherBossMixin(EntityType<? extends Monster> type, Level level) {
//		super(type, level);
//	}
//
//	@Redirect(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setName(Lnet/minecraft/network/chat/Component;)V"))
////  @Inject(method = "readAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setName(Lnet/minecraft/network/chat/Component;)V"), cancellable = true)
//	private void champions$readAdditionalSaveData(ServerBossEvent instance, Component name) {
////		ChampionUtil.getHandler(this).ifPresent(handler -> {
////			Optional<ServerChampionBossEvent> optional = handler.getBossEvent();
////			if (handler.isValid() && optional.isPresent()) {
////				optional.get().setName(name);
////			} else {
////				instance.setName(name);
////			}
////		});
//		if (ChampionHelper.isBoss(this)) {
//			ChampionHelper.getOrCreateChampionEvent(this).setName(name);
//		} else {
//			instance.setName(name);
//		}
//	}
//
//	@Redirect(method = "setCustomName", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setName(Lnet/minecraft/network/chat/Component;)V"))
////  @Inject(method = "setCustomName", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setName(Lnet/minecraft/network/chat/Component;)V"), cancellable = true)
//	private void champions$setCustomName(ServerBossEvent instance, Component name) {
////		ChampionUtil.getHandler(this).ifPresent(handler -> {
////			Optional<ServerChampionBossEvent> optional = handler.getBossEvent();
////			if (handler.isValid() && optional.isPresent()) {
////				optional.get().setName(name);
////			} else {
////				instance.setName(name);
////			}
////		});
//		if (ChampionHelper.isBoss(this)) {
//			ChampionHelper.getOrCreateChampionEvent(this).setName(name);
//		} else {
//			instance.setName(name);
//		}
//	}
//
//	@Redirect(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V", ordinal = 0))
////  @Inject(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V", ordinal = 0))
//	private void champions$customServerAiStep$0(ServerBossEvent instance, float progress) {
////		ChampionUtil.getHandler(this).ifPresent(handler -> {
////			Optional<ServerChampionBossEvent> optional = handler.getBossEvent();
////			if (handler.isValid() && optional.isPresent()) {
////				optional.get().setProgress(progress);
////			} else {
////				instance.setProgress(progress);
////			}
////		});
//		if (ChampionHelper.isBoss(this)) {
//			ChampionHelper.getOrCreateChampionEvent(this).setProgress(progress);
//		} else {
//			instance.setProgress(progress);
//		}
//	}
//
//	//  @Inject(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V", ordinal = 1))
//	@Redirect(method = "customServerAiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V", ordinal = 1))
//	private void champions$customServerAiStep$1(ServerBossEvent instance, float progress) {
////		ChampionUtil.getHandler(this).ifPresent(handler -> {
////			Optional<ServerChampionBossEvent> optional = handler.getBossEvent();
////			if (handler.isValid() && optional.isPresent()) {
////				optional.get().setProgress(progress);
////			} else {
////				instance.setProgress(progress);
////			}
////		});
//		if (ChampionHelper.isBoss(this)) {
//			ChampionHelper.getOrCreateChampionEvent(this).setProgress(progress);
//		} else {
//			instance.setProgress(progress);
//		}
//	}
//
//	@Redirect(method = "makeInvulnerable", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V"))
//	private void champions$makeInvulnerable(ServerBossEvent instance, float progress) {
////		ChampionUtil.getHandler(this).ifPresent(handler -> {
////			Optional<ServerChampionBossEvent> optional = handler.getBossEvent();
////			if (handler.isValid() && optional.isPresent()) {
////				optional.get().setProgress(progress);
////			} else {
////				instance.setProgress(progress);
////			}
////		});
//		if (ChampionHelper.isBoss(this)) {
//			ChampionHelper.getOrCreateChampionEvent(this).setProgress(progress);
//		} else {
//			instance.setProgress(progress);
//		}
//	}
//
//	@Redirect(method = "startSeenByPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;addPlayer(Lnet/minecraft/server/level/ServerPlayer;)V"))
//	private void champions$startSeenByPlayer(ServerBossEvent instance, ServerPlayer player) {
////    ChampionUtil.getHandler(this).ifPresent(handler -> {
////      Optional<ServerChampionBossEvent> optional = handler.getBossEvent();
////      if (handler.isValid()) {
////        if (optional.isPresent()) {
////          optional.get().addPlayer(player);
////        } else {
////          handler.setBoss(true);
////          instance.removeAllPlayers();
////        }
////      } else {
////        if (optional.isPresent()) {
////          handler.setBoss(false);
////        }
////        instance.addPlayer(player);
////      }
////    });
//		if (ChampionHelper.isBoss(this)) {
//			ChampionHelper.getOrCreateChampionEvent(this).addPlayer(player);
//			instance.removePlayer(player);
//		} else {
//			instance.addPlayer(player);
//		}
//	}
//
//	@Redirect(method = "stopSeenByPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;removePlayer(Lnet/minecraft/server/level/ServerPlayer;)V"))
//	private void champions$stopSeenByPlayer(ServerBossEvent instance, ServerPlayer player) {
////		ChampionUtil.getHandler(this).ifPresent(handler -> {
////			Optional<ServerChampionBossEvent> optional = handler.getBossEvent();
////			if (handler.isValid() && optional.isPresent()) {
////				optional.get().removePlayer(player);
////			} else {
////				instance.removePlayer(player);
////			}
////		});
//		if (ChampionHelper.isBoss(this)) {
//			ChampionHelper.getOrCreateChampionEvent(this).removePlayer(player);
//		} else {
//			instance.removePlayer(player);
//		}
//	}
//}
