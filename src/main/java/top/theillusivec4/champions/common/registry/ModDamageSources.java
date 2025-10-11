package top.theillusivec4.champions.common.registry;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

public class ModDamageSources {

    // 静态DamageSource实例，用于直接伤害
    public static final String ENKINDLING_BULLET = "enkindling_bullet";
    public static final String REFLECTION_DAMAGE = "reflection";

    // 获取直接伤害源
    public static DamageSource cinder(Entity attacker) {
        return new EntityDamageSource(ENKINDLING_BULLET, attacker).setIsFire().setMagic();
    }

    // 获取间接伤害源
    public static DamageSource cinderIndirect(Entity projectile, Entity attacker) {
        return new IndirectEntityDamageSource(ENKINDLING_BULLET, projectile, attacker)
                .setIsFire()
                .setMagic();
    }
}
