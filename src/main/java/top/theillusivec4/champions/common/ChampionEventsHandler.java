package top.theillusivec4.champions.common;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FastColor;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.registry.ModParticleTypes;
import top.theillusivec4.champions.common.stat.ChampionsStats;
import top.theillusivec4.champions.common.util.ChampionBuilder;
import top.theillusivec4.champions.common.util.ChampionHelper;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class ChampionEventsHandler {

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(Champions.getDataLoader());
        event.addListener(Champions.API.getAttributesModifierDataLoader());
    }

    @SubscribeEvent
    public void onLivingXpDrop(LivingExperienceDropEvent evt) {
        LivingEntity livingEntity = evt.getEntity();
        ChampionCapability.getCapability(livingEntity)
                .ifPresent(champion -> champion.getServer().getRank().ifPresent(rank -> {
                    int growth = rank.getGrowthFactor();

                    if (growth > 0) {
                        evt.setDroppedExperience(
                                (growth * ChampionsConfig.experienceGrowth * evt.getOriginalExperience() +
                                        evt.getDroppedExperience()));
                    }
                }));
    }

    @SubscribeEvent
    public void onPlayerRightClick(PlayerInteractEvent.EntityInteract event) {
        if (ChampionsConfig.enableDebug) {
            var player = event.getEntity();
            var target = event.getTarget();
            if (!target.level.isClientSide() && ChampionHelper.isChampionEntity(target)) {
                ChampionCapability.getCapability(target).ifPresent(ChampionBuilder::resetAndUpdate);
                player.sendSystemMessage(Component.literal("[Debug] Removed %s rank, affixes and attribute modifiers".formatted(target.getName().getString())));
            }
        }
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Start evt) {
        Explosion explosion = evt.getExplosion();
        Entity entity = explosion.getExploder();

        if (entity != null && !entity.level.isClientSide()) {
            ChampionCapability.getCapability(entity)
                    .ifPresent(champion -> champion.getServer().getRank().ifPresent(rank -> {
                        int growth = rank.getGrowthFactor();

                        if (growth > 0) {
                            explosion.radius += ChampionsConfig.explosionGrowth * growth;
                        }
                    }));
        }
    }

    @SubscribeEvent
    public void onLivingJoinWorld(EntityJoinLevelEvent evt) {
        Entity entity = evt.getEntity();

        if (!entity.level.isClientSide()) {
            if (ChampionHelper.isValidChampionEntity(entity)) {
                ChampionCapability.getCapability(entity).ifPresent(champion -> {
                    IChampion.Server serverChampion = champion.getServer();
                    Optional<Rank> maybeRank = serverChampion.getRank();

                    if (maybeRank.isEmpty()) {
                        ChampionBuilder.spawn(champion);
                    }
                    serverChampion.getAffixes().forEach(affix -> affix.onSpawn(champion));
                    serverChampion.getRank().ifPresent(rank -> {
                        List<Tuple<Holder<MobEffect>, Integer>> effects = rank.getEffects();
                        effects.forEach(effectPair -> champion.getLivingEntity()
                                .addEffect(new MobEffectInstance(effectPair.getA().get(), 200, effectPair.getB())));
                    });
                });
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingTickEvent evt) {
        LivingEntity livingEntity = evt.getEntity();

        if (livingEntity.level.isClientSide()) {
            ChampionCapability.getCapability(livingEntity).ifPresent(champion -> {
                IChampion.Client clientChampion = champion.getClient();
                if (ChampionHelper.isValidChampion(clientChampion)) {
                    clientChampion.getAffixes().forEach(affix -> affix.onClientUpdate(champion));
                    clientChampion.getRank().ifPresent(rank -> {
                        if (ChampionsConfig.showParticles && rank.getA() > 0) {
                            String colorCode = rank.getB();
                            int color = Rank.getColor(colorCode);
                            float r = (float) FastColor.ARGB32.red(color) / 255;
                            float g = (float) FastColor.ARGB32.green(color) / 255;
                            float b = (float) FastColor.ARGB32.blue(color) / 255;

                            livingEntity.level.addParticle(ModParticleTypes.RANK_PARTICLE_TYPE.get(),
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
            ChampionCapability.getCapability(livingEntity).ifPresent(champion -> {
                IChampion.Server serverChampion = champion.getServer();
                if (ChampionHelper.isValidChampion(serverChampion)) {
                    serverChampion.getAffixes().forEach(affix -> affix.onServerUpdate(champion));
                    serverChampion.getRank().ifPresent(rank -> {
                        if (livingEntity.tickCount % 4 == 0) {
                            List<Tuple<Holder<MobEffect>, Integer>> effects = rank.getEffects();
                            effects.forEach(effectPair -> livingEntity.addEffect(
                                    new MobEffectInstance(effectPair.getA().get(), 100, effectPair.getB())));
                        }
                    });
                }
            });
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent evt) {
        LivingEntity livingEntity = evt.getEntity();

        if (livingEntity.level.isClientSide()) {
            return;
        }
        ChampionCapability.getCapability(livingEntity).ifPresent(champion -> {
            IChampion.Server serverChampion = champion.getServer();
            serverChampion.getAffixes().forEach(affix -> {

                if (!affix.onAttacked(champion, evt.getSource(), evt.getAmount())) {
                    evt.setCanceled(true);
                }
            });
        });

        if (evt.isCanceled()) {
            return;
        }
        Entity source = evt.getSource().getDirectEntity();
        ChampionCapability.getCapability(source).ifPresent(champion -> {
            IChampion.Server serverChampion = champion.getServer();
            serverChampion.getAffixes().forEach(affix -> {

                if (!affix.onAttack(champion, evt.getEntity(), evt.getSource(), evt.getAmount())) {
                    evt.setCanceled(true);
                }
            });
        });
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent evt) {
        LivingEntity livingEntity = evt.getEntity();

        if (!livingEntity.level.isClientSide()) {
            float[] amounts = new float[]{evt.getAmount(), evt.getAmount()};
            ChampionCapability.getCapability(livingEntity).ifPresent(champion -> {
                IChampion.Server serverChampion = champion.getServer();
                serverChampion.getAffixes().forEach(
                        affix -> amounts[1] = affix.onHurt(champion, evt.getSource(), amounts[0], amounts[1]));
            });
            evt.setAmount(amounts[1]);
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent evt) {
        LivingEntity livingEntity = evt.getEntity();

        if (!livingEntity.level.isClientSide()) {
            float[] amounts = new float[]{evt.getAmount(), evt.getAmount()};
            ChampionCapability.getCapability(livingEntity).ifPresent(champion -> {
                IChampion.Server serverChampion = champion.getServer();
                serverChampion.getAffixes().forEach(affix -> amounts[1] = affix
                        .onDamage(champion, evt.getSource(), amounts[0], amounts[1]));
            });
            evt.setAmount(amounts[1]);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent evt) {
        LivingEntity livingEntity = evt.getEntity();

        if (livingEntity.level.isClientSide()) {
            return;
        }
        ChampionCapability.getCapability(livingEntity).ifPresent(champion -> {
            IChampion.Server serverChampion = champion.getServer();
            serverChampion.getAffixes().forEach(affix -> {

                if (!affix.onDeath(champion, evt.getSource())) {
                    evt.setCanceled(true);
                }
            });
            serverChampion.getRank().ifPresent(rank -> {
                if (!evt.isCanceled()) {
                    Entity source = evt.getSource().getEntity();

                    if (source instanceof ServerPlayer player && !(source instanceof FakePlayer)) {
                        player.awardStat(ChampionsStats.CHAMPION_MOBS_KILLED);
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
        });
    }

    @SubscribeEvent
    public void onServerStart(ServerAboutToStartEvent evt) {
        ChampionHelper.setServer(evt.getServer());
    }

    @SubscribeEvent
    public void onServerClose(ServerStoppedEvent evt) {
        ChampionHelper.setServer(null);
        ChampionHelper.clearBeacons();
    }

    @SubscribeEvent
    public void onBeaconStart(AttachCapabilitiesEvent<BlockEntity> evt) {
        BlockEntity blockEntity = evt.getObject();

        if (blockEntity instanceof BeaconBlockEntity beaconBlockEntity) {
            ChampionHelper.addBeacon(blockEntity.getBlockPos());
        }
    }

    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent evt) {
        LivingEntity livingEntity = evt.getEntity();

        if (!livingEntity.level.isClientSide()) {
            float[] amounts = new float[]{evt.getAmount(), evt.getAmount()};
            ChampionCapability.getCapability(livingEntity).ifPresent(champion -> {
                IChampion.Server serverChampion = champion.getServer();
                serverChampion.getAffixes()
                        .forEach(affix -> amounts[1] = affix.onHeal(champion, amounts[0], amounts[1]));
            });
            evt.setAmount(amounts[1]);
        }
    }
}
