package top.theillusivec4.champions.champion.affix.effect;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum AffixTarget implements StringRepresentable {
  ATTACKER("attacker"),
  DAMAGING_ENTITY("damaging_entity"),
  VICTIM("victim");
  public static final Codec<AffixTarget> CODEC = StringRepresentable.fromEnum(AffixTarget::values);

  private final String name;

  AffixTarget(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return name;
  }
}
