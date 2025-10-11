package top.theillusivec4.champions.common.loot;

import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.util.FakePlayer;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.capability.ChampionCapability;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.config.ConfigLoot;
import top.theillusivec4.champions.common.rank.Rank;
import top.theillusivec4.champions.common.util.Utils;

import javax.annotation.Nonnull;
import java.util.List;

public class ChampionLootModifier extends LootModifier {

	public ChampionLootModifier(ILootCondition[] conditions) {
		super(conditions);
	}

	@Nonnull
	@Override
	public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);

		if (entity == null) {
			return generatedLoot;
		}
		DamageSource damageSource = context.getParamOrNull(LootParameters.DAMAGE_SOURCE);

		if (damageSource == null) {
			return generatedLoot;
		}

		if (!entity.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT) ||
				(!ChampionsConfig.fakeLoot && damageSource.getDirectEntity() instanceof FakePlayer)) {
			return generatedLoot;
		}
		ChampionCapability.getCapability(entity).ifPresent(champion -> {
			IChampion.Server serverChampion = champion.getServer();
			ServerWorld serverWorld = (ServerWorld) entity.level;

			if (ChampionsConfig.lootSource != ConfigEnums.LootSource.CONFIG) {
				LootTable lootTable = serverWorld.getServer().getLootTables()
						.get(Utils.getLocation("champion_loot"));
				LootContext.Builder lootcontext$builder = (new LootContext.Builder(serverWorld)
						.withRandom(entity.level.getRandom())
						.withParameter(LootParameters.THIS_ENTITY, entity)
						.withParameter(LootParameters.ORIGIN, entity.position())
						.withParameter(LootParameters.DAMAGE_SOURCE, damageSource)
						.withOptionalParameter(LootParameters.KILLER_ENTITY, damageSource.getEntity())
						.withOptionalParameter(LootParameters.DIRECT_KILLER_ENTITY,
								damageSource.getDirectEntity()));

				if (entity instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) entity;
					LivingEntity attackingEntity = livingEntity.getKillCredit();

					if (attackingEntity instanceof PlayerEntity) {
						lootcontext$builder = lootcontext$builder
								.withParameter(LootParameters.LAST_DAMAGE_PLAYER, (PlayerEntity) attackingEntity)
								.withLuck(((PlayerEntity) attackingEntity).getLuck());
					}
				}
				lootTable.getRandomItemsRaw(lootcontext$builder.create(LootParameterSets.ENTITY),
						generatedLoot::add);
			}

			if (ChampionsConfig.lootSource != ConfigEnums.LootSource.LOOT_TABLE) {
				List<ItemStack> loot = ConfigLoot
						.getLootDrops(serverChampion.getRank().map(Rank::getTier).orElse(0));

				if (!loot.isEmpty()) {
					generatedLoot.addAll(loot);
				}
			}
		});
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<ChampionLootModifier> {

		@Override
		public ChampionLootModifier read(ResourceLocation name, JsonObject object,
		                                 ILootCondition[] conditions) {
			return new ChampionLootModifier(conditions);
		}

		@Override
		public JsonObject write(ChampionLootModifier instance) {
			return makeConditions(instance.conditions);
		}
	}
}
