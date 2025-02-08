package top.theillusivec4.champions.common.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import top.theillusivec4.champions.api.IAffix;
import top.theillusivec4.champions.api.data.ChampionModifierCondition;
import top.theillusivec4.champions.api.data.IntCodec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record AffixesPredicate(Set<ResourceLocation> values, MinMaxBounds.Ints matches,
                               MinMaxBounds.Ints count) {

    private static final AffixesPredicate ANY =
            new AffixesPredicate(Set.of(), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY);
    private static Codec<AffixesPredicate> CODEC_INSTANCE;

    public static Codec<AffixesPredicate> codec() {
        if (CODEC_INSTANCE == null) {
            CODEC_INSTANCE = RecordCodecBuilder.create(instance ->
                    instance.group(
                            ChampionModifierCondition.setOf(ResourceLocation.CODEC).fieldOf("values").forGetter(AffixesPredicate::values),
                            IntCodec.codec().fieldOf("matches").forGetter(AffixesPredicate::matches),
                            IntCodec.codec().fieldOf("count").forGetter(AffixesPredicate::count)
                    ).apply(instance, AffixesPredicate::new));
        }
        return CODEC_INSTANCE;
    }

    public static AffixesPredicate fromJson(JsonElement json) {

        if (json != null && !json.isJsonNull()) {

            if (json.isJsonArray()) {
                JsonArray jsonArray = GsonHelper.convertToJsonArray(json, "affixes");
                Set<ResourceLocation> affixes = new HashSet<>();

                for (JsonElement jsonElement : jsonArray) {

                    if (jsonElement.isJsonPrimitive()) {
                        affixes.add(ResourceLocation.tryParse(jsonElement.getAsString()));
                    }
                }
                return new AffixesPredicate(affixes, MinMaxBounds.Ints.atLeast(1), MinMaxBounds.Ints.ANY);
            } else {
                JsonObject jsonObject = json.getAsJsonObject();
                Set<ResourceLocation> affixes = new HashSet<>();

                if (jsonObject.has("values")) {
                    JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, "values");

                    for (JsonElement jsonElement : jsonArray) {

                        if (jsonElement.isJsonPrimitive()) {
                            affixes.add(ResourceLocation.tryParse(jsonElement.getAsString()));
                        }
                    }
                }
                MinMaxBounds.Ints matches = MinMaxBounds.Ints.atLeast(1);

                if (jsonObject.has("matches")) {
                    matches = MinMaxBounds.Ints.fromJson(jsonObject.get("matches"));
                }
                MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;

                if (jsonObject.has("count")) {
                    count = MinMaxBounds.Ints.fromJson(jsonObject.get("count"));
                }
                return new AffixesPredicate(affixes, matches, count);
            }
        }
        return ANY;
    }

    public boolean matches(List<IAffix> input) {

        if (this.values.isEmpty()) {
            return this.count.matches(input.size());
        } else {
            Set<ResourceLocation> affixes = input.stream().map(IAffix::getIdentifier).collect(Collectors.toSet());
            int found = 0;

            for (var affix : this.values) {

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

            for (var value : this.values) {
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
}
