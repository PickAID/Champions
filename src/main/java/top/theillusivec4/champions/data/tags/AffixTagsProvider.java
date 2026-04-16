package top.theillusivec4.champions.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.tags.AffixTags;
import top.theillusivec4.champions.world.entity.affix.Affix;
import top.theillusivec4.champions.world.entity.affix.Affixes;

import java.util.concurrent.CompletableFuture;

public class AffixTagsProvider extends TagsProvider<Affix> {
  public AffixTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, ChampionsRegistries.AFFIX, lookupProvider, modId, existingFileHelper);
  }

  public static AffixTagsProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
    return new AffixTagsProvider(output, lookupProvider, Champions.MOD_ID, existingFileHelper);
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
