package top.theillusivec4.champions.server.champion.config;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.loot.LootContext;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.world.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Optional;

public record ChampionConfigSelectorHolder(Identifier id, ChampionConfigSelector value) {

}
