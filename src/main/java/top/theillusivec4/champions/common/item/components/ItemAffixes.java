package top.theillusivec4.champions.common.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import top.theillusivec4.champions.api.affix.Affix;

import java.util.ArrayList;
import java.util.List;

public record ItemAffixes(int level, List<Holder<Affix>> affixes) {
  public static final Codec<ItemAffixes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    Codec.INT.fieldOf("level").forGetter(ItemAffixes::level),
    Affix.REFERENCE_CODEC.listOf().fieldOf("affixes").forGetter(ItemAffixes::affixes)
  ).apply(instance, ItemAffixes::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, ItemAffixes> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT, ItemAffixes::level,
    Affix.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)), ItemAffixes::affixes,
    ItemAffixes::new
  );
}
