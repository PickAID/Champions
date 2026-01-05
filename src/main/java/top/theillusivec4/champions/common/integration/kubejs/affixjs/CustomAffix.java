//package top.theillusivec4.champions.common.integration.kubejs.affixjs;
//
//import dev.latvian.mods.kubejs.registry.BuilderBase;
//import net.minecraft.resources.Identifier;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.LivingEntity;
//import top.theillusivec4.champions.Champions;
//import top.theillusivec4.champions.api.AffixSettingBuilder;
//import top.theillusivec4.champions.api.IChampion;
//import top.theillusivec4.champions.api.data.AffixSetting;
//import top.theillusivec4.champions.common.affix.core.CombatLifeCycleAffix;
//
//import java.util.function.Consumer;
//
//public class CustomAffix extends CombatLifeCycleAffix {
//  private final AffixBehavior behavior;
//
//  public CustomAffix(AffixSetting setting, AffixBehavior behavior) {
//    this.setting = setting;
//    this.behavior = behavior;
//  }
//
//  @Override
//  public void onInitialSpawn(IChampion champion) {
//    if (behavior.onInitialSpawnCallback != null) {
//      behavior.onInitialSpawnCallback.onInitialSpawn(champion);
//    }
//  }
//
//  @Override
//  public void onSpawn(IChampion champion) {
//    if (behavior.onSpawnCallback != null) {
//      behavior.onSpawnCallback.onSpawn(champion);
//    }
//  }
//
//  @Override
//  public void onServerUpdate(IChampion champion) {
//    if (behavior.onServerUpdateCallback != null) {
//      behavior.onServerUpdateCallback.onServerUpdate(champion);
//    }
//  }
//
//  @Override
//  public void onClientUpdate(IChampion champion) {
//    if (behavior.onClientUpdateCallback != null) {
//      behavior.onClientUpdateCallback.onClientUpdate(champion);
//    }
//  }
//
//  @Override
//  public boolean onAttack(IChampion champion, LivingEntity target, DamageSource source, float amount) {
//    if (behavior.onAttackCallback != null) {
//      return behavior.onAttackCallback.onAttack(champion, target, source, amount);
//    }
//    return super.onAttack(champion, target, source, amount);
//  }
//
//  @Override
//  public boolean onAttacked(IChampion champion, DamageSource source, float amount) {
//    if (behavior.onAttackedCallback != null) {
//      return behavior.onAttackedCallback.onAttacked(champion, source, amount);
//    }
//    return super.onAttacked(champion, source, amount);
//  }
//
//  @Override
//  public float onHurt(IChampion champion, DamageSource source, float amount, float newAmount) {
//    if (behavior.onHurtCallback != null) {
//      return behavior.onHurtCallback.onHurt(champion, source, amount, newAmount);
//    }
//    return super.onHurt(champion, source, amount, newAmount);
//  }
//
//  @Override
//  public float onHeal(IChampion champion, float amount, float newAmount) {
//    if (behavior.onHealCallback != null) {
//      return behavior.onHealCallback.onHeal(champion, amount, newAmount);
//    }
//    return super.onHeal(champion, amount, newAmount);
//  }
//
//  @Override
//  public float onDamage(IChampion champion, DamageSource source, float amount, float newAmount) {
//    if (this.behavior.onDamageCallback != null) {
//      return this.behavior.onDamageCallback.onDamage(champion, source, amount, newAmount);
//    }
//    return super.onDamage(champion, source, amount, newAmount);
//  }
//
//  @Override
//  public boolean onDeath(IChampion champion, DamageSource source) {
//    if (this.behavior.onDeathCallback != null) {
//      return this.behavior.onDeathCallback.onDeath(champion, source);
//    }
//    return super.onDeath(champion, source);
//  }
//
//  public static class Builder extends BuilderBase<CustomAffix> {
//    public AffixBehavior behavior;
//    public AffixSettingBuilder settingBuilder;
//
//    public Builder(Identifier id) {
//      super(id);
//      this.behavior = new AffixBehavior();
//      this.settingBuilder = new AffixSettingBuilder();
//    }
//
//    /**
//     * Used for kubejs script
//     */
//    @SuppressWarnings("unused")
//    public Builder behavior(Consumer<AffixBehavior> consumer) {
//      consumer.accept(this.behavior);
//      return this;
//    }
//
//    /**
//     * Used for kubejs script
//     */
//    @SuppressWarnings("unused")
//    public Builder settings(Consumer<AffixSettingBuilder> consumer) {
//      consumer.accept(this.settingBuilder);
//      return this;
//    }
//
//    @Override
//    public CustomAffix createObject() {
//      AffixSetting setting = settingBuilder.setType(id).build();
//      CustomAffix affix = new CustomAffix(setting, behavior);
//      Champions.API.addCategory(setting.category(), affix);
//      return affix;
//    }
//  }
//}
