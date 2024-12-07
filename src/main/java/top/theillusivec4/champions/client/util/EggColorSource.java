package top.theillusivec4.champions.client.util;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;

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
    return eggItem != null ? ARGB.opaque(eggItem.getBarColor(new ItemStack(eggItem))) : defaultColor;
  }

  @Override
  public MapCodec<? extends ItemTintSource> type() {
    return MAP_CODEC;
  }
}
