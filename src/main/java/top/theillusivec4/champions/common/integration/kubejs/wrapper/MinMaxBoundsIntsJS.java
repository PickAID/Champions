package top.theillusivec4.champions.common.integration.kubejs.wrapper;

import com.google.gson.JsonElement;
import net.minecraft.advancements.critereon.MinMaxBounds;
import org.jetbrains.annotations.Nullable;

import javax.json.JsonArray;

public class MinMaxBoundsIntsJS {
	public static MinMaxBounds.Ints of(@Nullable Object object){
		if(object == null){
			return MinMaxBounds.Ints.ANY;
		}else if(object instanceof MinMaxBounds.Ints ints){
			return ints;
		}else if(object instanceof JsonArray jsonArray){
			if(jsonArray.size() >= 2){
				int min = jsonArray.getInt(0);
				int max = jsonArray.getInt(1);
				return MinMaxBounds.Ints.between(min, max);
			}else if(jsonArray.size() == 1){
				return MinMaxBounds.Ints.exactly(jsonArray.getInt(0));
			}else {
				return MinMaxBounds.Ints.ANY; // JsonArray size == 0 应该允许这种情况吗?
			}
		}else if(object instanceof JsonElement jsonElement){
			return MinMaxBounds.Ints.fromJson(jsonElement);
		}
		
		return MinMaxBounds.Ints.ANY;
	}
}
