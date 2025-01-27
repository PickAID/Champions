package top.theillusivec4.champions.common.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.registry.ModEntityTypes;

import javax.annotation.Nullable;

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
    }

    @Override
    public String getName() {
        return "Champions Entity Type Tags";
    }
}
