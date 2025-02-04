package top.theillusivec4.champions.client.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;
import top.theillusivec4.champions.Champions;

public class ClientChampionsConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;
    private static final String CONFIG_PREFIX = "gui." + Champions.MODID + ".config.";
    public static int hudXOffset;
    public static int hudYOffset;
    public static int hudRange;
    public static int lineCount;
    public static boolean enableWailaIntegration;
    public static int jadeStarSpacing;
    public static int jadeStarBottomPadding;

    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
                .configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static void bake() {
        hudXOffset = CLIENT.hudXOffset.get();
        hudYOffset = CLIENT.hudYOffset.get();
        hudRange = CLIENT.hudRange.get();
        jadeStarSpacing = CLIENT.jadeStarSpacing.get();
        jadeStarBottomPadding = CLIENT.jadeStarBottomPadding.get();
        enableWailaIntegration = CLIENT.enableWailaIntegration.get();
        lineCount = CLIENT.lineCount.get();
    }

    public static class Client {

        public final IntValue hudXOffset;
        public final IntValue hudYOffset;
        public final IntValue hudRange;
        public final ForgeConfigSpec.BooleanValue enableWailaIntegration;
        public final ForgeConfigSpec.IntValue lineCount;
        public final ForgeConfigSpec.IntValue jadeStarSpacing;
        public final ForgeConfigSpec.IntValue jadeStarBottomPadding;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("hud");

            hudXOffset = builder.comment("The x-offset for the champion HUD")
                    .translation(CONFIG_PREFIX + "hudXOffset").defineInRange("hudXOffset", 0, -1000, 1000);

            hudYOffset = builder.comment("The y-offset for the champion HUD")
                    .translation(CONFIG_PREFIX + "hudYOffset").defineInRange("hudYOffset", 0, -1000, 1000);

            hudRange = builder.comment("The distance, in blocks, from which the champion HUD can be seen")
                    .translation(CONFIG_PREFIX + "hudRange").defineInRange("hudRange", 50, 0, 1000);

            enableWailaIntegration =
                    builder.comment("Set to true to move the WAILA overlay underneath the champion HUD")
                            .translation(CONFIG_PREFIX + "enableWailaIntegration")
                            .define("enableWailaIntegration", true);
            lineCount = builder.comment("The affix line count of the jade compact").defineInRange("lineCount", 5, 1, 20);
            jadeStarSpacing = builder.comment("The Jade Star spacing, when rendering star.").defineInRange("jadeStarSpacing", 2, 0, 25);
            jadeStarBottomPadding = builder.comment("The Jade Star bottom padding, when rendering star.").defineInRange("jadeStarBottomPadding", 3, 0, 100);
            builder.pop();
        }
    }
}
