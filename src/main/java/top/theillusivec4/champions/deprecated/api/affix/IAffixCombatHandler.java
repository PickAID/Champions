package top.theillusivec4.champions.deprecated.api.affix;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.deprecated.api.IChampion;

@Deprecated
public interface IAffixCombatHandler {
	/**
	 * When IChampion mob attacking a target living entity
	 *
	 * @param champion attacker
	 * @param target   to attack
	 * @param source   kind of damage source
	 * @param amount   of damage
	 * @return able to apply attack from attacker
	 */
	boolean onAttack(IChampion champion, LivingEntity target, DamageSource source,
	                 float amount);

	/**
	 * When IChampion mob attacked a target living entity
	 *
	 * @param champion attacker
	 * @param source   kind of damage source
	 * @param amount   of damage
	 * @return able to apply attacker damage to target
	 */
	boolean onAttacked(IChampion champion, DamageSource source, float amount);

	/**
	 * When IChamping mob being hurt
	 *
	 * @param champion  mob who having this affix
	 * @param source    kind of damage source
	 * @param amount    this damage
	 * @param newAmount new damage to calculate
	 * @return calculated new damage amount applies to champion mob
	 */
	float onHurt(IChampion champion, DamageSource source, float amount, float newAmount);

	/**
	 * When IChampion mob being healed
	 *
	 * @param champion  mob who having this affix
	 * @param amount    heal amount
	 * @param newAmount new heal amount
	 * @return calculated new heals amount applies to champion mob
	 */
	float onHeal(IChampion champion, float amount, float newAmount);

	/**
	 * When IChamping mob hurt,but pre-discount hearts
	 *
	 * @param champion  mob who having this affix
	 * @param source    kind of damage source
	 * @param amount    this damage
	 * @param newAmount new damage to calculate
	 * @return calculated final discount hearts
	 */
	float onDamage(IChampion champion, DamageSource source, float amount, float newAmount);

	/**
	 * When IChampion mob being death or dead
	 *
	 * @param champion mob who having this affix
	 * @param source   damage kind make champion mob death
	 * @return able to let champion death
	 */
	boolean onDeath(IChampion champion, DamageSource source);

}
