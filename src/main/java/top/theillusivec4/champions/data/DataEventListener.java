package top.theillusivec4.champions.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import top.theillusivec4.champions.data.champion.config.selector.ChampionConfigSelectorProvider;
import top.theillusivec4.champions.data.lang.LanguageHelper;
import top.theillusivec4.champions.data.registry.ChampionsRegistries;
import top.theillusivec4.champions.data.tag.RankTagsProvider;

import java.util.concurrent.CompletableFuture;

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
    CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
    event.addProvider(new RankTagsProvider(output, registries));
    event.addProvider(LanguageHelper.zhCn(output));
    event.addProvider(new ChampionConfigSelectorProvider.Internal(output, registries));
  }
}
