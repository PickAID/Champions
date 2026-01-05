package top.theillusivec4.champions.api.affix.effect;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum AffixPosition implements StringRepresentable {
  ENTITY("entity"),
  CONTEXT("context");
  public static final Codec<AffixPosition> CODEC = StringRepresentable.fromEnum(AffixPosition::values);

  private final String name;

  AffixPosition(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
