package top.theillusivec4.champions.common.integration.gateways_to_eternity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shadowsoffire.gateways.entity.GatewayEntity;
import dev.shadowsoffire.gateways.entity.NormalGatewayEntity;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Optional;

public record GatewaysSetting(ResourceLocation id, MinMaxBounds.Ints tierRange, List<ResourceLocation> affixes,
                              MinMaxBounds.Ints waveRange,
                              Gateways gatewayType, Optional<List<ResourceLocation>> entityBlackList,
                              Optional<Boolean> enable) {

  public static final Codec<GatewaysSetting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    ResourceLocation.CODEC.fieldOf("setting_id").forGetter(GatewaysSetting::id),
    MinMaxBounds.Ints.CODEC.fieldOf("tier_range").forGetter(GatewaysSetting::tierRange),
    Codec.list(ResourceLocation.CODEC).fieldOf("affixes").forGetter(GatewaysSetting::affixes),
    MinMaxBounds.Ints.CODEC.fieldOf("wave_range").forGetter(GatewaysSetting::waveRange),
    StringRepresentable.fromEnum(Gateways::values).fieldOf("gateway_type").forGetter(GatewaysSetting::gatewayType),
    Codec.list(ResourceLocation.CODEC).optionalFieldOf("entityBlackList").forGetter(GatewaysSetting::entityBlackList),
    Codec.BOOL.optionalFieldOf("enable").forGetter(GatewaysSetting::enable)
  ).apply(instance, GatewaysSetting::new));

  public static Gateways byEntityType(GatewayEntity gatewayType) {
    return gatewayType instanceof NormalGatewayEntity ? Gateways.NORMAL : Gateways.ENDLESS;
  }

}
