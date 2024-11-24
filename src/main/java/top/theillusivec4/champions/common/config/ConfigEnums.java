package top.theillusivec4.champions.common.config;

import net.minecraft.util.StringRepresentable;

public class ConfigEnums {

  public enum Permission implements StringRepresentable {
    BLACKLIST("blacklist"),
    WHITELIST("whitelist");
    final String name;

    Permission(String name) {
      this.name = name;
    }


    @Override
    public String getSerializedName() {
      return this.name;
    }
  }

  public enum LootSource {
    CONFIG,
    LOOT_TABLE,
    CONFIG_AND_LOOT_TABLE
  }
}
