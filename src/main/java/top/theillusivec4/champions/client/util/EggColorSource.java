package top.theillusivec4.champions.client.util;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.common.mixin.BlockModelWrapperMixin;

import java.util.Arrays;

import static top.theillusivec4.champions.common.item.ChampionEggItem.getType;

public record EggColorSource(int defaultColor) implements ItemTintSource {
  public static final MapCodec<EggColorSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
    instance.group(
      ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(EggColorSource::defaultColor)
    ).apply(instance, EggColorSource::new)
  );

  public EggColorSource(int defaultColor) {
    this.defaultColor = ARGB.opaque(defaultColor);
  }

  @Override
  public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
    SpawnEggItem eggItem = SpawnEggItem.byId(getType(itemStack).orElse(EntityType.ZOMBIE));

    int[] tints = new int[0];
    if (eggItem == null) {
      return defaultColor;
    }
    var itemName = BuiltInRegistries.ITEM.getKey(eggItem);
    ItemModel itemModel = Minecraft.getInstance().getModelManager().getItemModel(itemName);
    if (itemModel instanceof BlockModelWrapperMixin blockModelWrapperMixin) {
      var tintSourceList = blockModelWrapperMixin.getTints();
      tints = new int[tintSourceList.size()];
      for (int j = 0; j < tints.length; j++) {
        tints[j] =tintSourceList.get(j).calculate(itemStack, clientLevel, livingEntity);
      }
      int[] aint = prepareTintLayers(tints, tints.length);
      System.arraycopy(tints, 0, aint, 0, tints.length);
    }
    return tints.length > 0 ? tints[0] : defaultColor;
  }

  public int[] prepareTintLayers(int[] tintLayers, int count) {
    if (count > tintLayers.length) {
      tintLayers = new int[count];
      Arrays.fill(tintLayers, -1);
    }

    return tintLayers;
  }

  @Override
  public MapCodec<? extends ItemTintSource> type() {
    return MAP_CODEC;
  }
}
