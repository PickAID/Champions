package top.theillusivec4.champions.common.config;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public class ConfigEnums {

    public enum Permission implements IStringSerializable {
        BLACKLIST("blacklist"),
        WHITELIST("whitelist");
        final String name;

        Permission(String name) {
            this.name = name;
        }


        @Override
        public String getSerializedName() {
	        return this.name.toUpperCase(Locale.ROOT);
        }
    }

    public enum LootSource {
        CONFIG,
        LOOT_TABLE,
        CONFIG_AND_LOOT_TABLE
    }
}
