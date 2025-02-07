package top.theillusivec4.champions.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.registry.ModDamageTypes;

import java.util.concurrent.CompletableFuture;


public class ModDamageTypeTagsProvider extends TagsProvider<DamageType> {

  public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
    super(output, Registries.DAMAGE_TYPE, future, Champions.MODID);
  }

  @Override
  protected void addTags(@NotNull HolderLookup.Provider provider) {
    tag(DamageTypeTags.IS_FIRE).add(ModDamageTypes.ENKINDLING_BULLET);
  }

}
