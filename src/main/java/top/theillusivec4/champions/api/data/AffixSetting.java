package top.theillusivec4.champions.api.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.util.Utils;

import java.util.List;
import java.util.Optional;

public record AffixSetting(ResourceLocation type, boolean enabled, Optional<MinMaxBounds.Ints> tier,
                           Optional<List<ResourceLocation>> mobList, Optional<ConfigEnums.Permission> mobPermission,
                           AffixCategory category, Optional<String> prefix, Optional<Boolean> hasSub) {

    public static final Codec<AffixSetting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            // set affix type for data generation, must match this registry name
            ResourceLocation.CODEC.fieldOf("type").forGetter(AffixSetting::type),
            Codec.BOOL.fieldOf("enable").forGetter(AffixSetting::enabled),
            IntCodec.codec().optionalFieldOf("tier").forGetter(AffixSetting::tier),
            Codec.list(ResourceLocation.CODEC).optionalFieldOf("mobList").forGetter(AffixSetting::mobList),
            StringRepresentable.fromEnum(ConfigEnums.Permission::values).optionalFieldOf("mobPermission").forGetter(AffixSetting::mobPermission),
            StringRepresentable.fromEnum(AffixCategory::values).fieldOf("category").forGetter(AffixSetting::category),
            Codec.STRING.optionalFieldOf("prefix").forGetter(AffixSetting::prefix),
            Codec.BOOL.optionalFieldOf("hasSub").forGetter(AffixSetting::hasSub)
    ).apply(instance, AffixSetting::new));

    public static AffixSetting empty() {
        return new AffixSetting(Utils.getLocation("empty"), false, Optional.empty(), Optional.empty(), Optional.empty(), AffixCategory.CC, Optional.empty(), Optional.empty());
    }
}
