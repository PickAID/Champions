package top.theillusivec4.champions.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import top.theillusivec4.champions.common.registry.ModEntityTypes;

import javax.annotation.Nonnull;

public class ArcticBulletEntity extends BaseBulletEntity {

    public ArcticBulletEntity(World level) {
        super(ModEntityTypes.ARCTIC_BULLET.get(), level);
    }

    public ArcticBulletEntity(World level, LivingEntity livingEntity, @Nonnull Entity entity, Direction.Axis axis) {
        super(ModEntityTypes.ARCTIC_BULLET.get(), level, livingEntity, entity, axis);
    }

    public ArcticBulletEntity(EntityType<? extends ArcticBulletEntity> entityEntityType, World level) {
        super(entityEntityType, level);
    }

    @Override
    protected void bulletEffect(LivingEntity target) {
        target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 2));
        target.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 100, 2));
    }

    @Override
    protected BasicParticleType getParticle() {
        return ParticleTypes.ITEM_SNOWBALL;
    }

	@Override
	protected Item getDefaultItem() {
		return Items.SNOWBALL;
	}
}
