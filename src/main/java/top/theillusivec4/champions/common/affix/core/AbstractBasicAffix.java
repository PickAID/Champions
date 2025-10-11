package top.theillusivec4.champions.common.affix.core;

import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffix;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.network.NetworkHandler;
import top.theillusivec4.champions.common.network.SPacketSyncAffixData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractBasicAffix implements IAffix {
	public static final String DEFAULT_PREFIX = "affix.";
	protected AffixSetting setting = AffixSetting.empty();
	private ResourceLocation name;

    public static boolean canTarget(LivingEntity livingEntity, @Nullable LivingEntity target, boolean sightCheck) {

		if (target == null || !target.isAlive() || target instanceof ArmorStandEntity || (sightCheck && !hasLineOfSight(livingEntity, target))) {
			return false;
		}
	    ModifiableAttributeInstance attributeInstance = livingEntity.getAttribute(Attributes.FOLLOW_RANGE);
		double range = attributeInstance == null ? 16.0D : attributeInstance.getValue();
		range = ChampionsConfig.affixTargetRange == 0 ? range : Math.min(range, ChampionsConfig.affixTargetRange);
		return livingEntity.distanceTo(target) <= range;
	}

	private static boolean hasLineOfSight(LivingEntity livingEntity, LivingEntity target) {

		if (livingEntity instanceof MobEntity) {
			MobEntity mob = (MobEntity) livingEntity;
			return mob.getSensing().canSee(target);
		} else {
			return livingEntity.canSee(target);
		}
	}

	@Override
	public ResourceLocation getIdentifier() {
		return Champions.API.getAffixId(this).orElseThrow(IllegalStateException::new);
	}

	@Override
	public String toString() {
		return this.getIdentifier().toString();
	}

	@Override
	public AffixCategory getCategory() {
		return setting.category();
	}

	@Override
	public boolean isEnabled() {
		return setting.enabled();
	}

	@Override
	public ConfigEnums.Permission getMobPermission() {
		return setting.mobPermission().orElse(ConfigEnums.Permission.BLACKLIST);
	}

	@Override
	public String getPrefix() {
		return setting.prefix().orElse(DEFAULT_PREFIX);
	}

	@Override
	public void sync(IChampion champion) {
		LivingEntity livingEntity = champion.getLivingEntity();
		CompoundNBT tag = this.writeSyncTag(champion);
		NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> livingEntity), new SPacketSyncAffixData(livingEntity.getId(), this.getIdentifier(), tag));
	}

	public boolean canApply(IChampion champion) {
		boolean isValidEntity;
		ResourceLocation entityKey = ForgeRegistries.ENTITIES.getKey(champion.getLivingEntity().getType());
		if (isBlackList()) {
			isValidEntity = getMobList().map(mobList -> !mobList.contains(entityKey)).orElse(true);
		} else {
			isValidEntity = getMobList().map(mobList -> mobList.contains(entityKey)).orElse(false);
		}
		return isEnabled() && isValidEntity && champion.getServer().getRank().map(rank -> getTier().matches(rank.getTier())).orElse(false);
	}

	@Override
	public MinMaxBounds.IntBound getTier() {
		return setting.tier().orElse(MinMaxBounds.IntBound.atLeast(1));
	}

	@Override
	public Optional<List<ResourceLocation>> getMobList() {
		return setting.mobList();
	}

	@Override
	public AffixSetting getSetting() {
		if (this.setting == null) {
			this.setting = createDefaultSetting();
		}
		return this.setting;
	}

	@Override
	public void applySetting(AffixSetting affixSetting) {
		this.setting = affixSetting;
	}

	public void applyDefaultSettingWithId() {
		applySetting(createDefaultSetting().withNewType(getIdentifier()));
	}

	public boolean isBlackList() {
		return getMobPermission() == ConfigEnums.Permission.BLACKLIST;
	}

	public abstract AffixSetting createDefaultSetting();

	@Override
	public IAffix setRegistryName(ResourceLocation name) {
		this.name = name;
		return this;
	}

	@Override
	public @Nullable ResourceLocation getRegistryName() {
		return name;
	}

	@Override
	public Class<IAffix> getRegistryType() {
		return IAffix.class;
	}
}
