package top.theillusivec4.champions.server.champion.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.world.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Optional;

public record ChampionConfigSelector(List<ChampionConfig.Entry> entries) {
  public static final Codec<ChampionConfigSelector> CODEC = RecordCodecBuilder.create(instance -> instance.group(ChampionConfig.Entry.CODEC.listOf().fieldOf("entries").forGetter(ChampionConfigSelector::entries)).apply(instance, ChampionConfigSelector::new));

  public record Holder(ResourceKey<ChampionConfigSelector> id, ChampionConfigSelector value) {
    public Optional<ChampionConfig> select(ServerLevel serverLevel, Entity entity, @Nullable EntitySpawnReason entitySpawnReason) {
      LootContext lootContext = LootContextParamSets.spawn(serverLevel, entity, entitySpawnReason, this.id.identifier());
      RandomSource randomSource = lootContext.getRandom();
      List<ChampionConfig.Entry> entries = this.value.entries.stream().filter(entry -> entry.matches(lootContext)).toList();
      final int totalWeight = entries.stream().mapToInt(ChampionConfig.Entry::weight).sum();
      final int selectWeight = randomSource.nextInt(totalWeight);
      int currentWeight = 0;
      for (ChampionConfig.Entry entry : entries) {
        currentWeight += entry.weight();
        if (selectWeight < currentWeight) {
          return Optional.of(entry.config());
        }
      }

      return Optional.empty();
    }

  }
}
