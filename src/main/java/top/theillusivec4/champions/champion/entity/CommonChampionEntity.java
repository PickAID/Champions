package top.theillusivec4.champions.champion.entity;

import net.minecraft.world.entity.Entity;

/**
 * 实体的通用实现
 * @param entity 实体
 */
@Deprecated
public record CommonChampionEntity(Entity entity) implements ChampionHandlerEntity {

}
