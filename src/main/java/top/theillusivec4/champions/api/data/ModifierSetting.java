package top.theillusivec4.champions.api.data;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Optional;

public record ModifierSetting(ResourceLocation attributeType, boolean enable,
                              Pair<Double, AttributeModifier.Operation> setting,
                              Optional<ChampionModifierCondition> modifierCondition) {
    public static final Codec<AttributeModifier.Operation> OPERATION_CODEC = Codec.STRING.xmap(
            AttributeModifier.Operation::valueOf,  // 将 int 转换为 Operation
            AttributeModifier.Operation::name     // 将 Operation 转换为 int
    );

    public static final MapCodec<ModifierSetting> MAP_CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("attributeType").forGetter(ModifierSetting::attributeType),
                    Codec.BOOL.fieldOf("enable").forGetter(ModifierSetting::enable),
                    RecordCodecBuilder.<Pair<Double, AttributeModifier.Operation>>create(inst ->
                            inst.group(
                                    Codec.DOUBLE.fieldOf("value").forGetter(Pair::getFirst),
                                    OPERATION_CODEC.fieldOf("operation").forGetter(Pair::getSecond)
                            ).apply(inst, Pair::new)
                    ).fieldOf("modifier").forGetter(ModifierSetting::setting),
                    ChampionModifierCondition.MAP_CODEC.codec().optionalFieldOf("conditions").forGetter(ModifierSetting::modifierCondition)
            ).apply(instance, ModifierSetting::new));
}
