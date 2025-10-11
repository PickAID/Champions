package top.theillusivec4.champions.api.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.champions.api.AffixSettingBuilder;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.util.Utils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AffixSetting {
	public static final Codec<AffixSetting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			// set affix type for data generation, must match this registry name
			ResourceLocation.CODEC.fieldOf("type").forGetter(AffixSetting::type),
			Codec.BOOL.fieldOf("enable").forGetter(AffixSetting::enabled),
			IntCodec.codec().optionalFieldOf("tier").forGetter(AffixSetting::tier),
			Codec.list(ResourceLocation.CODEC).optionalFieldOf("mobList").forGetter(AffixSetting::mobList),
			IStringSerializable.fromEnum(ConfigEnums.Permission::values, ConfigEnums.Permission::valueOf).optionalFieldOf("mobPermission").forGetter(AffixSetting::mobPermission),
			IStringSerializable.fromEnum(AffixCategory::values, AffixCategory::valueOf).fieldOf("category").forGetter(AffixSetting::category),
			Codec.STRING.optionalFieldOf("prefix").forGetter(AffixSetting::prefix),
			Codec.BOOL.optionalFieldOf("hasSub").forGetter(AffixSetting::hasSub)
	).apply(instance, AffixSetting::new));
	private final ResourceLocation type;
	private final boolean enabled;
	private final Optional<MinMaxBounds.IntBound> tier;
	private final Optional<List<ResourceLocation>> mobList;
	private final Optional<ConfigEnums.Permission> mobPermission;
	private final AffixCategory category;
	private final Optional<String> prefix;
	private final Optional<Boolean> hasSub;

	public AffixSetting(ResourceLocation type, boolean enabled, Optional<MinMaxBounds.IntBound> tier,
	                    Optional<List<ResourceLocation>> mobList, Optional<ConfigEnums.Permission> mobPermission,
	                    AffixCategory category, Optional<String> prefix, Optional<Boolean> hasSub) {
		this.type = type;
		this.enabled = enabled;
		this.tier = tier;
		this.mobList = mobList;
		this.mobPermission = mobPermission;
		this.category = category;
		this.prefix = prefix;
		this.hasSub = hasSub;
	}

	public static AffixSetting empty() {
		return new AffixSetting(Utils.getLocation("empty"), false, Optional.empty(), Optional.empty(), Optional.empty(), AffixCategory.CC, Optional.empty(), Optional.empty());
	}

	public static AffixSettingBuilder builder() {
		return new AffixSettingBuilder();
	}

	public AffixSetting withNewType(ResourceLocation newType) {
		return new AffixSetting(
				newType,
				this.enabled(),
				this.tier(),
				this.mobList(),
				this.mobPermission(),
				this.category(),
				this.prefix(),
				this.hasSub()
		);
	}

	public ResourceLocation type() {
		return type;
	}

	public boolean enabled() {
		return enabled;
	}

	public Optional<MinMaxBounds.IntBound> tier() {
		return tier;
	}

	public Optional<List<ResourceLocation>> mobList() {
		return mobList;
	}

	public Optional<ConfigEnums.Permission> mobPermission() {
		return mobPermission;
	}

	public AffixCategory category() {
		return category;
	}

	public Optional<String> prefix() {
		return prefix;
	}

	public Optional<Boolean> hasSub() {
		return hasSub;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		AffixSetting that = (AffixSetting) obj;
		return Objects.equals(this.type, that.type) &&
				this.enabled == that.enabled &&
				Objects.equals(this.tier, that.tier) &&
				Objects.equals(this.mobList, that.mobList) &&
				Objects.equals(this.mobPermission, that.mobPermission) &&
				Objects.equals(this.category, that.category) &&
				Objects.equals(this.prefix, that.prefix) &&
				Objects.equals(this.hasSub, that.hasSub);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, enabled, tier, mobList, mobPermission, category, prefix, hasSub);
	}

	@Override
	public String toString() {
		return "AffixSetting[" +
				"type=" + type + ", " +
				"enabled=" + enabled + ", " +
				"tier=" + tier + ", " +
				"mobList=" + mobList + ", " +
				"mobPermission=" + mobPermission + ", " +
				"category=" + category + ", " +
				"prefix=" + prefix + ", " +
				"hasSub=" + hasSub + ']';
	}

}
