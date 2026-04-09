package top.theillusivec4.champions.deprecated.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface IAffix {
  /**
   * Get IAffix id
   * @return String of IAffix id
   */
  String getIdentifier();

  /**
   * Get IAffix's Category
   * @return AffixCategory
   */
  AffixCategory getCategory();

  /**
   * Initial Spawn IChampion mob
   * @param champion to Initial Spawn
   */
  default void onInitialSpawn(IChampion champion) {

  }

  /**
   * When Spawning IChampion mob
   * @param champion to Spawning
   */
  default void onSpawn(IChampion champion) {

  }

  /**
   * When server handles this affix to champion, such as change mob attack, make mob harder to kill.
   * @param champion to apply Affix
   */
  default void onServerUpdate(IChampion champion) {

  }

  /**
   * When a client handles this affix to champion. Such as render Particle, some attack effects to render.
   * @param champion to render effects
   */
  default void onClientUpdate(IChampion champion) {

  }

  /**
   * When IChampion mob attacking a target living entity
   * @param champion attacker
   * @param target to attack
   * @param source kind of damage source
   * @param amount of damage
   * @return able to apply attack from attacker
   */
  default boolean onAttack(IChampion champion, LivingEntity target, DamageSource source,
                           float amount) {
    return true;
  }

  /**
   * When IChampion mob attacked a target living entity
   * @param champion attacker
   * @param source kind of damage source
   * @param amount of damage
   * @return able to apply attacker damage to target
   */
  default boolean onAttacked(IChampion champion, DamageSource source, float amount) {
    return true;
  }

  /**
   * When IChamping mob being hurt
   * @param champion mob who having this affix
   * @param source kind of damage source
   * @param amount this damage
   * @param newAmount new damage to calculate
   * @return calculated new damage amount applies to champion mob
   */
  default float onHurt(IChampion champion, DamageSource source, float amount, float newAmount) {
    return newAmount;
  }

  /**
   * When IChampion mob being healed
   * @param champion mob who having this affix
   * @param amount heal amount
   * @param newAmount new heal amount
   * @return calculated new heals amount applies to champion mob
   */
  default float onHeal(IChampion champion, float amount, float newAmount) {
    return newAmount;
  }

  /**
   * When IChamping mob hurt,but pre-discount hearts
   * @param champion mob who having this affix
   * @param source kind of damage source
   * @param amount this damage
   * @param newAmount new damage to calculate
   * @return calculated final discount hearts
   */
  default float onDamage(IChampion champion, DamageSource source, float amount, float newAmount) {
    return newAmount;
  }

  /**
   * When IChampion mob being death or dead
   * @param champion mob who having this affix
   * @param source damage kind make champion mob death
   * @return able to let champion death
   */
  default boolean onDeath(IChampion champion, DamageSource source) {
    return true;
  }

  /**
   * Does this IChampion mob can apply this Affix?
   * @param champion to apply affix
   * @return able to apply affix
   */
  default boolean canApply(IChampion champion) {
    return true;
  }

  /**
   * Is this Affix compatible to another affix?
   * @param affix another affix
   * @return able to compatible affix
   */
  default boolean isCompatible(IAffix affix) {
    return affix != this;
  }

  /**
   * sync champion data to champion mob
   * @param champion to sync
   */
  void sync(IChampion champion);

  /**
   * Read synced data at a client
   * @param champion client champion to save data
   * @param tag data to save
   */
  default void readSyncTag(IChampion champion, CompoundTag tag) {

  }

  /**
   * Write data for sync to server
   * @param champion target to sync
   * @return to sync data
   */
  default CompoundTag writeSyncTag(IChampion champion) {
    return new CompoundTag();
  }
}
