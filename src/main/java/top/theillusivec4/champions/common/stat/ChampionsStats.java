package top.theillusivec4.champions.common.stat;

import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import top.theillusivec4.champions.common.util.Utils;

public class ChampionsStats {

    public static ResourceLocation CHAMPION_MOBS_KILLED;

    public static void setup() {
        CHAMPION_MOBS_KILLED = makeCustomStat("champion_mobs_killed", IStatFormatter.DEFAULT);
    }

    private static ResourceLocation makeCustomStat(String key, IStatFormatter formatter) {
        ResourceLocation resourcelocation = Utils.getLocation(key);
        Registry.register(Registry.CUSTOM_STAT, resourcelocation.toString(), resourcelocation);
        Stats.CUSTOM.get(resourcelocation, formatter);
        return resourcelocation;
    }
}
