package top.theillusivec4.champions.championegg;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.affix.AffixHelper;
import top.theillusivec4.champions.attachments.ChampionsAttachments;
import top.theillusivec4.champions.championmob.property.ChampionProperty;
import top.theillusivec4.champions.championmob.property.ChampionPropertyHelper;

import java.util.Objects;
import java.util.function.Consumer;

public final class ChampionMobEggHelper {
  private ChampionMobEggHelper() {
  }

  public static void addToTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> adder, TooltipFlag flags) {
    ChampionPropertyHelper.addToTooltip(stack, context, adder, flags);
    AffixHelper.addToTooltip(stack, context, adder, flags);
  }

  public static void applyEntityConfig(ItemStack stack, Entity entity) {
    AffixContainer container = AffixHelper.getStored(stack);
    ChampionProperty property = ChampionPropertyHelper.getStored(stack);
    boolean flag = false;

    if (!Objects.equals(container, AffixContainer.EMPTY)) {
      AffixHelper.setToEntity(entity, container);
      flag = true;
    }
    if (!Objects.equals(property, ChampionProperty.EMPTY)) {
      ChampionPropertyHelper.setToEntity(entity, property);
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
    modifyItem(stack, ChampionPropertyHelper.get(entity), AffixHelper.get(entity));
  }

  public static void modifyItem(ItemStack stack, ChampionProperty property, AffixContainer affixes) {
    ChampionPropertyHelper.setToItem(stack, property);
    AffixHelper.setToItem(stack, affixes);
  }
}
