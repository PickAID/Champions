package top.theillusivec4.champions.common.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ServerExplosion;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.client.ChampionsOverlay;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.event.customEvent.ChampionsEventHooks;
import top.theillusivec4.champions.common.network.SyncAffixSettingPacket;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.rank.RankManager;
import top.theillusivec4.champions.common.registry.AffixTypes;
import top.theillusivec4.champions.common.registry.ModParticleTypes;
import top.theillusivec4.champions.common.registry.ModStats;
import top.theillusivec4.champions.common.util.ChampionBuilder;
import top.theillusivec4.champions.common.util.ChampionHelper;
import top.theillusivec4.champions.common.util.Utils;
import top.theillusivec4.champions.server.command.ChampionsCommand;

import java.util.ArrayList;
import java.util.Optional;

public class ChampionEventsHandler {

  @SubscribeEvent
  private void onAddReloadListener(AddServerReloadListenersEvent event) {
    event.addListener(Utils.getLocation("affix_data"), Champions.API.getAffixDataLoader());
    event.addListener(Utils.getLocation("attributes_modifier_data"), Champions.API.getAttributesModifierDataLoader());
  }

  @SubscribeEvent
  private void onLivingXpDrop(LivingExperienceDropEvent evt) {
    LivingEntity livingEntity = evt.getEntity();
    ChampionAttachment.getAttachment(livingEntity).flatMap(champion -> champion.getServer().getRank()).ifPresent(rank -> {
      int growth = rank.getGrowthFactor();

      if (growth > 0) {
        evt.setDroppedExperience(
          (growth * ChampionsConfig.experienceGrowth * evt.getOriginalExperience() +
            evt.getDroppedExperience()));
      }
    });
  }

  @SubscribeEvent
  private void onExplosion(ExplosionEvent.Start evt) {
    ServerExplosion explosion = evt.getExplosion();
    Entity entity = explosion.getDirectSourceEntity();

    if (entity != null && !entity.level().isClientSide()) {
      ChampionAttachment.getAttachment(entity).flatMap(champion -> champion.getServer().getRank()).ifPresent(rank -> {
        int growth = rank.getGrowthFactor();

        if (growth > 0) {
          explosion.radius += ChampionsConfig.explosionGrowth * growth;
        }
      });
    }
  }

  @SubscribeEvent
  private void onMobSpilt(MobSplitEvent event) {
    if (ChampionsConfig.mobInherit) {
      var parentMob = event.getParent();
      var children = event.getChildren();
      ChampionAttachment.getAttachment(parentMob).ifPresent(champion -> {
        var serverChampion = champion.getServer();
        if (ChampionHelper.isValidChampion(serverChampion)) {
          serverChampion.getRank().ifPresent(rank -> children.forEach(child -> ChampionAttachment.getAttachment(child).ifPresent(championChild -> {
              ArrayList<IAffix> parentAffixes = new ArrayList<>(serverChampion.getAffixes());
              if (!ChampionsConfig.canHaveInfestedAffix) {
                parentAffixes.remove(AffixTypes.INFESTED.get());
              }
              championChild.getServer().setRank(RankManager.getRank(rank.getTier() - ChampionsConfig.rankReduce));
              if (!parentAffixes.isEmpty()) {
                championChild.getServer().setAffixes(parentAffixes);
              }
              ChampionBuilder.applyGrowth(championChild, championChild.getServer().getRank().orElse(RankManager.getEmptyRank()).getGrowthFactor());
            }))
          );
        }
      });
    }
  }

