package top.theillusivec4.champions.integration.theoneprobe.styles;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record Padding(int left, int right, int bottom, int top) {
  public static final Padding EMPTY = new Padding(0, 0, 0, 0);
  public static final StreamCodec<ByteBuf, Padding> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.INT, Padding::left,
    ByteBufCodecs.INT, Padding::right,
    ByteBufCodecs.INT, Padding::bottom,
    ByteBufCodecs.INT, Padding::top,
    Padding::new
  );

  public static class Mutable {
    private int left;
    private int right;
    private int bottom;
    private int top;

    public Mutable left(int left) {
      this.left = left;
      return this;
    }

    public Mutable right(int right) {
      this.right = right;
      return this;
    }

    public Mutable bottom(int bottom) {
      this.bottom = bottom;
      return this;
    }

    public Mutable top(int top) {
      this.top = top;
      return this;
    }

    public Padding toImmutable() {
      return new Padding(left, right, bottom, top);
    }
  }
}
