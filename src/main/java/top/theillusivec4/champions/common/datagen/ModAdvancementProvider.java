package top.theillusivec4.champions.common.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.champions.common.loot.AffixesPredicate;
import top.theillusivec4.champions.common.loot.ChampionPropertyCondition;
import top.theillusivec4.champions.common.util.Utils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {

  /**
   * Constructs an advancement provider using the generators to write the
   * advancements to a file.
   *
   * @param output             the target directory of the data generator
   * @param registries         a future of a lookup for registries and their objects
   * @param existingFileHelper a helper used to find whether a file exists
   * @param subProviders       the generators used to create the advancements
   */
  public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper, List<AdvancementGenerator> subProviders) {
    super(output, registries, existingFileHelper, subProviders);
  }

  public static final class Generator implements AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
      // Generate your advancements here.
      Advancement killAChampion = Advancement.Builder.advancement()
        .parent(AdvancementSubProvider.createPlaceholder("minecraft:adventure/kill_a_mob"))
        .display(
          new ItemStack(Items.IRON_SWORD),
          Component.translatable("advancements.champions.kill_a_champion.title"),
          Component.translatable("advancements.champions.kill_a_champion.description"),
          null,
          AdvancementType.CHALLENGE, true, true, true
        )
        .addCriterion("kill_a_champion", KilledTrigger.TriggerInstance.playerKilledEntity(new EntityPredicate.Builder()
          .subPredicate(new ChampionPropertyCondition(LootContext.EntityTarget.THIS,
            Optional.of(MinMaxBounds.Ints.ANY),
            Optional.of(new AffixesPredicate(Utils.getLocationSet("hasty", "dampening", "enkindling"), MinMaxBounds.Ints.atLeast(1), MinMaxBounds.Ints.atLeast(1))))
          )))
        .requirements(AdvancementRequirements.allOf(List.of("kill_a_champion")))
        .save(saver, Utils.getLocation("kill_a_champion"), existingFileHelper).value();
    }
  }
}
