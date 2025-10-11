package top.theillusivec4.champions.common.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.api.data.ChampionModifierCondition;
import top.theillusivec4.champions.api.data.IntCodec;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class AffixesPredicate {

	public static Codec<AffixesPredicate> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(ChampionModifierCondition.setOf(ResourceLocation.CODEC).fieldOf("values").forGetter(AffixesPredicate::values),
					IntCodec.codec().fieldOf("matches").forGetter(AffixesPredicate::matches),
					IntCodec.codec().fieldOf("count").forGetter(AffixesPredicate::count)
			).apply(instance, AffixesPredicate::new));
	private final Set<ResourceLocation> values;
	private final MinMaxBounds.IntBound matches;
	private final MinMaxBounds.IntBound count;

	public AffixesPredicate(Set<ResourceLocation> values, MinMaxBounds.IntBound matches,
	                        MinMaxBounds.IntBound count) {
		this.values = values;
		this.matches = matches;
		this.count = count;
	}

	public static AffixesPredicate fromJson(JsonElement json) {

		if (json != null && !json.isJsonNull()) {

			if (json.isJsonArray()) {
				JsonArray jsonArray = JSONUtils.convertToJsonArray(json, "affixes");
				Set<ResourceLocation> affixes = new HashSet<>();

				for (JsonElement jsonElement : jsonArray) {

					if (jsonElement.isJsonPrimitive()) {
						affixes.add(ResourceLocation.tryParse(jsonElement.getAsString()));
					}
				}
				return new AffixesPredicate(affixes, MinMaxBounds.IntBound.atLeast(1), MinMaxBounds.IntBound.ANY);
			} else {
				JsonObject jsonObject = json.getAsJsonObject();
				Set<ResourceLocation> affixes = new HashSet<>();

				if (jsonObject.has("values")) {
					JsonArray jsonArray = JSONUtils.getAsJsonArray(jsonObject, "values");

					for (JsonElement jsonElement : jsonArray) {

						if (jsonElement.isJsonPrimitive()) {
							affixes.add(ResourceLocation.tryParse(jsonElement.getAsString()));
						}
					}
				}
				MinMaxBounds.IntBound matches = MinMaxBounds.IntBound.atLeast(1);

				if (jsonObject.has("matches")) {
					matches = MinMaxBounds.IntBound.fromJson(jsonObject.get("matches"));
				}
				MinMaxBounds.IntBound count = MinMaxBounds.IntBound.ANY;

				if (jsonObject.has("count")) {
					count = MinMaxBounds.IntBound.fromJson(jsonObject.get("count"));
				}
				return new AffixesPredicate(affixes, matches, count);
			}
		}
		return getAny();
	}

	public static Codec<AffixesPredicate> codec() {
		if (CODEC == null) {
			CODEC = RecordCodecBuilder.create(instance ->
					instance.group(
							ChampionModifierCondition.setOf(ResourceLocation.CODEC).fieldOf("values").forGetter(AffixesPredicate::values),
							IntCodec.codec().fieldOf("matches").forGetter(AffixesPredicate::matches),
							IntCodec.codec().fieldOf("count").forGetter(AffixesPredicate::count)
					).apply(instance, AffixesPredicate::new));
		}
		return CODEC;
	}

	public static AffixesPredicate getAny() {
		return new AffixesPredicate(new HashSet<>(), MinMaxBounds.IntBound.ANY, MinMaxBounds.IntBound.ANY);
	}

	public boolean matches(List<IAffix> input) {

		if (this.values.isEmpty()) {
			return this.count.matches(input.size());
		} else {
			Set<ResourceLocation> affixes = input.stream().map(IAffix::getIdentifier).collect(Collectors.toSet());
			int found = 0;

			for (ResourceLocation affix : this.values) {

				if (affixes.contains(affix)) {
					found++;
				}
			}
			return this.matches.matches(found) && this.count.matches(input.size());
		}
	}

	public JsonElement serializeToJson() {
		if (this.values.isEmpty() && this.count.isAny() && this.matches.isAny()) {
			return JsonNull.INSTANCE;
		} else {
			JsonObject jsonObject = new JsonObject();
			JsonArray jsonArray = new JsonArray();

			for (ResourceLocation value : this.values) {
				jsonArray.add(value.toString());
			}
			Integer min = this.count.getMin();
			Integer max = this.count.getMax();

			if (min != null && min == 1 && max == null) {
				return jsonArray;
			}
			jsonObject.add("values", jsonArray);
			jsonObject.add("matches", this.matches.serializeToJson());
			jsonObject.add("count", this.count.serializeToJson());
			return jsonObject;
		}
	}

	public Set<ResourceLocation> values() {
		return values;
	}

	public MinMaxBounds.IntBound matches() {
		return matches;
	}

	public MinMaxBounds.IntBound count() {
		return count;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		AffixesPredicate that = (AffixesPredicate) obj;
		return Objects.equals(this.values, that.values) &&
				Objects.equals(this.matches, that.matches) &&
				Objects.equals(this.count, that.count);
	}

	@Override
	public int hashCode() {
		return Objects.hash(values, matches, count);
	}

	@Override
	public String toString() {
		return "AffixesPredicate[" +
				"values=" + values + ", " +
				"matches=" + matches + ", " +
				"count=" + count + ']';
	}

}
