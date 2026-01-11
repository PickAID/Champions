package top.theillusivec4.champions.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.champions.champion.ChampionUtil;

import java.util.function.Consumer;

/**
 * 实现了使用刷怪蛋生成时从物品读取冠军数据的功能
 * @param <T>
 */
@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity> implements EntityTypeTest<Entity, T>, FeatureElement {
  @Inject(method = "appendCustomEntityStackConfig", at = @At(value = "RETURN"), cancellable = true)
  private static <T extends Entity> void champion$appendCustomEntityStackConfig(Consumer<T> initialConfig, Level level, ItemStack itemStack, @Nullable LivingEntity user, CallbackInfoReturnable<Consumer<T>> cir) {
    cir.setReturnValue(champion$appendChampionConfig(itemStack, cir.getReturnValue()));
//    Affixes affixes = itemStack.get(DataComponents.AFFIXES);
//    Holder<Rank> rank = itemStack.get(DataComponents.RANK);
//    Integer lvl = itemStack.get(DataComponents.LEVEL);
//    Integer color = itemStack.get(DataComponents.COLOR);
//    Component prefixName = itemStack.get(DataComponents.PREFIX_NAME);
//
//    if (affixes != null) {
//      cir.setReturnValue(champion$appendAffixesConfig(rank, lvl, color, prefixName, affixes, cir.getReturnValue()));
//    }
  }

  @Unique
  private static <T extends Entity> Consumer<T> champion$appendChampionConfig(ItemStack itemStack, Consumer<T> consumer) {
    return consumer.andThen(entity -> ChampionUtil.getHandler(entity).ifPresent(handler -> ChampionUtil.getHandler(itemStack).ifPresent(handlerItem -> handler.applyConfig(handlerItem.deriveConfig()))));
  }

//  @Unique
//  private static <T extends Entity> Consumer<T> champion$appendAffixesConfig(@Nullable Holder<Rank> rank, @Nullable Integer level, @Nullable Integer color, @Nullable Component prefixName, Affixes affixes, Consumer<T> consumer) {
//    return consumer.andThen(entity -> ChampionUtil.getHandler(entity).ifPresent(handler -> {
//      if (rank != null) {
//        handler.setRank(rank);
//      }
//
//      if (level != null) {
//        handler.setLevel(level);
//      }
//
//      if (color != null) {
//        handler.setColor(color);
//      }
//
//      if (prefixName != null) {
//        handler.setPrefixName(prefixName);
//      }
//
//      handler.updateAffixes(mutable -> {
//        for (Holder<Affix> affix : affixes.getAffixes()) {
//          mutable.add(affix);
//        }
//      });
//    }));
//  }
}
