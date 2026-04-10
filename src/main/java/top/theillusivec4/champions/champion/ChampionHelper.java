package top.theillusivec4.champions.champion;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.affix.AffixHelper;
import top.theillusivec4.champions.attachments.ChampionsAttachments;
import top.theillusivec4.champions.particles.ChampionsParticleTypes;
import top.theillusivec4.champions.server.boss.ChampionsServerBossEvent;
import top.theillusivec4.champions.util.ChampionsEmpties;

import java.util.Objects;

public final class ChampionHelper {
  private ChampionHelper() {
  }

  public static int getLevel(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.CHAMPION_LEVEL).orElse(1);
  }

  public static Component getNamePrefix(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.NAME_PREFIX).orElse(CommonComponents.EMPTY);
  }

  public static TextColor getNameColor(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.NAME_COLOR).orElse(ChampionsEmpties.TEXT_COLOR);
  }

  public static ChampionsServerBossEvent getBossbar(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.BOSS_EVENT).orElse(ChampionsServerBossEvent.EMPTY);
  }

  public static Component getDisplayName(Entity entity) {
    MutableComponent component = Component.empty().copy();
    Component prefix = getNamePrefix(entity);
    if (prefix != CommonComponents.EMPTY) {
      component.append(prefix);
      component.append(CommonComponents.SPACE);
    }
    component.append(entity.getDisplayName());
    component.withStyle(style -> style.withColor(getNameColor(entity)));
    return component;
  }

  public static boolean isBoss(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.BOSS_EVENT).isPresent();
  }

  public static void doParticlesEffects(Entity entity) {
    if (!AffixHelper.get(entity).isEmpty()) {
      RandomSource randomSource = entity.getRandom();
      Vec3 position = entity.position();
      double x = position.x() + (randomSource.nextDouble() - 0.5) * entity.getBbWidth();
      double y = position.y() + randomSource.nextDouble() * entity.getBbHeight();
      double z = position.z() + (randomSource.nextDouble() - 0.5) * entity.getBbWidth();
      int color = getNameColor(entity).getValue();
      entity.level().addParticle(ChampionsParticleTypes.champion(color), x, y, z, 1.0f, 1.0f, 1.0f);
    }
  }

  public static void updateBossbarPlayers(Entity entity) {
    if (entity.level() instanceof ServerLevel level) {
      ChampionsServerBossEvent event = getBossbar(entity);
      if (event != ChampionsServerBossEvent.EMPTY) {
        for (ServerPlayer player : level.players()) {
          if (player.blockPosition().distSqr(entity.blockPosition()) <= 3025.0) {
            event.addPlayer(player);
          } else {
            event.removePlayer(player);
          }
        }
      }
    }
  }

  public static void updateBossbarProgress(Entity entity) {
    if (entity.level() instanceof ServerLevel level && entity instanceof LivingEntity livingEntity) {
      ChampionsServerBossEvent event = getBossbar(entity);
      if (event != ChampionsServerBossEvent.EMPTY) {
        float progress = livingEntity.getHealth() / livingEntity.getMaxHealth();
        if (!Float.isNaN(progress)) {
          event.setProgress(progress);
        }
      }
    }
  }

  public static void setNameColor(Entity entity, TextColor color) {
    if (Objects.equals(ChampionsEmpties.TEXT_COLOR, color)) {
      entity.removeData(ChampionsAttachments.NAME_COLOR);
    } else {
      entity.setData(ChampionsAttachments.NAME_COLOR, color);
    }

    ChampionsServerBossEvent event = getBossbar(entity);
    if (event != ChampionsServerBossEvent.EMPTY) {
      event.setColor(getNameColor(entity));
    }
  }

  public static void setBoss(Entity entity) {
    if (getBossbar(entity) == ChampionsServerBossEvent.EMPTY) {
      ChampionsServerBossEvent event = new ChampionsServerBossEvent(entity.getUUID(), getDisplayName(entity));
      event.setLevel(getLevel(entity));
      event.setColor(getNameColor(entity));
      event.setAffixes(AffixHelper.get(entity));
      entity.setData(ChampionsAttachments.BOSS_EVENT, event);
    }
  }

  public static void removeBoss(Entity entity) {
    ChampionsServerBossEvent event = entity.removeData(ChampionsAttachments.BOSS_EVENT);
    if (event != null) {
      event.removeAllPlayers();
    }
  }
}
