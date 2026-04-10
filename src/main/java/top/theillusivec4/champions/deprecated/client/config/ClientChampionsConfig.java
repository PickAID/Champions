package top.theillusivec4.champions.deprecated.client.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import top.theillusivec4.champions.ChampionsMod;

public class ClientChampionsConfig {

  private static final String CONFIG_PREFIX = "gui." + ChampionsMod.MOD_ID + ".config.";

  public static final ModConfigSpec CLIENT_SPEC;
  public static final Client CLIENT;

  static {
    final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder()
      .configure(Client::new);
    CLIENT_SPEC = specPair.getRight();
    CLIENT = specPair.getLeft();
  }

  public static class Client {

    public final ModConfigSpec.IntValue hudXOffset;
    public final ModConfigSpec.IntValue hudYOffset;
    public final ModConfigSpec.IntValue hudRange;
    public final ModConfigSpec.BooleanValue enableWailaIntegration;

    public Client(ModConfigSpec.Builder builder) {
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

      builder.pop();
    }
  }

  public static int hudXOffset;
  public static int hudYOffset;
  public static int hudRange;
  public static boolean enableWailaIntegration;

  public static void bake() {
    hudXOffset = CLIENT.hudXOffset.get();
    hudYOffset = CLIENT.hudYOffset.get();
    hudRange = CLIENT.hudRange.get();
    enableWailaIntegration = CLIENT.enableWailaIntegration.get();
  }
}
