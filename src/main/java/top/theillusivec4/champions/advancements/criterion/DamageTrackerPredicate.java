package top.theillusivec4.champions.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.EntitySubPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.world.entity.damagetracker.DamageTracker;
import top.theillusivec4.champions.world.entity.damagetracker.DamageTrackerHelper;

import java.util.Map;
import java.util.Optional;

public record DamageTrackerPredicate(Map<Holder<DamageType>, net.minecraft.advancements.criterion.MinMaxBounds.Ints> damages, Optional<Holder<DamageType>> last) implements EntitySubPredicate {

	public static final MapCodec<DamageTrackerPredicate> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.unboundedMap(DamageType.CODEC, net.minecraft.advancements.criterion.MinMaxBounds.Ints.CODEC).fieldOf("damages").forGetter(DamageTrackerPredicate::damages),
			DamageType.CODEC.optionalFieldOf("last").forGetter(DamageTrackerPredicate::last)
	).apply(instance, DamageTrackerPredicate::new));

	public boolean matches(DamageTracker tracker) {
		if (this.last.isPresent()) {
			if (tracker.getLast() == null) {
				return false;
			} else if (this.last.get() != tracker.getLast()) {
				return false;
			}
		}

		for (Map.Entry<Holder<DamageType>, MinMaxBounds.Ints> entry : damages.entrySet()) {
			Holder<DamageType> type = entry.getKey();
			MinMaxBounds.Ints value = entry.getValue();
			if (!value.matches(tracker.getCount(type))) {
				return false;
			}
		}
		return true;
	}


	@Override
	public MapCodec<? extends EntitySubPredicate> codec() {
		return MAP_CODEC;
	}

	@Override
	public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
		DamageTracker tracker = DamageTrackerHelper.get(entity);
		return this.matches(tracker);
	}
}
