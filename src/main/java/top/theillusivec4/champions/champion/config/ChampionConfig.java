package top.theillusivec4.champions.champion.config;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.champions.champion.Affixes;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.rank.Rank;

import java.util.Optional;

/**
 * 将冠军数据组织进一个配置对象应该对数据转移有所帮助
 *
 * @param rank
 * @param prefixName
 * @param affixes
 * @param level
 * @param color
 * @param boss
 */
public record ChampionConfig(
  Optional<Holder<Rank>> rank,
  Optional<Component> prefixName,
  Optional<Affixes> affixes,
  Optional<Integer> level,
  Optional<Integer> color,
  Optional<Boolean> boss
) {

}
