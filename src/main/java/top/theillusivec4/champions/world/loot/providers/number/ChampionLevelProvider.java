package top.theillusivec4.champions.world.loot.providers.number;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import top.theillusivec4.champions.champion.value.based.lootcontext.LevelBasedValue;
import top.theillusivec4.champions.world.loot.parameters.LootContextParams;

import java.util.Set;

public record ChampionLevelProvider(LevelBasedValue amount) implements NumberProvider {
	public static final MapCodec<ChampionLevelProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			LevelBasedValue.CODEC.fieldOf("amount").forGetter(ChampionLevelProvider::amount)
	).apply(instance, ChampionLevelProvider::new));

	@Override
	public float getFloat(LootContext context) {
		int level = context.getParameter(LootContextParams.CHAMPION_LEVEL);
		return this.amount.calculate(level);
	}

	@Override
	public MapCodec<? extends NumberProvider> codec() {
		return MAP_CODEC;
	}

	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return Set.of(LootContextParams.CHAMPION_LEVEL);
	}
}
