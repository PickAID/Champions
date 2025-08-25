package top.theillusivec4.champions.common.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.registry.ModEntityTypes;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Stream;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {

	public ModEntityTypeTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, Champions.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(ModEntityTypes.Tags.IS_ENDER)
				.add(EntityType.ENDER_DRAGON)
				.add(EntityType.ENDERMITE)
				.add(EntityType.ENDERMAN)
				.add(EntityType.SHULKER);
		// add champion mob allow list
		lookUpMonster().forEach(this::addEntity);
	}

	private Stream<Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>>> lookUpMonster() {
		return ForgeRegistries.ENTITIES.getEntries().stream().filter(resourceKeyEntityTypeEntry -> resourceKeyEntityTypeEntry.getValue().getCategory() == MobCategory.MONSTER);
	}

	void addEntity(Map.Entry<ResourceKey<EntityType<?>>, EntityType<?>> entityTypeHolder) {
		tag(ModEntityTypes.Tags.ALLOW_CHAMPIONS).add(entityTypeHolder.getKey());
	}

	@Override
	public String getName() {
		return "Champions Entity Type Tags";
	}
}
