package top.theillusivec4.champions.spawnegg;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.champions.affix.AffixHelper;
import top.theillusivec4.champions.champion.ChampionHelper;

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

  public static void modifyPickResult(ItemStack itemStack, Entity entity) {
    ChampionHelper.setToItem(itemStack, ChampionHelper.get(entity));
    AffixHelper.setToItem(itemStack, AffixHelper.get(entity));
  }
}
