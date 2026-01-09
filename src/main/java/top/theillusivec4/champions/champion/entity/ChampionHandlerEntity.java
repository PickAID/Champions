package top.theillusivec4.champions.champion.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;
import top.theillusivec4.champions.champion.ChampionHandler;
import top.theillusivec4.champions.server.level.ServerChampionBossEvent;

import java.awt.*;
import java.util.Optional;

/**
 * 受不了了我说实话，必须需要有一个统一接口来帮助判断它需不需要被生成事件监听器处理
 */
public interface ChampionHandlerEntity extends ChampionHandler {
  Component getName();

  Optional<ServerChampionBossEvent> getEvent();

  void setEvent(@Nullable ServerChampionBossEvent event);

  boolean isSpawned();

  void setSpawned(boolean spawned);

  Entity getEntity();

  float getHealth();

  float getMaxHealth();
}
