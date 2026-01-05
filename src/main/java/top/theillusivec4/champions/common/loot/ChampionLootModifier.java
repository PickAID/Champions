package top.theillusivec4.champions.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import net.neoforged.neoforge.common.util.FakePlayer;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capabilities.ChampionAttachment;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.config.ConfigLoot;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.registries.ModLootTables;

import java.util.List;

@Deprecated
public class ChampionLootModifier extends LootModifier {
  public static final MapCodec<ChampionLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, ChampionLootModifier::new));
  private static final ThreadLocal<Boolean> IS_PROCESSING = ThreadLocal.withInitial(() -> false);

  public ChampionLootModifier(LootItemCondition[] conditions) {
    super(conditions);
  }

  @Override
  public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
    if (IS_PROCESSING.get()) {
      return generatedLoot;
    }

    IS_PROCESSING.set(true);
    try {
      Entity entity = context.getOptionalParameter(LootContextParams.THIS_ENTITY);

      if (entity == null) {
        return generatedLoot;
      }
      DamageSource damageSource = context.getOptionalParameter(LootContextParams.DAMAGE_SOURCE);

      if (damageSource == null) {
        return generatedLoot;
      }

//      var server = entity.level().getServer();

      if (entity.level() instanceof ServerLevel level && !level.getGameRules().get(GameRules.MOB_DROPS) ||
        (!ChampionsConfig.fakeLoot && damageSource.getDirectEntity() instanceof FakePlayer)) {
        return generatedLoot;
      }

      ChampionAttachment.getAttachment(entity).ifPresent(champion -> {
        IChampion.Server serverChampion = champion.getServer();
        ServerLevel serverWorld = (ServerLevel) entity.level();

        if (ChampionsConfig.lootSource != ConfigEnums.LootSource.CONFIG) {
          LootTable lootTable = serverWorld.getServer().reloadableRegistries()
            .getLootTable(ModLootTables.CHAMPION_LOOT);
          LootParams.Builder lootParams$builder = new LootParams.Builder(serverWorld)
            .withParameter(LootContextParams.THIS_ENTITY, entity)
            .withParameter(LootContextParams.ORIGIN, entity.position())
            .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
            .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
            .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity());

          if (entity instanceof LivingEntity livingEntity) {
            LivingEntity attackingEntity = livingEntity.getKillCredit();

            // using player luck to provider lootParams
            if (attackingEntity instanceof Player player) {
              lootParams$builder = lootParams$builder
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
                .withLuck(player.getLuck());
            }
          }
          LootParams lootParams = lootParams$builder.create(LootContextParamSets.ENTITY);
          lootTable.getRandomItems(lootParams, generatedLoot::add);
        }

        if (ChampionsConfig.lootSource != ConfigEnums.LootSource.LOOT_TABLE) {
          List<ItemStack> loot = ConfigLoot.getLootDrops(serverChampion.getRank().map(Rank::getTier).orElse(0));

          if (!loot.isEmpty()) {
            generatedLoot.addAll(loot);
          }
        }
      });
      return generatedLoot;
    } finally {
      IS_PROCESSING.set(false);
    }
  }

  @Override
  public MapCodec<? extends IGlobalLootModifier> codec() {
    return CODEC;
  }

}
