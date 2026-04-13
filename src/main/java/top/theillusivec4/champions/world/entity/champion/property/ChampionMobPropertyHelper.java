package top.theillusivec4.champions.world.entity.champion.property;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.attachment.ChampionsAttachments;
import top.theillusivec4.champions.core.component.ChampionsDataComponents;
import top.theillusivec4.champions.core.particles.ChampionsParticleTypes;
import top.theillusivec4.champions.core.registries.ChampionsDataMaps;
import top.theillusivec4.champions.core.registries.ChampionsRegistries;
import top.theillusivec4.champions.server.ChampionsServerConfig;
import top.theillusivec4.champions.server.champion.ChampionsServerBossEvent;
import top.theillusivec4.champions.util.ChampionsUtil;
import top.theillusivec4.champions.world.entity.affix.Affix;
import top.theillusivec4.champions.world.entity.affix.AffixHelper;
import top.theillusivec4.champions.world.entity.affix.AffixInstance;
import top.theillusivec4.champions.world.entity.champion.ChampionMobPreset;
import top.theillusivec4.champions.world.entity.champion.Rank;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class ChampionMobPropertyHelper {

  private ChampionMobPropertyHelper() {
  }

  public static int getTier(Entity entity) {
    return get(entity).tier();
  }

  public static void update(Entity entity, Consumer<ChampionMobProperty.Mutable> consumer) {
    ChampionMobProperty.Mutable mutable = get(entity).mutable();
    consumer.accept(mutable);
    set(entity, mutable.toImmutable());
  }

  public static void setTier(Entity entity, int tier) {
    if (tier > 0 && getTier(entity) != tier) {
      update(entity, mutable -> mutable.setTier(tier));
    }
  }

  public static Component getPrefix(Entity entity) {
    return get(entity).prefix();
  }

  public static void setPrefix(Entity entity, Component prefix) {
    if (!getPrefix(entity).equals(prefix)) {
      update(entity, mutable -> mutable.setPrefix(prefix));
    }
  }

  public static TextColor getColor(Entity entity) {
    return get(entity).color();
  }

  public static ChampionsServerBossEvent getBossbar(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.BOSS_EVENT).orElse(ChampionsServerBossEvent.EMPTY);
  }

  public static Component getDisplayName(Entity entity) {
    MutableComponent component = Component.empty().copy();
    Component prefix = getPrefix(entity);
    if (!prefix.equals(CommonComponents.EMPTY)) {
      component.append(prefix);
      component.append(CommonComponents.SPACE);
    }
    component.append(entity.getDisplayName());
    component.withStyle(style -> style.withColor(getColor(entity)));
    return component;
  }

  public static boolean isBoss(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.BOSS_EVENT).isPresent();
  }

  public static void addToTooltip(ItemStack item, Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
    ChampionMobProperty state = get(item);
    if (state != ChampionMobProperty.EMPTY) {
      tooltipAdder.accept(
        Component.translatable("item.champions.champion.tier", Component.translatable("champions.champion.tier." + state.tier()).withStyle(style -> style.withColor(state.color()))).withStyle(ChatFormatting.GRAY)
      );
      tooltipAdder.accept(
        Component.translatable("item.champions.champion.prefix", state.prefix().copy().withStyle(style -> style.withColor(state.color()))).withStyle(ChatFormatting.GRAY)
      );
      tooltipAdder.accept(
        Component.translatable("item.champions.champion.color", Component.translatable("champions.champion.color").withStyle(style -> style.withColor(state.color()))).withStyle(ChatFormatting.GRAY)
      );
      tooltipAdder.accept(
        Component.translatable("item.champions.champion.boss", state.boss() ? Component.translatable("champions.champion.boss.true") : Component.translatable("champions.champion.boss.false")).withStyle(ChatFormatting.GRAY)
      );
    }
  }

  public static void setColor(Entity entity, TextColor color) {
    if (!getColor(entity).equals(color)) {
      update(entity, mutable -> mutable.setColor(color));
    }
  }

  public static void addBossbar(Entity entity) {
    ChampionsServerBossEvent event = getBossbar(entity);
    if (event == ChampionsServerBossEvent.EMPTY) {
      event = new ChampionsServerBossEvent(entity.getUUID(), getDisplayName(entity));
      event.setTier(getTier(entity));
      event.setColor(getColor(entity));
      event.setAffixes(AffixHelper.get(entity));
      entity.setData(ChampionsAttachments.BOSS_EVENT, event);
    }
  }

  public static ChampionMobProperty get(ItemStack itemStack) {
    return itemStack.getOrDefault(ChampionsDataComponents.STORED_CHAMPION_MOB_PROPERTY, ChampionMobProperty.EMPTY);
  }

  public static ChampionMobProperty get(Entity entity) {
    return entity.getExistingData(ChampionsAttachments.CHAMPION_MOB_PROPERTY).orElse(ChampionMobProperty.EMPTY);
  }

  public static void set(ItemStack item, ChampionMobProperty state) {
    if (!get(item).equals(state)) {
      item.set(ChampionsDataComponents.STORED_CHAMPION_MOB_PROPERTY, state);

      if (item.getItem() instanceof SpawnEggItem eggItem) {
        ChampionMobProperty stored = get(item);
        Component name = stored.prefix().copy()
          .append(CommonComponents.SPACE)
          .append(eggItem.getDefaultType().getDescription())
          .withStyle(style -> style.withColor(stored.color()));
        item.set(DataComponents.ITEM_NAME, name);
      }
    }
  }

  public static void set(Entity entity, ChampionMobProperty state) {
    if (!get(entity).equals(state)) {
      entity.setData(ChampionsAttachments.CHAMPION_MOB_PROPERTY, state);
      refreshBossbar(entity);
    }
  }

  public static void removeBossBar(Entity entity) {
    ChampionsServerBossEvent event = entity.removeData(ChampionsAttachments.BOSS_EVENT);
    if (event != null) {
      event.removeAllPlayers();
    }
  }

  private static Optional<Holder<Rank>> selectRank(RandomSource random, Entity entity, Stream<? extends Holder<Rank>> possible) {
    return selectRank(random, entity.getType(), possible);
  }

  private static Optional<Holder<Rank>> selectRank(RandomSource random, EntityType<?> entity, Stream<? extends Holder<Rank>> possible) {
    return ChampionsUtil.getRandom(random,
      possible.filter(rank -> rank.value().isSupported(entity)).toList()
    );
  }

  public static void doApplyPreset(ServerLevel level, Entity entity, DifficultyInstance difficulty) {
    ChampionMobPreset preset = getPreset(entity);
    if (preset != null) {
      preset.apply(level, level.getRandom(), entity, difficulty);
    }
  }

  public static void doFinalizeSpawn(ServerLevel level, Mob mob, double x, double y, double z, DifficultyInstance difficulty, MobSpawnType reason) {
    RandomSource random = level.getRandom();
    HolderLookup<Affix> affixes = level.registryAccess().lookupOrThrow(ChampionsRegistries.AFFIX);
    HolderLookup<Rank> ranks = level.registryAccess().lookupOrThrow(ChampionsRegistries.RANK);

    if (difficulty.isHarderThan(ChampionsServerConfig.CHAMPION_SPAWN_DIFFICULTY_THRESHOLD.get().floatValue())) {
      selectRank(random, mob, ranks.listElements()).ifPresent(rank -> {
        List<AffixInstance> list = AffixHelper.selectAffixByLevel(
          random,
          mob.getType(),
          rank.value().tier(),
          affixes.listElements(),
          rank.value().createAffixInstances(mob, random, difficulty).toList()
        );
        if (!list.isEmpty()) {
          AffixHelper.update(mob,
            mutable -> list.forEach(instance -> mutable.upgrade(instance.affix(), instance.level()))
          );
          doApplyRank(mob, rank);
        }
      });
    }
  }

  private static @Nullable ChampionMobPreset getPreset(Entity entity) {
    //noinspection deprecation
    return entity.getType().builtInRegistryHolder().getData(ChampionsDataMaps.CHAMPION_MOB_PRESET);
  }

  private static void doApplyRank(Entity entity, Holder<Rank> rank) {
    update(entity, mutable ->
      mutable.setPrefix(rank.value().description())
        .setTier(rank.value().tier())
        .setColor(rank.value().color())
        .setBoss(rank.value().boss())
    );
    refreshBossbar(entity);
  }

  public static void doParticlesEffects(Entity entity) {
    if (get(entity) != ChampionMobProperty.EMPTY) {
      RandomSource randomSource = entity.getRandom();
      Vec3 position = entity.position();
      double x = position.x() + (randomSource.nextDouble() - 0.5) * entity.getBbWidth();
      double y = position.y() + randomSource.nextDouble() * entity.getBbHeight();
      double z = position.z() + (randomSource.nextDouble() - 0.5) * entity.getBbWidth();
      int color = getColor(entity).getValue();
      entity.level().addParticle(ChampionsParticleTypes.champion(color), x, y, z, 1.0f, 1.0f, 1.0f);
    }
  }

  public static void updateBossbarPlayers(Entity entity) {
    if (entity.level() instanceof ServerLevel level) {
      ChampionsServerBossEvent event = getBossbar(entity);
      if (event != ChampionsServerBossEvent.EMPTY) {
        for (ServerPlayer player : level.players()) {
          if (player.blockPosition().distSqr(entity.blockPosition()) <= 3025.0) {
            event.addPlayer(player);
          } else {
            event.removePlayer(player);
          }
        }
      }
    }
  }

  public static void updateBossbarProgress(Entity entity) {
    if (entity.level() instanceof ServerLevel && entity instanceof LivingEntity livingEntity) {
      ChampionsServerBossEvent event = getBossbar(entity);
      float progress = livingEntity.getHealth() / livingEntity.getMaxHealth();
      if (!Float.isNaN(progress)) {
        event.setProgress(progress);
      }
    }
  }

  /*
    refresh并不关注对boss标志的更改，它不会创建新的BossBar，只会尝试将现有数据更新至BossBar，如果BossBar是EMPTY，那么则无事发生
   */
  public static void refreshBossbar(Entity entity) {
    ChampionsServerBossEvent event = getBossbar(entity);
    event.setName(getDisplayName(entity));
    event.setColor(getColor(entity));
    event.setTier(getTier(entity));
    event.setAffixes(AffixHelper.get(entity));
  }

  public static void makeBoss(Entity entity) {
    addBossbar(entity);
  }
}
