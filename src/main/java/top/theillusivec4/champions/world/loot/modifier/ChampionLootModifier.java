package top.theillusivec4.champions.world.loot.modifier;

import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

/**
 * 每个实体类型应该有自己专属的冠军战利品表
 */
public class ChampionLootModifier extends LootModifier {
  public static final ChampionLootModifier INSTANCE = new ChampionLootModifier();
  public static final MapCodec<ChampionLootModifier> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final String ENTITIES = "entities/";
  private static final String CHAMPIONS = "champions/";

  protected ChampionLootModifier() {
    LootItemCondition[] lootItemConditions = {};
    super(lootItemConditions);
  }

  @Override
  public MapCodec<? extends IGlobalLootModifier> codec() {
    return MAP_CODEC;
  }

  @Override
  protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
    Identifier id = context.getQueriedLootTableId();
    if (!id.equals(LootTableIdCondition.UNKNOWN_LOOT_TABLE) && id.getPath().startsWith(ENTITIES)) {
      Identifier lootTableId = Identifier.fromNamespaceAndPath(id.getNamespace(), CHAMPIONS + id.getPath().substring(ENTITIES.length()));
      ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, lootTableId);
      LootTable lootTable = context.getLevel().getServer().getServerResources().managers().fullRegistries().getLootTable(key);
      // 使用该弃用方法是有意为之。
      lootTable.getRandomItemsRaw(context, generatedLoot::add);
    }

    return generatedLoot;
  }
}
