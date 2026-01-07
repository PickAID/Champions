package top.theillusivec4.champions.mixin;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
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
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.components.DataComponents;
import top.theillusivec4.champions.components.ItemAffixes;

import java.util.function.Consumer;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity> implements EntityTypeTest<Entity, T>, FeatureElement {
  @Inject(method = "appendCustomEntityStackConfig", at = @At(value = "RETURN"), cancellable = true)
  private static <T extends Entity> void champion$appendCustomEntityStackConfig(Consumer<T> initialConfig, Level level, ItemStack itemStack, @Nullable LivingEntity user, CallbackInfoReturnable<Consumer<T>> cir) {
    ItemAffixes itemAffixes = itemStack.get(DataComponents.ITEM_AFFIXES);
    Holder<Rank> rank = itemStack.get(DataComponents.RANK);
    Integer lvl = itemStack.get(DataComponents.LEVEL);
    Integer color = itemStack.get(DataComponents.COLOR);
    Component prefixName = itemStack.get(DataComponents.PREFIX_NAME);

    if (itemAffixes != null) {
      cir.setReturnValue(champion$appendAffixesConfig(rank, lvl, color, prefixName, itemAffixes, cir.getReturnValue()));
    }
  }

  @Unique
  private static <T extends Entity> Consumer<T> champion$appendAffixesConfig(@Nullable Holder<Rank> rank, @Nullable Integer level, @Nullable Integer color, @Nullable Component prefixName, ItemAffixes affixes, Consumer<T> consumer) {
    return consumer.andThen(entity -> ChampionUtil.getHandler(entity).ifPresent(handler -> {
      if (rank != null) {
        handler.setRank(rank);
      }

      if (level != null) {
        handler.setLevel(level);
      }

      if (color != null) {
        handler.setColor(color);
      }

      if (prefixName != null) {
        handler.setPrefixName(prefixName);
      }

      handler.updateAffixes(mutable -> {
        for (Holder<Affix> affix : affixes.affixes()) {
          mutable.add(affix);
        }
      });
    }));
  }
}
