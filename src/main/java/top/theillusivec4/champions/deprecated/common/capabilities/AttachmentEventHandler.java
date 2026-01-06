package top.theillusivec4.champions.deprecated.common.capabilities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingConversionEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.champions.deprecated.api.affix.IAffix;
import top.theillusivec4.champions.deprecated.api.IChampion;
import top.theillusivec4.champions.deprecated.common.config.ChampionsConfig;
import top.theillusivec4.champions.deprecated.common.network.SPacketSyncChampion;
import top.theillusivec4.champions.deprecated.common.rank.Rank;
import top.theillusivec4.champions.deprecated.common.rank.RankManager;
import top.theillusivec4.champions.deprecated.common.util.ChampionBuilder;
import top.theillusivec4.champions.deprecated.common.util.ChampionHelper;

import java.util.stream.Collectors;


@Deprecated
public class AttachmentEventHandler {
  /**
   * 可能是生成处理的等级，但这里使用生物生成位置检查事件？
   * 此处对事件的使用不符合该事件设计的初衷
   * @param evt 生物生成位置合法性检查
   */
  @SubscribeEvent
  public void onSpecialSpawn(MobSpawnEvent.PositionCheck evt) {
    LivingEntity entity = evt.getEntity();

    if (!entity.level().isClientSide()) {
      ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
        IChampion.Server serverChampion = champion.getServer();

        if (serverChampion.getRank().isEmpty()) {

          if (ChampionsConfig.championSpawners && evt.getSpawnType() == EntitySpawnReason.SPAWNER) {
            serverChampion.setRank(RankManager.getLowestRank()); // check basic spawner
          } else if (!ChampionsConfig.championTrialSpawners && evt.getSpawnType() == EntitySpawnReason.TRIAL_SPAWNER) {
            serverChampion.setRank(RankManager.getLowestRank()); // check trial spawner
          }
        }
      });
    }
  }

  /**
   * 这里可能是处理生物转换时的词条数据复制
   * @param evt
   */
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
                SPacketSyncChampion.syncChampionDataToPlayerTrackingEntity(serverChampion, outcome);
              });
          }
        });
    }
  }

  /**
   * 处理刚刚开始被追踪时的数据同步
   * @param evt
   */
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
              serverChampion.getRank().orElse(new Rank()).getDefaultColor().serialize(),
              serverChampion.getAffixes().stream().map(IAffix::getIdentifier).collect(Collectors.toSet()))
          );
        }
      });
    }
  }
}
