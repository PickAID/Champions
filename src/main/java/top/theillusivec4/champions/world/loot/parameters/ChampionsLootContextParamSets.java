package top.theillusivec4.champions.world.loot.parameters;

import com.google.common.collect.BiMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import top.theillusivec4.champions.extralootparam.ExtraLootParamHelper;
import top.theillusivec4.champions.mixin.LootContextParamSetsAccessor;
import top.theillusivec4.champions.util.ChampionsUtil;

import java.util.function.Consumer;

public final class ChampionsLootContextParamSets {
  public static final LootContextParamSet AFFIXED_LOCATION = register(
    "affixed_location",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(ChampionsLootContextParams.AFFIX_LEVEL)
  );
  public static final LootContextParamSet AFFIXED_DAMAGE = register(
    "affixed_damage",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(LootContextParams.DAMAGE_SOURCE)
      .required(ChampionsLootContextParams.AFFIX_LEVEL)
      .optional(LootContextParams.DIRECT_ATTACKING_ENTITY)
      .optional(LootContextParams.ATTACKING_ENTITY)
  );
  public static final LootContextParamSet AFFIXED_ENTITY = register("affixed_entity",
    builder -> builder
      .required(LootContextParams.THIS_ENTITY)
      .required(LootContextParams.ORIGIN)
      .required(ChampionsLootContextParams.AFFIX_LEVEL)
  );

  private ChampionsLootContextParamSets() {
  }

  private static LootContextParamSet register(String name, Consumer<LootContextParamSet.Builder> consumer) {
    BiMap<ResourceLocation, LootContextParamSet> registry = LootContextParamSetsAccessor.getRegistry();
    LootContextParamSet.Builder builder = new LootContextParamSet.Builder();
    consumer.accept(builder);
    ExtraLootParamHelper.addExtraParameters(builder);
    ResourceLocation id = ChampionsUtil.id(name);
    LootContextParamSet set = builder.build();
    LootContextParamSet set1 = registry.put(id, set);
    if (set1 != null) {
      throw new IllegalStateException("Loot table parameter set " + id + " is already registered");
    } else {
      return set;
    }
  }
}
