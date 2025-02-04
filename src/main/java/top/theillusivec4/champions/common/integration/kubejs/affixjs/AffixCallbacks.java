package top.theillusivec4.champions.common.integration.kubejs.affixjs;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.api.IChampion;

public class AffixCallbacks {
	@FunctionalInterface
	public interface OnInitialSpawnCallback {
		void onInitialSpawn(IChampion champion);
	}
	
	@FunctionalInterface
	public interface OnSpawnCallback {
		void onSpawn(IChampion champion);
	}
	
	@FunctionalInterface
	public interface OnServerUpdateCallback {
		void onServerUpdate(IChampion champion);
	}
	
	@FunctionalInterface
	public interface OnClientUpdateCallback {
		void onClientUpdate(IChampion champion);
	}
	
	@FunctionalInterface
	public interface OnAttackCallback {
		boolean onAttack(IChampion champion, LivingEntity target, DamageSource source, float amount);
	}
	
	@FunctionalInterface
	public interface OnAttackedCallback {
		boolean onAttacked(IChampion champion, DamageSource source, float amount);
	}
	
	@FunctionalInterface
	public interface OnHurtCallback {
		float onHurt(IChampion champion, DamageSource source, float amount, float newAmount);
	}
	@FunctionalInterface
	public interface OnHealCallback {
		float onHeal(IChampion champion, float amount, float newAmount);
	}
	@FunctionalInterface
	public interface OnDamageCallback {
		float onDamage(IChampion champion, DamageSource source, float amount, float newAmount);
	}
	@FunctionalInterface
	public interface OnDeathCallback {
		boolean onDeath(IChampion champion, DamageSource source);
	}
}
