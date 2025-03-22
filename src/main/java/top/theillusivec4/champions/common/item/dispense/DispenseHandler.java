package top.theillusivec4.champions.common.item.dispense;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import top.theillusivec4.champions.common.capability.ChampionAttachment;
import top.theillusivec4.champions.common.item.ChampionEggItem;
import top.theillusivec4.champions.common.util.ChampionHelper;

import java.util.Optional;

public class DispenseHandler {
  public static ItemStack handleChampionEggDispense(BlockSource source, ItemStack stack) {
    Direction direction = source.state().getValue(DispenserBlock.FACING);
    Optional<EntityType<?>> entityType = ChampionEggItem.getType(stack);
    entityType.ifPresent(type -> {
      Entity entity = type.create(source.level(), (s) -> stack.getTags(), source.pos().relative(direction), EntitySpawnReason.DISPENSER, true, direction != Direction.UP);
      ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
        if (ChampionHelper.isValidChampion(champion.getServer())) {
          ChampionEggItem.read(champion, stack);
          source.level().addFreshEntity(champion.getLivingEntity());
          stack.shrink(1);
        }
      });
    });
    return stack;
  }
}
