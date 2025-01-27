package top.theillusivec4.champions.api;

import net.minecraft.util.StringRepresentable;

public enum AffixCategory implements StringRepresentable {
    CC("cc"), OFFENSE("offense"), DEFENSE("defence");

    final String name;

    AffixCategory(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
