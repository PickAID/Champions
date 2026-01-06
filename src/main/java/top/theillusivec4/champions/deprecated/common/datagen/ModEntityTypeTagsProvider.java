package top.theillusivec4.champions.deprecated.common.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.entities.EntityTypes;

import java.util.concurrent.CompletableFuture;


@Deprecated
public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {

  public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
    super(output, future, Champions.MODID);
  }

  @Override
  protected void addTags(@NotNull HolderLookup.Provider provider) {
    tag(EntityTypes.Tags.IS_ENDER).add(lookup(provider, "ender_dragon"));
    tag(EntityTypes.Tags.IS_ENDER).add(lookup(provider, "endermite"));
    tag(EntityTypes.Tags.IS_ENDER).add(lookup(provider, "enderman"));
    tag(EntityTypes.Tags.IS_ENDER).add(lookup(provider, "shulker"));
    // add champion allow list
    lookUpMonster(provider).listElements().forEach(this::addEntity);
  }

  void addEntity(Holder.Reference<EntityType<?>> entityType) {
    tag(EntityTypes.Tags.ALLOW_CHAMPIONS).add(entityType.value());
  }

  private HolderLookup.RegistryLookup<EntityType<?>> lookUpMonster(@NotNull HolderLookup.Provider provider) {
    return provider.lookupOrThrow(Registries.ENTITY_TYPE).filterElements(entityType -> entityType.getCategory() == MobCategory.MONSTER);
  }

  private ResourceKey<EntityType<?>> create(String name) {
    return ResourceKey.create(Registries.ENTITY_TYPE, Identifier.parse(name));
  }

  private EntityType<?> lookup(HolderLookup.Provider provider, String name) {
    return provider.lookupOrThrow(Registries.ENTITY_TYPE).getOrThrow(create(name)).value();
  }
}
