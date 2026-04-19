package top.theillusivec4.champions.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.affix.Affix;
import top.theillusivec4.champions.api.affix.Affixes;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.tags.AffixTags;

import java.util.concurrent.CompletableFuture;

public class AffixTagsProvider extends KeyTagProvider<Affix> {
  public AffixTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
    super(output, ChampionsRegistries.AFFIX, registries, modId);
  }

	public static KeyTagProvider<Affix> create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries){
		return new AffixTagsProvider(output, registries, Champions.MOD_ID);
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
