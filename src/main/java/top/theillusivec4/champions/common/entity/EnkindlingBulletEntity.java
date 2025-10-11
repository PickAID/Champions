package top.theillusivec4.champions.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.world.World;
import top.theillusivec4.champions.common.registry.ModEntityTypes;

import javax.annotation.Nonnull;

public class EnkindlingBulletEntity extends BaseBulletEntity {

    public EnkindlingBulletEntity(World level) {
        super(ModEntityTypes.ENKINDLING_BULLET.get(), level);
    }

    public EnkindlingBulletEntity(World level, LivingEntity livingEntity, @Nonnull Entity entity,
                                  Direction.Axis axis) {
        super(ModEntityTypes.ENKINDLING_BULLET.get(), level, livingEntity, entity, axis);

    }

    public EnkindlingBulletEntity(EntityType<? extends EnkindlingBulletEntity> enkindlingBulletEntityEntityType, World level) {
        super(enkindlingBulletEntityEntityType, level);
    }

	@Override
    protected void bulletEffect(LivingEntity target) {

	    if (this.getOwner() != null) {
		    target.hurt(
				    new IndirectEntityDamageSource("cinderBullet.indirect", this, this.getOwner()).setIsFire()
						    .setMagic(), 1);
	    } else {
		    target.hurt(new EntityDamageSource("cinderBullet", this).setIsFire().setMagic(), 1);
	    }
	    target.setSecondsOnFire(8);
    }

    @Override
    protected BasicParticleType getParticle() {
        return ParticleTypes.FLAME;
    }

	@Override
	protected Item getDefaultItem() {
		return Items.SNOWBALL;
	}
}
