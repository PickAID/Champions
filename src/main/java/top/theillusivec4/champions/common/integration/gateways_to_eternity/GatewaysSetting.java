//package top.theillusivec4.champions.common.integration.gateways_to_eternity;
//
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import dev.shadowsoffire.gateways.entity.GatewayEntity;
//import dev.shadowsoffire.gateways.entity.NormalGatewayEntity;
//import net.minecraft.advancements.critereon.MinMaxBounds;
//import net.minecraft.resources.Identifier;
//import net.minecraft.util.StringRepresentable;
//
//import java.util.List;
//import java.util.Optional;
//
//public record GatewaysSetting(Identifier id, MinMaxBounds.Ints tierRange, List<Identifier> affixes,
//                              MinMaxBounds.Ints waveRange,
//                              Gateways gatewayType, Optional<List<Identifier>> entityBlackList,
//                              Optional<Boolean> enable) {
//
//  public static final Codec<GatewaysSetting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//    Identifier.CODEC.fieldOf("setting_id").forGetter(GatewaysSetting::id),
//    MinMaxBounds.Ints.CODEC.fieldOf("tier_range").forGetter(GatewaysSetting::tierRange),
//    Codec.list(Identifier.CODEC).fieldOf("affixes").forGetter(GatewaysSetting::affixes),
//    MinMaxBounds.Ints.CODEC.fieldOf("wave_range").forGetter(GatewaysSetting::waveRange),
//    StringRepresentable.fromEnum(Gateways::values).fieldOf("gateway_type").forGetter(GatewaysSetting::gatewayType),
//    Codec.list(Identifier.CODEC).optionalFieldOf("entityBlackList").forGetter(GatewaysSetting::entityBlackList),
//    Codec.BOOL.optionalFieldOf("enable").forGetter(GatewaysSetting::enable)
//  ).apply(instance, GatewaysSetting::new));
//
//  public static Gateways byEntityType(GatewayEntity gatewayType) {
//    return gatewayType instanceof NormalGatewayEntity ? Gateways.NORMAL : Gateways.ENDLESS;
//  }
//
//}
