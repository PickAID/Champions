package top.theillusivec4.champions.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class DataEventListener {
  public static void register(IEventBus modEventBus) {
    modEventBus.register(new DataEventListener());
  }

  private DataEventListener() {
  }

  @SubscribeEvent
  public void onGatherDataServer(GatherDataEvent.Server event) {
    event.createDatapackRegistryObjects(ChampionsRegistries.BUILDER);
  }

  @SubscribeEvent
  public void onGatherDataClient(GatherDataEvent.Client event) {
    DataGenerator dataGenerator = event.getGenerator();
    PackOutput output = dataGenerator.getPackOutput();

    event.createDatapackRegistryObjects(ChampionsRegistries.BUILDER);
    event.addProvider(LanguageProviders.zhCn(output));
  }
}
