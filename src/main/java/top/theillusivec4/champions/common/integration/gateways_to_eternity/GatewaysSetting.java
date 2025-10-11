package top.theillusivec4.champions.common.integration.gateways_to_eternity;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.champions.api.data.IntCodec;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class GatewaysSetting {

	public static final Codec<GatewaysSetting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("setting_id").forGetter(GatewaysSetting::id),
			IntCodec.codec().fieldOf("tier_range").forGetter(GatewaysSetting::tier),
			Codec.list(ResourceLocation.CODEC).fieldOf("affixes").forGetter(GatewaysSetting::affixes),
			IntCodec.codec().fieldOf("wave_range").forGetter(GatewaysSetting::waveRange),
			Codec.list(ResourceLocation.CODEC).optionalFieldOf("entityBlackList").forGetter(GatewaysSetting::entityBlackList),
			Codec.BOOL.optionalFieldOf("enable").forGetter(GatewaysSetting::enable)
	).apply(instance, GatewaysSetting::new));
	private final ResourceLocation id;
	private final MinMaxBounds.IntBound tier;
	private final List<ResourceLocation> affixes;
	private final MinMaxBounds.IntBound waveRange;
	private final Optional<List<ResourceLocation>> entityBlackList;
	private final Optional<Boolean> enable;

	public GatewaysSetting(ResourceLocation id, MinMaxBounds.IntBound tier, List<ResourceLocation> affixes,
	                       MinMaxBounds.IntBound waveRange,
	                       Optional<List<ResourceLocation>> entityBlackList,
	                       Optional<Boolean> enable) {
		this.id = id;
		this.tier = tier;
		this.affixes = affixes;
		this.waveRange = waveRange;
		this.entityBlackList = entityBlackList;
		this.enable = enable;
	}

	public ResourceLocation id() {
		return id;
	}

	public MinMaxBounds.IntBound tier() {
		return tier;
	}

	public List<ResourceLocation> affixes() {
		return affixes;
	}

	public MinMaxBounds.IntBound waveRange() {
		return waveRange;
	}

	public Optional<List<ResourceLocation>> entityBlackList() {
		return entityBlackList;
	}

	public Optional<Boolean> enable() {
		return enable;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		GatewaysSetting that = (GatewaysSetting) obj;
		return Objects.equals(this.id, that.id) &&
				Objects.equals(this.tier, that.tier) &&
				Objects.equals(this.affixes, that.affixes) &&
				Objects.equals(this.waveRange, that.waveRange) &&
				Objects.equals(this.entityBlackList, that.entityBlackList) &&
				Objects.equals(this.enable, that.enable);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tier, affixes, waveRange, entityBlackList, enable);
	}

	@Override
	public String toString() {
		return "GatewaysSetting[" +
				"id=" + id + ", " +
				"tier=" + tier + ", " +
				"affixes=" + affixes + ", " +
				"waveRange=" + waveRange + ", " +
				"entityBlackList=" + entityBlackList + ", " +
				"enable=" + enable + ']';
	}


}
