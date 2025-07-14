package top.theillusivec4.champions.common.affix.core;

import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.affix.IAffixCombatHandler;

import java.util.List;

public abstract class GoalCombatAffix extends GoalAffix implements IAffixCombatHandler {

    // 默认的战斗处理方法，子类可以重写
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

    // 抽象方法，子类必须实现
    @Override
    public abstract List<Tuple<Integer, Goal>> getGoals(IChampion champion);
}