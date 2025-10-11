package top.theillusivec4.champions.api.data;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum AffixCategory implements IStringSerializable {
    CC("cc"), OFFENSE("offense"), DEFENSE("defense");

    final String name;

    AffixCategory(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name.toUpperCase(Locale.ROOT);
    }
}
