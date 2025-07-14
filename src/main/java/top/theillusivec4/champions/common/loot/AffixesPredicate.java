package top.theillusivec4.champions.common.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;
import top.theillusivec4.champions.api.affix.IAffix;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record AffixesPredicate(Set<ResourceLocation> values, MinMaxBounds.Ints matches,
                               MinMaxBounds.Ints count) {

    public static final Codec<AffixesPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      NeoForgeExtraCodecs.setOf(ResourceLocation.CODEC).fieldOf("values").forGetter(AffixesPredicate::values),
      MinMaxBounds.Ints.CODEC.fieldOf("matches").forGetter(AffixesPredicate::matches),
      MinMaxBounds.Ints.CODEC.fieldOf("count").forGetter(AffixesPredicate::count)
    ).apply(instance, AffixesPredicate::new));

    public boolean matches(List<IAffix> input) {

      if (this.values.isEmpty()) {
        return this.count.matches(input.size());
      } else {
        var affixes = input.stream().map(IAffix::getIdentifier).collect(Collectors.toSet());
        var found = (int) values.stream().filter(affixes::contains).count();
        return this.matches.matches(found) && this.count.matches(input.size());
      }
    }
  }
