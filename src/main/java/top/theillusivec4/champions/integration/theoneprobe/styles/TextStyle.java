package top.theillusivec4.champions.integration.theoneprobe.styles;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.ITextStyle;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import top.theillusivec4.champions.integration.theoneprobe.ChampionsTheOneProbePlugin;

import java.util.Optional;

public record TextStyle(Padding padding, Integer width, Integer height, ElementAlignment alignment) implements ITextStyle {
  public static final TextStyle EMPTY = new TextStyle(Padding.EMPTY, null, null, ElementAlignment.ALIGN_TOPLEFT);
  public static final StreamCodec<ByteBuf, TextStyle> STREAM_CODEC = StreamCodec.composite(
    Padding.STREAM_CODEC, TextStyle::padding,
    ByteBufCodecs.optional(ByteBufCodecs.INT), style -> Optional.ofNullable(style.width),
    ByteBufCodecs.optional(ByteBufCodecs.INT), style -> Optional.ofNullable(style.height),
    ChampionsTheOneProbePlugin.StreamCodecs.ELEMENT_ALIGNMENT, style -> style.alignment,
    (padding, width, height, alignment) -> new TextStyle(
      padding,
      width.orElse(null),
      height.orElse(null),
      alignment
    )
  );


  public static TextStyle create(int leftPadding, int rightPadding, int topPadding, int bottomPadding, Integer width, Integer height, ElementAlignment alignment) {
    return new TextStyle(new Padding(leftPadding, rightPadding, bottomPadding, topPadding), width, height, alignment);
  }

  @Override
  public TextStyle.Mutable copy() {
    return new Mutable(padding.left(), padding.right(), padding.bottom(), padding.top(), this.alignment);
  }

  @Override
  public ITextStyle topPadding(int i) {
    return this;
  }

  @Override
  public ITextStyle bottomPadding(int i) {
    return null;
  }

  @Override
  public ITextStyle leftPadding(int i) {
    return this;
  }

  @Override
  public ITextStyle rightPadding(int i) {
    return this;
  }

  @Override
  public ITextStyle width(Integer integer) {
    return this;
  }

  @Override
  public ITextStyle height(Integer integer) {
    return this;
  }

  @Override
  public ITextStyle alignment(ElementAlignment elementAlignment) {
    return this;
  }

  @Override
  public int getLeftPadding() {
    return padding.left();
  }

  @Override
  public int getRightPadding() {
    return padding.right();
  }

  @Override
  public int getTopPadding() {
    return padding.top();
  }

  @Override
  public int getBottomPadding() {
    return padding.bottom();
  }

  @Override
  public Integer getWidth() {
    return null;
  }

  @Override
  public Integer getHeight() {
    return null;
  }

  @Override
  public ElementAlignment getAlignment() {
    return alignment;
  }

  public static class Mutable implements ITextStyle {
    private int leftPadding;
    private int rightPadding;
    private int topPadding;
    private int bottomPadding;
    private Integer width = null;
    private Integer height = null;
    private ElementAlignment alignment;

    private Mutable(int leftPadding, int rightPadding, int topPadding, int bottomPadding, ElementAlignment alignment) {
      this.leftPadding = leftPadding;
      this.rightPadding = rightPadding;
      this.topPadding = topPadding;
      this.bottomPadding = bottomPadding;
      this.alignment = alignment;
    }

    @Override
    public ITextStyle copy() {
      return new Mutable(this.leftPadding, this.rightPadding, this.topPadding, this.bottomPadding, this.alignment);
    }

    @Override
    public ITextStyle topPadding(int i) {
      topPadding = i;
      return this;
    }

    @Override
    public ITextStyle bottomPadding(int i) {
      bottomPadding = i;
      return this;
    }

    @Override
    public ITextStyle leftPadding(int i) {
      leftPadding = i;
      return this;
    }

    @Override
    public ITextStyle rightPadding(int i) {
      rightPadding = i;
      return this;
    }

    @Override
    public ITextStyle width(Integer integer) {
      width = integer;
      return this;
    }

    @Override
    public ITextStyle height(Integer integer) {
      height = integer;
      return this;
    }

    @Override
    public ITextStyle alignment(ElementAlignment elementAlignment) {
      alignment = elementAlignment;
      return this;
    }

    @Override
    public int getLeftPadding() {
      return leftPadding;
    }

    @Override
    public int getRightPadding() {
      return rightPadding;
    }

    @Override
    public int getTopPadding() {
      return topPadding;
    }

    @Override
    public int getBottomPadding() {
      return bottomPadding;
    }

    @Override
    public Integer getWidth() {
      return width;
    }

    @Override
    public Integer getHeight() {
      return height;
    }

    @Override
    public ElementAlignment getAlignment() {
      return alignment;
    }
  }
}
