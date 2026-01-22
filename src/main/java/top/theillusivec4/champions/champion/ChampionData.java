package top.theillusivec4.champions.champion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

import java.util.Optional;

/**
 * 一个冠军数据快照对象。
 * 将冠军数据组织进一个配置对象应该对数据转移有所帮助，事实上也确实如此。
 *
 * @param prefixName
 * @param affixes
 * @param level
 * @param color
 * @param boss
 */
public record ChampionData(
		Optional<Component> prefixName,
		Optional<Affixes> affixes,
		Optional<Integer> level,
		Optional<Integer> color,
		Optional<Boolean> boss
) {
	public static final Codec<ChampionData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ComponentSerialization.CODEC.optionalFieldOf("prefix_name").forGetter(ChampionData::prefixName),
			Affixes.CODEC.optionalFieldOf("affixes").forGetter(ChampionData::affixes),
			Codec.INT.optionalFieldOf("level").forGetter(ChampionData::level),
			Codec.INT.optionalFieldOf("color").forGetter(ChampionData::color),
			Codec.BOOL.optionalFieldOf("boss").forGetter(ChampionData::boss)
	).apply(instance, ChampionData::new));

}
