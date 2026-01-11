package top.theillusivec4.champions.server.champion.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import top.theillusivec4.champions.champion.ChampionConfig;
import top.theillusivec4.champions.world.loot.parameters.LootContextParamSets;

import java.util.Optional;

public record ChampionConfigEntry(Optional<LootItemCondition> requirements, ChampionConfig config, int weight) {
  public static final Codec<ChampionConfigEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
    LootItemCondition.DIRECT_CODEC.validate(Validatable.validatorForContext(LootContextParamSets.SPAWN)).optionalFieldOf("requirements").forGetter(ChampionConfigEntry::requirements),
    ChampionConfig.CODEC.fieldOf("config").forGetter(ChampionConfigEntry::config),
    Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").forGetter(ChampionConfigEntry::weight)
  ).apply(instance, ChampionConfigEntry::new));

  public boolean matches(LootContext lootContext) {
    return this.requirements.isEmpty() || this.requirements.get().test(lootContext);
  }
}
