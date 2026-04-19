package top.theillusivec4.champions.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class StreamCodecs {
  public static final StreamCodec<ByteBuf, TextColor> TEXT_COLOR = ByteBufCodecs.fromCodec(TextColor.CODEC);

  private StreamCodecs() {
  }
}
