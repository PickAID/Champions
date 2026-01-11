package top.theillusivec4.champions.data.champion.config.selector;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import top.theillusivec4.champions.champion.rank.Rank;
import top.theillusivec4.champions.champion.rank.Ranks;
import top.theillusivec4.champions.registry.Registries;
import top.theillusivec4.champions.server.champion.config.ChampionConfig;
import top.theillusivec4.champions.server.champion.config.ChampionConfigSelector;
import top.theillusivec4.champions.server.champion.config.ChampionConfigSelectorHolder;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public abstract class ChampionConfigSelectorProvider implements DataProvider {
  private final PackOutput.PathProvider pathProvider;
  private final CompletableFuture<HolderLookup.Provider> registries;
  private final List<WithConditions<ChampionConfigSelectorHolder>> selectors = new ArrayList<>();

  public ChampionConfigSelectorProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    this.registries = registries;
    this.pathProvider = output.createRegistryElementsPathProvider(Registries.CHAMPION_CONFIG_SELECTOR);
  }

  protected abstract void addConfigSelectors(HolderLookup.Provider registries);

  @Override
  public CompletableFuture<?> run(CachedOutput cache) {
    return this.registries.thenCompose(lookup -> {
      Set<Identifier> allSelectors = new HashSet<>();
      List<CompletableFuture<?>> tasks = new ArrayList<>();
      Consumer<WithConditions<ChampionConfigSelectorHolder>> consumer = withConditionsHolder -> {
        ChampionConfigSelectorHolder holder = withConditionsHolder.carrier();
        List<ICondition> conditions = withConditionsHolder.conditions();
        ChampionConfigSelector selector = holder.value();
        if (!allSelectors.add(holder.id())) {
          throw new IllegalStateException("Duplicate champion config selector " + holder.id());
        } else {
          Path path = this.pathProvider.json(holder.id());

          tasks.add(DataProvider.saveStable(cache, lookup, ChampionConfigSelector.WITH_CONDITIONS_CODEC, Optional.of(new WithConditions<>(conditions, selector)), path));
        }
      };

      this.addConfigSelectors(lookup);
      for (WithConditions<ChampionConfigSelectorHolder> selector : this.selectors) {
        consumer.accept(selector);
      }

      return CompletableFuture.anyOf(tasks.toArray(CompletableFuture[]::new));
    });
  }

  @Override
  public String getName() {
    return "ChampionConfigSelector";
  }

  protected void add(Identifier id, ChampionConfigSelector.Builder selector) {
    this.add(id, selector, List.of());
  }

  protected void add(Identifier id, ChampionConfigSelector.Builder selector, List<ICondition> conditions) {
    this.selectors.add(new WithConditions<>(conditions, new ChampionConfigSelectorHolder(id, selector.build(id))));
  }

  public static final class Internal extends ChampionConfigSelectorProvider {

    public Internal(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
      super(output, registries);
    }

    @Override
    protected void addConfigSelectors(HolderLookup.Provider registries) {
      Holder<Rank> rank = registries.holderOrThrow(Ranks.COMMON);
      this.add(
        EntityType.getKey(EntityType.ZOMBIE),
        ChampionConfigSelector.builder()
          .add(
            ChampionConfig.builder()
              .setRank(rank)
          )
      );
    }
  }
}
