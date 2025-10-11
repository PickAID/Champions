package top.theillusivec4.champions.api;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.util.MinMaxBoundsHelper;

import java.util.List;
import java.util.Optional;

/**
 * Basic affix builder for custom affix behavior
 *
 */
public class AffixSettingBuilder {

	private ResourceLocation type;
	private boolean isEnable;
	private MinMaxBounds.IntBound tier;
	private List<ResourceLocation> mobList;
	private ConfigEnums.Permission mobPermission;
	private AffixCategory category;
	private String prefix;
	private boolean hasSubscriptions;

	public AffixSettingBuilder withDefault() {
		return this.setEnable(true)
				.setCategory(AffixCategory.CC)
				.setTier(MinMaxBoundsHelper.between(1, 100));
	}

	public AffixSettingBuilder setType(ResourceLocation type) {
		this.type = type;
		return this;
	}


	public AffixSettingBuilder setCategory(AffixCategory pCategory) {
		this.category = pCategory;
		return this;
	}


	public AffixSettingBuilder setPrefix(String pPrefix) {
		this.prefix = pPrefix;
		return this;
	}


	public AffixSettingBuilder setHasSub() {
		this.hasSubscriptions = true;
		return this;
	}


	public AffixSettingBuilder setHasSub(boolean pHasSub) {
		this.hasSubscriptions = pHasSub;
		return this;
	}


	public AffixSettingBuilder setEnable(boolean pEnable) {
		this.isEnable = pEnable;
		return this;
	}


	public AffixSettingBuilder setMobList(List<ResourceLocation> mobList) {
		this.mobList = mobList;
		return this;
	}


	public AffixSettingBuilder setTier(MinMaxBounds.IntBound tier) {
		this.tier = tier;
		return this;
	}

	public AffixSettingBuilder setMobPermission(ConfigEnums.Permission mobPermission) {
		this.mobPermission = mobPermission;
		return this;
	}

	public AffixSetting build() {
		return new AffixSetting(
				type,
				isEnable,
				Optional.ofNullable(tier),
				Optional.ofNullable(mobList),
				Optional.ofNullable(mobPermission),
				category,
				Optional.ofNullable(prefix),
				Optional.of(hasSubscriptions)
		);
	}

}
