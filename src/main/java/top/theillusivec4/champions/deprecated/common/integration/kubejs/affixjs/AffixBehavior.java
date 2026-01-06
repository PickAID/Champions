package top.theillusivec4.champions.deprecated.common.integration.kubejs.affixjs;

import top.theillusivec4.champions.deprecated.common.integration.kubejs.affixjs.AffixCallbacks.*;

@Deprecated
public class AffixBehavior {
  protected OnInitialSpawnCallback onInitialSpawnCallback;
  protected OnSpawnCallback onSpawnCallback;
  protected OnServerUpdateCallback onServerUpdateCallback;
  protected OnClientUpdateCallback onClientUpdateCallback;
  protected OnAttackCallback onAttackCallback;
  protected OnAttackedCallback onAttackedCallback;
  protected OnHurtCallback onHurtCallback;
  protected OnHealCallback onHealCallback;
  protected OnDamageCallback onDamageCallback;
  protected OnDeathCallback onDeathCallback;

  /**
   * Used for kubejs script
   */
  @SuppressWarnings("unused")
  public AffixBehavior onInitialSpawn(OnInitialSpawnCallback callback) {
    this.onInitialSpawnCallback = callback;
    return this;
  }

  /**
   * Used for kubejs script
   */
  @SuppressWarnings("unused")
  public AffixBehavior onSpawn(OnSpawnCallback callback) {
    this.onSpawnCallback = callback;
    return this;
  }

  /**
   * Used for kubejs script
   */
  @SuppressWarnings("unused")
  public AffixBehavior onServerUpdate(OnServerUpdateCallback callback) {
    this.onServerUpdateCallback = callback;
    return this;
  }

  /**
   * Used for kubejs script
   */
  @SuppressWarnings("unused")
  public AffixBehavior onClientUpdate(OnClientUpdateCallback callback) {
    this.onClientUpdateCallback = callback;
    return this;
  }

  /**
   * Used for kubejs script
   */
  @SuppressWarnings("unused")
  public AffixBehavior onAttack(OnAttackCallback callback) {
    this.onAttackCallback = callback;
    return this;
  }

  /**
   * Used for kubejs script
   */
  @SuppressWarnings("unused")
  public AffixBehavior onAttacked(OnAttackedCallback callback) {
    this.onAttackedCallback = callback;
    return this;
  }

  /**
   * Used for kubejs script
   */
  @SuppressWarnings("unused")
  public AffixBehavior onHurt(OnHurtCallback callback) {
    this.onHurtCallback = callback;
    return this;
  }

  /**
   * Used for kubejs script
   */
  @SuppressWarnings("unused")
  public AffixBehavior onHeal(OnHealCallback callback) {
    this.onHealCallback = callback;
    return this;
  }

  /**
   * Used for kubejs script
   */
  @SuppressWarnings("unused")
  public AffixBehavior onDamage(OnDamageCallback callback) {
    this.onDamageCallback = callback;
    return this;
  }

  /**
   * Used for kubejs script
   */
  @SuppressWarnings("unused")
  public AffixBehavior onDeath(OnDeathCallback callback) {
    this.onDeathCallback = callback;
    return this;
  }
}
