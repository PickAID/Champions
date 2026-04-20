package top.theillusivec4.champions.api.affix;

import net.minecraft.core.Holder;

public record AffixInstance(Holder<Affix> affix, int level) {
}