  @SubscribeEvent
  private void onLivingJoinWorld(EntityJoinLevelEvent evt) {
    Entity entity = evt.getEntity();

    if (!entity.level().isClientSide()) {
      if (ChampionHelper.isValidChampionEntity(entity)) {
        ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
          IChampion.Server serverChampion = champion.getServer();
          Optional<Rank> maybeRank = serverChampion.getRank();

          if (maybeRank.isEmpty()) {
            if (!ChampionsEventHooks.onAttemptChampionSpawn(champion)) {
              evt.setCanceled(true);
              return; // 事件被取消
            }
            ChampionBuilder.spawn(champion);
          }
          Utils.consumeIfLifeCycle(serverChampion.getAffixes(), lifecycle -> lifecycle.onSpawn(champion));
          serverChampion.getRank().ifPresent(rank -> {
            var effects = rank.getEffects();
            effects.forEach(effectPair -> champion.getLivingEntity()
              .addEffect(new MobEffectInstance(effectPair.getA(), 200, effectPair.getB())));
          });
        });
      }
    }
  }

  @SubscribeEvent
  private void onLivingUpdate(EntityTickEvent.Pre evt) {
    if (evt.getEntity() instanceof LivingEntity livingEntity) {
      if (livingEntity.level().isClientSide()) {
        ChampionAttachment.getAttachment(livingEntity).ifPresent(champion -> {
          IChampion.Client clientChampion = champion.getClient();
          if (ChampionHelper.isValidChampion(clientChampion)) {
            Utils.consumeIfLifeCycle(clientChampion.getAffixes(), lifecycle -> lifecycle.onClientUpdate(champion));
            clientChampion.getRank().ifPresent(rank -> {
              if (ChampionsConfig.showParticles && rank.getA() >= 1) {
                String colorCode = rank.getB();
                int color = Rank.getColor(colorCode);
                float r = (float) ARGB.red(color) / 255;
                float g = (float) ARGB.green(color) / 255;
                float b = (float) ARGB.blue(color) / 255;

                livingEntity.level().addParticle(ModParticleTypes.RANK_PARTICLE_TYPE.get(),
                  livingEntity.position().x + (livingEntity.getRandom().nextDouble() - 0.5D) *
                    (double) livingEntity.getBbWidth(), livingEntity.position().y +
                    livingEntity.getRandom().nextDouble() * livingEntity.getBbHeight(),
                  livingEntity.position().z + (livingEntity.getRandom().nextDouble() - 0.5D) *
                    (double) livingEntity.getBbWidth(), r, g, b);
              }
            });
          }
        });
      } else if (livingEntity.tickCount % 10 == 0) {
        ChampionAttachment.getAttachment(livingEntity).ifPresent(champion -> {
          IChampion.Server serverChampion = champion.getServer();
          if (ChampionHelper.isValidChampion(serverChampion)) {
            Utils.consumeIfLifeCycle(serverChampion.getAffixes(), lifecycle -> lifecycle.onServerUpdate(champion));
            serverChampion.getRank().ifPresent(rank -> {
              if (livingEntity.tickCount % 4 == 0) {
                var effects = rank.getEffects();
                effects.forEach(effectPair -> livingEntity.addEffect(
                  new MobEffectInstance(effectPair.getA(), 100, effectPair.getB())));
              }
            });
          }
        });
      }
    }
  }

  @SubscribeEvent
  private void onPlayerRightClick(PlayerInteractEvent.EntityInteract event) {
    if (ChampionsConfig.enableDebug) {
      var player = event.getEntity();
      var target = event.getTarget();
      if (player instanceof ServerPlayer serverPlayer && ChampionHelper.isChampionEntity(target)) {
        ChampionAttachment.getAttachment(target).ifPresent(ChampionBuilder::resetAndUpdate);
        serverPlayer.sendSystemMessage(Component.literal("[Debug] Removed %s rank, affixes and attribute modifiers".formatted(target.getName().getString())));
      }
    }
  }

  @SubscribeEvent
  private void onLivingAttack(LivingIncomingDamageEvent evt) {
    LivingEntity livingEntity = evt.getEntity();

    if (!livingEntity.level().isClientSide()) {
      ChampionAttachment.getAttachment(livingEntity).ifPresent(champion -> {
        IChampion.Server serverChampion = champion.getServer();
        if (ChampionHelper.isValidChampion(serverChampion)) {

          Utils.consumeIfCombat(serverChampion.getAffixes(), combatAffix -> {
            if (!combatAffix.onAttacked(champion, evt.getSource(), evt.getAmount())) {
              evt.setCanceled(true);
            }
          });
        }
      });

      if (evt.isCanceled()) {
        return;
      }
      Entity source = evt.getSource().getDirectEntity();
      ChampionAttachment.getAttachment(source).ifPresent(champion -> {
        IChampion.Server serverChampion = champion.getServer();
        if (ChampionHelper.isValidChampion(serverChampion)) {
          Utils.consumeIfCombat(serverChampion.getAffixes(), combatAffix -> {
            if (!combatAffix.onAttack(champion, evt.getEntity(), evt.getSource(), evt.getAmount())) {
              evt.setCanceled(true);
            }
          });
        }
      });
    }
  }

  @SubscribeEvent
  private void onLivingDamage(LivingDamageEvent.Pre evt) {
    LivingEntity livingEntity = evt.getEntity();

    if (!livingEntity.level().isClientSide()) {
      float[] amounts = new float[]{evt.getOriginalDamage(), evt.getNewDamage()};
      ChampionAttachment.getAttachment(livingEntity).ifPresent(champion -> {
        IChampion.Server serverChampion = champion.getServer();
        if (ChampionHelper.isValidChampion(serverChampion)) {
          Utils.consumeIfCombat(serverChampion.getAffixes(), combatAffix ->
            amounts[1] = combatAffix.onDamage(champion, evt.getSource(), amounts[0], amounts[1]));
          Utils.consumeIfCombat(serverChampion.getAffixes(), combatAffix ->
            amounts[1] = combatAffix.onHurt(champion, evt.getSource(), amounts[0], amounts[1]));
          evt.setNewDamage(amounts[1]);
        }
      });
    }
  }

  @SubscribeEvent
  private void onLivingDeath(LivingDeathEvent evt) {
    LivingEntity livingEntity = evt.getEntity();

    if (livingEntity.level().isClientSide()) {
      return;
    }
    ChampionAttachment.getAttachment(livingEntity).ifPresent(champion -> {
      IChampion.Server serverChampion = champion.getServer();
      if (ChampionHelper.isValidChampion(serverChampion)) {
        Utils.consumeIfCombat(serverChampion.getAffixes(), combatAffix -> {
          if (!combatAffix.onDeath(champion, evt.getSource())) {
            evt.setCanceled(true);
          }
        });
        serverChampion.getRank().ifPresent(rank -> {
          if (!evt.isCanceled()) {
            Entity source = evt.getSource().getEntity();

            if (source instanceof ServerPlayer player && !(source instanceof FakePlayer)) {
              player.awardStat(ModStats.CHAMPION_MOBS_KILLED.get());
              int messageTier = ChampionsConfig.deathMessageTier;

              if (messageTier > 0 && rank.getTier() >= messageTier) {
                MinecraftServer server = livingEntity.getServer();

                if (server != null) {
                  server.getPlayerList().broadcastSystemMessage(
                    Component.translatable("rank.champions.title." + rank.getTier())
                      .append(" ")
                      .append(livingEntity.getCombatTracker().getDeathMessage())
                    , false);
                }
              }
            }
          }
        });
      }
    });
  }

  @SubscribeEvent
  private void onServerStart(ServerAboutToStartEvent evt) {
    ChampionHelper.setServer(evt.getServer());
  }

  @SubscribeEvent
  private void onServerClose(ServerStoppedEvent evt) {
    ChampionHelper.setServer(null);
    ChampionHelper.clearBeacons();
  }

  @SubscribeEvent
  private void onLivingHeal(LivingHealEvent evt) {
    LivingEntity livingEntity = evt.getEntity();

    if (!livingEntity.level().isClientSide()) {
      float[] amounts = new float[]{evt.getAmount(), evt.getAmount()};
      ChampionAttachment.getAttachment(livingEntity).ifPresent(champion -> {
        IChampion.Server serverChampion = champion.getServer();
        if (ChampionHelper.isValidChampion(serverChampion)) {
          Utils.consumeIfCombat(serverChampion.getAffixes(),
            combatAffix -> amounts[1] = combatAffix.onHeal(champion, amounts[0], amounts[1]));
        }
      });
      evt.setAmount(amounts[1]);
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  private void onBossBarEvent(final CustomizeGuiOverlayEvent.BossEventProgress event) {
    event.setCanceled(ChampionsOverlay.isRendering);
  }

  @SubscribeEvent
  private void onDatapackSync(OnDatapackSyncEvent event) {
    // send to single player login or reload for all relevant players.
    var relevantPlayers = event.getRelevantPlayers();
    var syncAffixSetting = new SyncAffixSettingPacket(Champions.API.getAffixDataLoader().getLoadedData());
    // apply setting on server, and sync affix settings to client
    SyncAffixSettingPacket.handelSettingMainThread();
    relevantPlayers.forEach(player -> PacketDistributor.sendToPlayer(player, syncAffixSetting));
  }

  @SubscribeEvent
  private void registerCommands(final RegisterCommandsEvent evt) {
    ChampionsCommand.register(evt.getDispatcher());
  }

}
