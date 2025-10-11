package top.theillusivec4.champions.common.item.dispense;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.item.ChampionEggItem;
import top.theillusivec4.champions.common.util.ChampionHelper;

import java.util.Optional;

public class DispenseHandler {
	public static ItemStack handleChampionEggDispense(IBlockSource source, ItemStack stack) {
		Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
		Optional<EntityType<?>> entityType = ChampionEggItem.getType(stack);
		entityType.ifPresent(type -> {
			Entity entity = type.create(source.getLevel(), stack.getTag(), null, null, source.getPos().relative(direction), SpawnReason.DISPENSER, true, direction != Direction.UP);

			if (entity != null) {
				ChampionCapability.getCapability(entity).ifPresent(champion -> {
					if (ChampionHelper.isValidChampion(champion.getServer())) {
						ChampionEggItem.read(champion, stack);
						source.getLevel().addFreshEntity(champion.getLivingEntity());
						stack.shrink(1);
					}
				});
			}
		});
		return stack;
	}
}
