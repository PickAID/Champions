package top.theillusivec4.champions.affix;

import net.minecraft.core.Holder;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;

public record AffixInstance(Holder<Affix> affix, int level) implements WeightedEntry {
  @Override
  public Weight getWeight() {
    return Weight.of(this.affix.value().definition().weight());
  }
}
