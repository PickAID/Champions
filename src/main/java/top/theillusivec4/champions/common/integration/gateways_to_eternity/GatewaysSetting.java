package top.theillusivec4.champions.common.integration.gateways_to_eternity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.champions.api.data.IntCodec;

import java.util.List;
import java.util.Optional;

public record GatewaysSetting(ResourceLocation id, MinMaxBounds.Ints tier, List<ResourceLocation> affixes,
                              MinMaxBounds.Ints waveRange,
                              Optional<List<ResourceLocation>> entityBlackList,
                              Optional<Boolean> enable) {

	public static final Codec<GatewaysSetting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("setting_id").forGetter(GatewaysSetting::id),
			IntCodec.codec().fieldOf("tier_range").forGetter(GatewaysSetting::tier),
			Codec.list(ResourceLocation.CODEC).fieldOf("affixes").forGetter(GatewaysSetting::affixes),
			IntCodec.codec().fieldOf("wave_range").forGetter(GatewaysSetting::waveRange),
			Codec.list(ResourceLocation.CODEC).optionalFieldOf("entityBlackList").forGetter(GatewaysSetting::entityBlackList),
			Codec.BOOL.optionalFieldOf("enable").forGetter(GatewaysSetting::enable)
	).apply(instance, GatewaysSetting::new));

}
