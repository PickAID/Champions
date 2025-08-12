package top.theillusivec4.champions.api;

import net.minecraft.util.StringRepresentable;

public enum Sephirah implements StringRepresentable {
  Malchut("Malchut"),
  Yesod("Yesod"),
  Hod("Hod"),
  Netzach("Netzach"),
  Tiphereth("Tiphereth"),
  Geburah("Geburah"),
  Chesed("Chesed"),
  Binah("Binah"),
  Cochma("Cochma"),
  Kether("Kether"),
  ;

  final String name;

  Sephirah(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }
}
