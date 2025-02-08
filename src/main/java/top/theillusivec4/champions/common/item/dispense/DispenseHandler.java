package top.theillusivec4.champions.common.item.dispense;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.item.ChampionEggItem;
import top.theillusivec4.champions.common.util.ChampionHelper;

import java.util.Optional;

public class DispenseHandler {

    public static ItemStack handleChampionEggDispense(BlockSource source, ItemStack stack) {
        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
        Optional<EntityType<?>> entityType = ChampionEggItem.getType(stack);
        entityType.ifPresent(type -> {
            Entity entity = type.create(source.getLevel(), stack.getTag(), null, null,
                    source.getPos().relative(direction), MobSpawnType.DISPENSER, true,
                    direction != Direction.UP);

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
