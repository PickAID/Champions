package top.theillusivec4.champions.world.item.champion;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.champions.core.attachment.ChampionsAttachments;
import top.theillusivec4.champions.world.entity.affix.AffixHelper;
import top.theillusivec4.champions.world.entity.affix.EntityAffixes;
import top.theillusivec4.champions.world.entity.champion.property.ChampionMobProperty;
import top.theillusivec4.champions.world.entity.champion.property.ChampionMobPropertyHelper;

import java.util.Objects;
import java.util.function.Consumer;

public final class ChampionEggHelper {
  private ChampionEggHelper() {
  }

  public static void addToTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flags) {
    ChampionMobPropertyHelper.addToTooltip(stack, context, adder, flags);
    AffixHelper.addToTooltip(stack, context, adder, flags);
  }

  public static void applyEntityConfig(ItemStack stack, Entity entity) {
    EntityAffixes container = AffixHelper.get(stack);
    ChampionMobProperty property = ChampionMobPropertyHelper.get(stack);
    boolean flag = false;

    if (!Objects.equals(container, EntityAffixes.EMPTY)) {
      AffixHelper.set(entity, container);
      flag = true;
    }
    if (!Objects.equals(property, ChampionMobProperty.EMPTY)) {
      ChampionMobPropertyHelper.set(entity, property);
      flag = true;
    }
    if (flag) {
      entity.setData(ChampionsAttachments.CHAMPION_MOB_EGG_SPAWNED, true);
    }
  }

  public static boolean isSpawnFor(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.CHAMPION_MOB_EGG_SPAWNED).orElse(false);
  }

  public static void modifyPickResult(ItemStack stack, Entity entity) {
    modifyItem(stack, ChampionMobPropertyHelper.get(entity), AffixHelper.get(entity));
  }

  public static void modifyItem(ItemStack stack, ChampionMobProperty property, EntityAffixes affixes) {
    ChampionMobPropertyHelper.set(stack, property);
    AffixHelper.set(stack, affixes);
  }
}
