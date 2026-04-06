package top.theillusivec4.champions.data.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.champion.affix.Affix;
import top.theillusivec4.champions.champion.affix.Affixes;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.tag.AffixTags;

import java.util.concurrent.CompletableFuture;

public class AffixTagsProvider extends KeyTagProvider<Affix> {
  public AffixTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
    super(output, ChampionsRegistries.AFFIX, registries, modId);
  }

  public AffixTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, ChampionsRegistries.AFFIX, registries, Champions.MODID);
  }

  @Override
  protected void addTags(HolderLookup.Provider registries) {
    tag(AffixTags.DAMAGE_IMMUNITY_EXCLUSIVE)
      .add(Affixes.SHIELDING) // 保护
      .add(Affixes.MOLTEN); // 熔融
    tag(AffixTags.DAMAGE_PROTECTION_EXCLUSIVE)
      .add(Affixes.ADAPTABLE) // 适应
      .add(Affixes.DAMPENING); // 抑制
    tag(AffixTags.DAMAGE_EXCLUSIVE)
      .add(Affixes.KNOCKING) // 爆震
      .add(Affixes.MOLTEN) // 熔融
      .add(Affixes.PARALYZING); // 瘟疫
    tag(AffixTags.RANGE_EXCLUSIVE)
      .add(Affixes.PLAGUED); // 瘟疫
    tag(AffixTags.CURSE)
      .add(Affixes.WOUNDING);

  }
}
