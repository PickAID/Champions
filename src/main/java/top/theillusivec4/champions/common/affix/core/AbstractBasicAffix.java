package top.theillusivec4.champions.common.affix.core;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.Champions;
import top.theillusivec4.champions.api.*;
import top.theillusivec4.champions.common.config.ChampionsConfig;
import top.theillusivec4.champions.common.config.ConfigEnums;
import top.theillusivec4.champions.common.network.NetworkHandler;
import top.theillusivec4.champions.common.network.SPacketSyncAffixData;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractBasicAffix implements IAffix {
    public static final String DEFAULT_PREFIX = "affix.";
    public AffixSetting affixSetting = AffixSetting.empty();

    public static boolean canTarget(LivingEntity livingEntity, @Nullable LivingEntity target, boolean sightCheck) {

        if (target == null || !target.isAlive() || target instanceof ArmorStand || (sightCheck && !hasLineOfSight(livingEntity, target))) {
            return false;
        }
        AttributeInstance attributeInstance = livingEntity.getAttribute(Attributes.FOLLOW_RANGE);
        double range = attributeInstance == null ? 16.0D : attributeInstance.getValue();
        range = ChampionsConfig.affixTargetRange == 0 ? range : Math.min(range, ChampionsConfig.affixTargetRange);
        return livingEntity.distanceTo(target) <= range;
    }

    private static boolean hasLineOfSight(LivingEntity livingEntity, LivingEntity target) {

        if (livingEntity instanceof Mob mob) {
            return mob.getSensing().hasLineOfSight(target);
        } else {
            return livingEntity.hasLineOfSight(target);
        }
    }

    public static <T extends IAffix, B extends BasicAffixBuilder<T>> Codec<T> codec(Supplier<B> affixType) {
        return BasicAffixBuilder.of(affixType);
    }

    @Override
    public ResourceLocation getIdentifier() {
        return Champions.API.getAffixId(this).orElseThrow();
    }

    @Override
    public boolean hasSubscriptions() {
        return affixSetting.hasSub().orElse(false);
    }

    @Override
    public String toString() {
        return this.getIdentifier().toString();
    }

    @Override
    public AffixCategory getCategory() {
        return affixSetting.category();
    }

    @Override
    public boolean isEnabled() {
        return affixSetting.enabled();
    }

    public ConfigEnums.Permission getMobPermission() {
        return affixSetting.mobPermission().orElse(ConfigEnums.Permission.BLACKLIST);
    }

    @Override
    public String getPrefix() {
        return affixSetting.prefix().orElse(DEFAULT_PREFIX);
    }

    @Override
    public void sync(IChampion champion) {
        LivingEntity livingEntity = champion.getLivingEntity();
        CompoundTag tag = this.writeSyncTag(champion);
        NetworkHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> livingEntity),
                new SPacketSyncAffixData(livingEntity.getId(), this.getIdentifier(), tag));
    }

    public boolean canApply(IChampion champion) {
        boolean isValidEntity;
        var entityKey = ForgeRegistries.ENTITY_TYPES.getKey(champion.getLivingEntity().getType());
        if (isBlackList()) {
            isValidEntity = getMobList().map(mobList -> !mobList.contains(entityKey)).orElse(true);
        } else {
            isValidEntity = getMobList().map(mobList -> mobList.contains(entityKey)).orElse(false);
        }
        return isEnabled() && isValidEntity && champion.getServer().getRank().map(rank -> getTier().matches(rank.getTier())).orElse(false);
    }

    @Override
    public MinMaxBounds.Ints getTier() {
        return affixSetting.tier().orElse(MinMaxBounds.Ints.atLeast(1));
    }

    @Override
    public Optional<List<ResourceLocation>> getMobList() {
        return affixSetting.mobList();
    }

    @Override
    public void applySetting(AffixSetting affixSetting) {
        this.affixSetting = affixSetting;
    }

    public boolean isBlackList() {
        return getMobPermission() == ConfigEnums.Permission.BLACKLIST;
    }

    @Override
    public AffixSetting getSetting() {
        return affixSetting;
    }
}
