package top.theillusivec4.champions.api.data;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum AffixCategory implements StringRepresentable {
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
