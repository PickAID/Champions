package top.theillusivec4.champions.common.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class DataGenerationEventListener {
  public static void register(IEventBus modEventBus) {
    modEventBus.register(new DataGenerationEventListener());
  }

  private DataGenerationEventListener() {
  }

  @SubscribeEvent
  public void onGatherDataServer(GatherDataEvent.Server event) {
    event.createDatapackRegistryObjects(Registries.BUILDER);
  }

  @SubscribeEvent
  public void onGatherDataClient(GatherDataEvent.Client event) {
    DataGenerator dataGenerator = event.getGenerator();
    PackOutput output = dataGenerator.getPackOutput();

    event.createDatapackRegistryObjects(Registries.BUILDER);
    event.addProvider(LanguageProviders.zhCn(output));
  }
}
