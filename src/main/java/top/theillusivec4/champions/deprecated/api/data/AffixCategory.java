package top.theillusivec4.champions.deprecated.api.data;

import net.minecraft.util.StringRepresentable;

@Deprecated
public enum AffixCategory implements StringRepresentable {
  CC("cc"), OFFENSE("offense"), DEFENSE("defense");

  final String name;

  AffixCategory(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
