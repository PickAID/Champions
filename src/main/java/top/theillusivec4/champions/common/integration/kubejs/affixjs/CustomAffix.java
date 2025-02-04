package top.theillusivec4.champions.common.integration.kubejs.affixjs;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.common.affix.core.BasicAffix;
import top.theillusivec4.champions.common.integration.kubejs.affixjs.AffixCallbacks.*;

import javax.annotation.ParametersAreNullableByDefault;

public class CustomAffix extends BasicAffix {
    private final OnInitialSpawnCallback onInitialSpawnCallback;
    private final OnSpawnCallback onSpawnCallback;
    private final OnServerUpdateCallback onServerUpdateCallback;
    private final OnClientUpdateCallback onClientUpdateCallback;
    private final OnAttackCallback onAttackCallback;
    private final OnAttackedCallback onAttackedCallback;
    private final OnHurtCallback onHurtCallback;
    private final OnHealCallback onHealCallback;
    private final OnDamageCallback onDamageCallback;
    private final OnDeathCallback onDeathCallback;


    @ParametersAreNullableByDefault
    public CustomAffix(OnInitialSpawnCallback onInitialSpawnCallback,
                       OnSpawnCallback onSpawnCallback,
                       OnServerUpdateCallback onServerUpdateCallback,
                       OnClientUpdateCallback onClientUpdateCallback,
                       OnAttackCallback onAttackCallback,
                       OnAttackedCallback onAttackedCallback,
                       OnHurtCallback onHurtCallback,
                       OnHealCallback onHealCallback,
                       OnDamageCallback onDamageCallback,
                       OnDeathCallback onDeathCallback
    ) {
        this.onInitialSpawnCallback = onInitialSpawnCallback;
        this.onSpawnCallback = onSpawnCallback;
        this.onServerUpdateCallback = onServerUpdateCallback;
        this.onClientUpdateCallback = onClientUpdateCallback;
        this.onAttackCallback = onAttackCallback;
        this.onAttackedCallback = onAttackedCallback;
        this.onHurtCallback = onHurtCallback;
        this.onHealCallback = onHealCallback;
        this.onDamageCallback = onDamageCallback;
        this.onDeathCallback = onDeathCallback;
    }

    @Override
    public void onInitialSpawn(IChampion champion) {
        if (onInitialSpawnCallback != null) {
            onInitialSpawnCallback.onInitialSpawn(champion);
        }
    }

    @Override
    public void onSpawn(IChampion champion) {
        if (onSpawnCallback != null) {
            onSpawnCallback.onSpawn(champion);
        }
    }

    @Override
    public void onServerUpdate(IChampion champion) {
        if (onServerUpdateCallback != null) {
            onServerUpdateCallback.onServerUpdate(champion);
        }
    }

    @Override
    public void onClientUpdate(IChampion champion) {
        if (onClientUpdateCallback != null) {
            onClientUpdateCallback.onClientUpdate(champion);
        }
    }

    @Override
    public boolean onAttack(IChampion champion, LivingEntity target, DamageSource source, float amount) {
        if (onAttackCallback != null) {
            return onAttackCallback.onAttack(champion, target, source, amount);
        }
        return super.onAttack(champion, target, source, amount);
    }

    @Override
    public boolean onAttacked(IChampion champion, DamageSource source, float amount) {
        if (onAttackedCallback != null) {
            return onAttackedCallback.onAttacked(champion, source, amount);
        }
        return super.onAttacked(champion, source, amount);
    }

    @Override
    public float onHurt(IChampion champion, DamageSource source, float amount, float newAmount) {
        if (onHurtCallback != null) {
            return onHurtCallback.onHurt(champion, source, amount, newAmount);
        }
        return super.onHurt(champion, source, amount, newAmount);
    }

    @Override
    public float onHeal(IChampion champion, float amount, float newAmount) {
        if (onHealCallback != null) {
            return onHealCallback.onHeal(champion, amount, newAmount);
        }
        return super.onHeal(champion, amount, newAmount);
    }

    @Override
    public float onDamage(IChampion champion, DamageSource source, float amount, float newAmount) {
        if (onDamageCallback != null) {
            return onDamageCallback.onDamage(champion, source, amount, newAmount);
        }
        return super.onDamage(champion, source, amount, newAmount);
    }

    @Override
    public boolean onDeath(IChampion champion, DamageSource source) {
        if (onDeathCallback != null) {
            return onDeathCallback.onDeath(champion, source);
        }
        return super.onDeath(champion, source);
    }


    public static class Builder {
        private OnInitialSpawnCallback onInitialSpawnCallback;
        private OnSpawnCallback onSpawnCallback;
        private OnServerUpdateCallback onServerUpdateCallback;
        private OnClientUpdateCallback onClientUpdateCallback;
        private OnAttackCallback onAttackCallback;
        private OnAttackedCallback onAttackedCallback;
        private OnHurtCallback onHurtCallback;
        private OnHealCallback onHealCallback;
        private OnDamageCallback onDamageCallback;
        private OnDeathCallback onDeathCallback;

        public Builder() {

        }

        public Builder onInitialSpawn(OnInitialSpawnCallback callback) {
            this.onInitialSpawnCallback = callback;
            return this;
        }


        public Builder onSpawn(OnSpawnCallback callback) {
            this.onSpawnCallback = callback;
            return this;
        }


        public Builder onServerUpdate(OnServerUpdateCallback callback) {
            this.onServerUpdateCallback = callback;
            return this;
        }


        public Builder onClientUpdate(OnClientUpdateCallback callback) {
            this.onClientUpdateCallback = callback;
            return this;
        }


        public Builder onAttack(OnAttackCallback callback) {
            this.onAttackCallback = callback;
            return this;
        }


        public Builder onAttacked(OnAttackedCallback callback) {
            this.onAttackedCallback = callback;
            return this;
        }


        public Builder onHurt(OnHurtCallback callback) {
            this.onHurtCallback = callback;
            return this;
        }


        public Builder onHeal(OnHealCallback callback) {
            this.onHealCallback = callback;
            return this;
        }


        public Builder onDamage(OnDamageCallback callback) {
            this.onDamageCallback = callback;
            return this;
        }


        public Builder onDeath(OnDeathCallback callback) {

            this.onDeathCallback = callback;
            return this;
        }

        public CustomAffix build() {
            return new CustomAffix(this.onInitialSpawnCallback, this.onSpawnCallback, this.onServerUpdateCallback, this.onClientUpdateCallback, this.onAttackCallback, this.onAttackedCallback, this.onHurtCallback, this.onHealCallback, this.onDamageCallback, this.onDeathCallback);
        }
    }
}
