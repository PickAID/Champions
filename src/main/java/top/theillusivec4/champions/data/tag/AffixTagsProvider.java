package top.theillusivec4.champions.data.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.Affixes;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.tag.AffixTags;

import java.util.concurrent.CompletableFuture;

public class AffixTagsProvider extends KeyTagProvider<Affix> {
  public AffixTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
    super(output, Registries.AFFIX, registries, modId);
  }

  public AffixTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, Registries.AFFIX, registries, Champions.MODID);
  }

  @Override
  protected void addTags(HolderLookup.Provider registries) {
    tag(AffixTags.DAMAGE_IMMUNITY_EXCLUSIVE)
      .add(Affixes.SHIELDING);
    tag(AffixTags.DAMAGE_PROTECTION_EXCLUSIVE)
      .add(Affixes.ADAPTABLE)
      .add(Affixes.DAMPENING)
      .add(Affixes.MOLTEN);
    tag(AffixTags.DAMAGE_EXCLUSIVE)
      .add(Affixes.KNOCKING)
      .add(Affixes.MOLTEN)
      .add(Affixes.PARALYZING);
    tag(AffixTags.RANGE_EXCLUSIVE)
      .add(Affixes.PLAGUED);
    tag(AffixTags.CURSE)
      .add(Affixes.WOUNDING);

  }
}
