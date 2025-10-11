package top.theillusivec4.champions.common.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
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

	private Stream<Map.Entry<RegistryKey<EntityType<?>>, EntityType<?>>> lookUpMonster() {
		return ForgeRegistries.ENTITIES.getEntries().stream().filter(resourceKeyEntityTypeEntry -> resourceKeyEntityTypeEntry.getValue().getCategory() == EntityClassification.MONSTER);
	}

	void addEntity(Map.Entry<RegistryKey<EntityType<?>>, EntityType<?>> entityTypeHolder) {
		tag(ModEntityTypes.Tags.ALLOW_CHAMPIONS).add(entityTypeHolder.getKey());
	}

	@Override
	public String getName() {
		return "Champions Entity Type Tags";
	}
}
