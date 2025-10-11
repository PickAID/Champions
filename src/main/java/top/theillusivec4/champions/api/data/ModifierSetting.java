package top.theillusivec4.champions.api.data;


import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.Optional;

public final class ModifierSetting {
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
	private final ResourceLocation attributeType;
	private final boolean enable;
	private final Pair<Double, AttributeModifier.Operation> setting;
	private final Optional<ChampionModifierCondition> modifierCondition;

	public ModifierSetting(ResourceLocation attributeType, boolean enable,
	                       Pair<Double, AttributeModifier.Operation> setting,
	                       Optional<ChampionModifierCondition> modifierCondition) {
		this.attributeType = attributeType;
		this.enable = enable;
		this.setting = setting;
		this.modifierCondition = modifierCondition;
	}

	public ResourceLocation attributeType() {
		return attributeType;
	}

	public boolean enable() {
		return enable;
	}

	public Pair<Double, AttributeModifier.Operation> setting() {
		return setting;
	}

	public Optional<ChampionModifierCondition> modifierCondition() {
		return modifierCondition;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		ModifierSetting that = (ModifierSetting) obj;
		return Objects.equals(this.attributeType, that.attributeType) &&
				this.enable == that.enable &&
				Objects.equals(this.setting, that.setting) &&
				Objects.equals(this.modifierCondition, that.modifierCondition);
	}

	@Override
	public int hashCode() {
		return Objects.hash(attributeType, enable, setting, modifierCondition);
	}

	@Override
	public String toString() {
		return "ModifierSetting[" +
				"attributeType=" + attributeType + ", " +
				"enable=" + enable + ", " +
				"setting=" + setting + ", " +
				"modifierCondition=" + modifierCondition + ']';
	}

}
