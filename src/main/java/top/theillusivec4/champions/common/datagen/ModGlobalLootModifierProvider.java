package top.theillusivec4.champions.common.datagen;

import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.loot.ChampionLootModifier;
import top.theillusivec4.champions.common.loot.LootItemChampionPropertyCondition;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(DataGenerator gen) {
        super(gen, Champions.MODID);
    }

    @Override
    protected void start() {
        this.add("champion_loot_modifier", new ChampionLootModifier(new LootItemCondition[]{
                new LootItemChampionPropertyCondition(LootContext.EntityTarget.THIS, MinMaxBounds.Ints.ANY, null)
        }));
    }
}
