package top.theillusivec4.champions.common.util;

import net.minecraft.advancements.criterion.MinMaxBounds;

public class MinMaxBoundsHelper {

	public static MinMaxBounds.IntBound atMost(int max) {
		return new MinMaxBounds.IntBound(null, max);
	}
	public static MinMaxBounds.IntBound between(int pMin, int pMax) {
		return new MinMaxBounds.IntBound(pMin, pMax);
	}
}
