package top.theillusivec4.champions.data.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.ChampionsMod;
import top.theillusivec4.champions.affix.Affix;
import top.theillusivec4.champions.affix.Affixes;
import top.theillusivec4.champions.registries.ChampionsRegistries;
import top.theillusivec4.champions.tags.AffixTags;

import java.util.concurrent.CompletableFuture;

public class AffixTagsProvider extends TagsProvider<Affix> {
  public AffixTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, ChampionsRegistries.AFFIX, lookupProvider, ChampionsMod.MOD_ID, existingFileHelper);
  }

  public AffixTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, ChampionsRegistries.AFFIX, lookupProvider, modId, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
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
