package top.theillusivec4.champions.deprecated.common.capability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingConversionEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.champions.deprecated.api.IAffix;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.deprecated.common.network.SPacketSyncChampion;
import top.theillusivec4.champions.deprecated.common.rank.Rank;
import top.theillusivec4.champions.deprecated.common.rank.RankManager;
import top.theillusivec4.champions.deprecated.common.util.ChampionBuilder;
import top.theillusivec4.champions.deprecated.common.util.ChampionHelper;

import java.util.stream.Collectors;


public class AttachmentEventHandler {

  @SubscribeEvent
  public void onSpecialSpawn(MobSpawnEvent.PositionCheck evt) {
    LivingEntity entity = evt.getEntity();

    if (!entity.level().isClientSide()) {
      ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
        IChampion.Server serverChampion = champion.getServer();

        if (ChampionHelper.isValidChampion(serverChampion)) {

          if (!ChampionsConfig.championSpawners && evt.getSpawner() != null) {
            serverChampion.setRank(RankManager.getLowestRank());
          } else {
            ChampionBuilder.spawn(champion);
          }
        }
      });
    }
  }

  @SubscribeEvent
  public void onLivingConvert(LivingConversionEvent.Post evt) {
    LivingEntity entity = evt.getEntity();

    if (!entity.level().isClientSide()) {
      LivingEntity outcome = evt.getOutcome();
      ChampionAttachment.getAttachment(entity).ifPresent(
        oldChampion -> {
          if (ChampionHelper.isValidChampion(oldChampion.getServer())) {
            ChampionAttachment.getAttachment(outcome)
              .ifPresent(newChampion -> {
                ChampionBuilder.copy(oldChampion, newChampion);
                IChampion.Server serverChampion = newChampion.getServer();
                PacketDistributor.sendToPlayersTrackingEntity(outcome,
                  new SPacketSyncChampion(outcome.getId(),
                    serverChampion.getRank().map(Rank::getTier).orElse(0),
                    serverChampion.getRank().map(Rank::getDefaultColor).orElse(0),
                    serverChampion.getAffixes().stream().map(IAffix::getIdentifier).collect(Collectors.toSet())));
              });
          }
        });
    }
  }

  @SubscribeEvent
  public void startTracking(PlayerEvent.StartTracking evt) {
    Entity entity = evt.getTarget();
    Player playerEntity = evt.getEntity();

    if (playerEntity instanceof ServerPlayer serverPlayer) {
      ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
        IChampion.Server serverChampion = champion.getServer();
        if (ChampionHelper.isValidChampion(serverChampion)) {
          PacketDistributor.sendToPlayer(serverPlayer,
            new SPacketSyncChampion(entity.getId(),
              serverChampion.getRank().map(Rank::getTier).orElse(0),
              serverChampion.getRank().map(Rank::getDefaultColor).orElse(0),
              serverChampion.getAffixes().stream().map(IAffix::getIdentifier).collect(Collectors.toSet()))
          );
        }
      });
    }
  }
}
