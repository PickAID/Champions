package top.theillusivec4.champions.common.datagen;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.common.loot.AffixesPredicate;
import top.theillusivec4.champions.common.loot.ChampionLootModifier;
import top.theillusivec4.champions.common.loot.LootItemChampionPropertyCondition;
import top.theillusivec4.champions.common.registry.ModLootModifiers;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
	public ModGlobalLootModifierProvider(DataGenerator gen) {
		super(gen, Champions.MODID);
	}

	@Override
	protected void start() {
		this.add("champion_loot_modifier", ModLootModifiers.CHAMPION_LOOT.get(), new ChampionLootModifier(new ILootCondition[]{
				new LootItemChampionPropertyCondition(LootContext.EntityTarget.THIS, MinMaxBounds.IntBound.ANY, AffixesPredicate.getAny())
		}));
	}
}
