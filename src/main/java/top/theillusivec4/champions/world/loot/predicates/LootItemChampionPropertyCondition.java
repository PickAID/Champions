package top.theillusivec4.champions.world.loot.predicates;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import top.theillusivec4.champions.champion.ChampionUtil;
import top.theillusivec4.champions.champion.affix.Affix;

import java.util.Optional;

public record LootItemChampionPropertyCondition(LootContext.EntityTarget entityTarget) implements LootItemCondition {

  @Override
  public MapCodec<? extends LootItemCondition> codec() {
    return null;
  }

  @Override
  public boolean test(LootContext context) {
    return false;
  }

  public record ChampionPredicate(
    Optional<MinMaxBounds.Ints> level,
    Optional<Component> prefixName,
    Optional<TextColor> color,
    Optional<HolderSet<Affix>> affixes
  ) {
    public boolean matches(Entity entity) {
      return ChampionUtil.getHandler(entity)
        .map(handler -> {
          if (this.level.isPresent() && !this.level.get().matches(handler.getLevel())) {
            return false;
          }





          return true;
        })
        .orElse(false);
    }
  }
}
