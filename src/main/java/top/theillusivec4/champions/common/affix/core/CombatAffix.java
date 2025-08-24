package top.theillusivec4.champions.common.affix.core;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffixCombatHandler;

public abstract class CombatAffix extends BasicAffix implements IAffixCombatHandler {
	@Override
	public boolean onAttack(IChampion champion, LivingEntity target, DamageSource source, float amount) {
		return true;
	}

	@Override
	public boolean onAttacked(IChampion champion, DamageSource source, float amount) {
		return true;
	}

	@Override
	public float onHurt(IChampion champion, DamageSource source, float amount, float newAmount) {
		return newAmount;
	}

	@Override
	public float onHeal(IChampion champion, float amount, float newAmount) {
		return newAmount;
	}

	@Override
	public float onDamage(IChampion champion, DamageSource source, float amount, float newAmount) {
		return newAmount;
	}

	@Override
	public boolean onDeath(IChampion champion, DamageSource source) {
		return true;
	}
}
