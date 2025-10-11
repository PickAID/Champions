package top.theillusivec4.champions.common.rank;


import net.minecraft.potion.Effect;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.Color;
import top.theillusivec4.champions.api.affix.IAffix;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Rank {

    private final int tier;
    private final String defaultColor;
    private final int numAffixes;
    private final int growthFactor;
    private final int weight;
    private final List<Tuple<Effect, Integer>> effects;
    private final List<IAffix> presetAffixes;

    public Rank() {
        this(0, 0, 0, 0, "#FF000000", new ArrayList<>(), new ArrayList<>());
    }

    public Rank(int tier, int numAffixes, int growthFactor, int weight, String defaultColor,
                List<Tuple<Effect, Integer>> effects, List<IAffix> presetAffixes) {
        this.tier = tier;
        this.numAffixes = numAffixes;
        this.growthFactor = growthFactor;
        this.weight = weight;
        this.defaultColor = defaultColor;
        this.effects = effects;
        this.presetAffixes = presetAffixes;
    }

    public static int getColor(String color) {
        Optional<Color> parsedColor = Optional.ofNullable(Color.parseColor(color));
        return parsedColor.orElse(Color.fromRgb(0)).getValue();
    }

    public int getTier() {
        return tier;
    }

    public Color getDefaultColor() {
        return Color.parseColor(defaultColor);
    }

    public int getNumAffixes() {
        return numAffixes;
    }

    public int getGrowthFactor() {
        return growthFactor;
    }

    public int getWeight() {
        return weight;
    }

    public List<Tuple<Effect, Integer>> getEffects() {
        return effects;
    }

    public List<IAffix> getPresetAffixes() {
        return presetAffixes;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "Rank[]";
    }

}
