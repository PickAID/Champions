package top.theillusivec4.champions.common.integration.gateways_to_eternity;

import net.minecraft.util.StringRepresentable;

public enum Gateways implements StringRepresentable {
    NORMAL("normal"),ENDLESS("endless");

    final String name;
    Gateways(String name) {
        this.name = name;
    }
    @Override
    public String getSerializedName() {
        return name;
    }
}
