package top.theillusivec4.champions.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.config.ConfigEnums;

import java.util.List;
import java.util.Optional;

public record AffixSetting(ResourceLocation type, boolean enabled, Optional<MinMaxBounds.Ints> tier,
                           Optional<List<ResourceLocation>> mobList, Optional<ConfigEnums.Permission> mobPermission,
                           AffixCategory category, Optional<String> prefix, Optional<Boolean> hasSub) {
    public static final Codec<MinMaxBounds.Ints> INTS_CODEC = Codec.PASSTHROUGH.xmap(
            // 从 JsonElement 转换到 MinMaxBounds.Ints
            (dynamic) -> MinMaxBounds.Ints.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue()),
            // 从 MinMaxBounds.Ints 转换到 JsonElement
            (bounds) -> new Dynamic<>(JsonOps.INSTANCE, bounds.serializeToJson())
    );

    public static final Codec<AffixSetting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            // set affix type for data generation, must match this registry name
            ResourceLocation.CODEC.fieldOf("type").forGetter(AffixSetting::type),
            Codec.BOOL.fieldOf("enable").forGetter(AffixSetting::enabled),
            INTS_CODEC.optionalFieldOf("tier").forGetter(AffixSetting::tier),
            Codec.list(ResourceLocation.CODEC).optionalFieldOf("mobList").forGetter(AffixSetting::mobList),
            StringRepresentable.fromEnum(ConfigEnums.Permission::values).optionalFieldOf("mobPermission").forGetter(AffixSetting::mobPermission),
            StringRepresentable.fromEnum(AffixCategory::values).fieldOf("category").forGetter(AffixSetting::category),
            Codec.STRING.optionalFieldOf("prefix").forGetter(AffixSetting::prefix),
            Codec.BOOL.optionalFieldOf("hasSub").forGetter(AffixSetting::hasSub)
    ).apply(instance, AffixSetting::new));

    public static AffixSetting empty() {
        return new AffixSetting(Champions.getLocation("empty"), false, Optional.empty(), Optional.empty(), Optional.empty(), AffixCategory.CC, Optional.empty(), Optional.empty());
    }
}
