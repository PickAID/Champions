package top.theillusivec4.champions.spawnegg;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.affix.AffixHelper;
import top.theillusivec4.champions.champion.ChampionHelper;
import top.theillusivec4.champions.champion.ChampionProperty;

import java.util.function.Consumer;

public final class SpawnEggHelper {
  private SpawnEggHelper() {
  }

  public static void addToTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flags) {
    ChampionHelper.addToTooltip(stack, context, adder, flags);
    AffixHelper.addToTooltip(stack, context, adder, flags);
  }

  public static void applyEntityConfig(ItemStack stack, Entity entity) {
    AffixHelper.setToEntity(entity, AffixHelper.getStored(stack));
    ChampionHelper.setToEntity(entity, ChampionHelper.getStored(stack));
  }

  public static void modifyPickResult(ItemStack stack, Entity entity) {
    modifyItem(stack, ChampionHelper.get(entity), AffixHelper.get(entity));
  }

  public static void modifyItem(ItemStack stack, ChampionProperty property, AffixContainer affixes) {
    ChampionHelper.setToItem(stack, property);
    AffixHelper.setToItem(stack, affixes);
  }
}
