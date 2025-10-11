package top.theillusivec4.champions.common.entity;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class BaseBulletEntity extends ProjectileItemEntity {

    @Nullable
    private Entity finalTarget;
    @Nullable
    private Direction currentMoveDirection;
    private int flightSteps;
    private double targetDeltaX;
    private double targetDeltaY;
    private double targetDeltaZ;
    @Nullable
    private UUID targetId;

    public BaseBulletEntity(EntityType<? extends ProjectileItemEntity> type, World level) {
        super(type, level);
        this.noPhysics = true;
    }

    public BaseBulletEntity(EntityType<? extends ProjectileItemEntity> type, World level,
                            LivingEntity livingEntity, @Nonnull Entity entity, Direction.Axis axis) {
        this(type, level);
        this.setOwner(livingEntity);
        BlockPos blockpos = livingEntity.blockPosition();
        double d0 = (double) blockpos.getX() + 0.5D;
        double d1 = (double) blockpos.getY() + 0.5D;
        double d2 = (double) blockpos.getZ() + 0.5D;
        this.moveTo(d0, d1, d2, this.yRot, this.xRot);
        this.finalTarget = entity;
        this.currentMoveDirection = Direction.UP;
        this.selectNextMoveDirection(axis);
    }

    @Nonnull
    public SoundCategory getSoundSource() {
        return SoundCategory.HOSTILE;
    }

	@Override
    public void addAdditionalSaveData(@Nonnull CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);

        if (this.finalTarget != null) {
            pCompound.putUUID("Target", this.finalTarget.getUUID());
        }

        if (this.currentMoveDirection != null) {
            pCompound.putInt("Dir", this.currentMoveDirection.get3DDataValue());
        }
        pCompound.putInt("Steps", this.flightSteps);
        pCompound.putDouble("TXD", this.targetDeltaX);
        pCompound.putDouble("TYD", this.targetDeltaY);
        pCompound.putDouble("TZD", this.targetDeltaZ);
    }

	@Override
	public void readAdditionalSaveData(@Nonnull CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.flightSteps = pCompound.getInt("Steps");
        this.targetDeltaX = pCompound.getDouble("TXD");
        this.targetDeltaY = pCompound.getDouble("TYD");
        this.targetDeltaZ = pCompound.getDouble("TZD");

        if (pCompound.contains("Dir", 99)) {
            this.currentMoveDirection = Direction.from3DDataValue(pCompound.getInt("Dir"));
        }

        if (pCompound.hasUUID("Target")) {
            this.targetId = pCompound.getUUID("Target");
        }
    }

    protected void defineSynchedData() {
    }

    @Nullable
    private Direction getMoveDirection() {
        return this.currentMoveDirection;
    }

    private void setMoveDirection(@Nullable Direction pDirection) {
        this.currentMoveDirection = pDirection;
    }

    private void selectNextMoveDirection(@Nullable Direction.Axis p_37349_) {
        double d0 = 0.5D;
        BlockPos blockpos;

        if (this.finalTarget == null) {
            blockpos = this.blockPosition().below();
        } else {
            d0 = (double) this.finalTarget.getBbHeight() * 0.5D;
            blockpos = new BlockPos(this.finalTarget.blockPosition().getX(), (int) (this.finalTarget.blockPosition().getY() + d0),
                    this.finalTarget.blockPosition().getZ());
        }
        double d1 = (double) blockpos.getX() + 0.5D;
        double d2 = (double) blockpos.getY() + d0;
        double d3 = (double) blockpos.getZ() + 0.5D;
        Direction direction = null;

        if (!this.closerToCenterThan(blockpos, this.position(), 2.0F)) {
            BlockPos blockpos1 = this.blockPosition();
            List<Direction> list = Lists.newArrayList();

            if (p_37349_ != Direction.Axis.X) {

                if (blockpos1.getX() < blockpos.getX() && this.getLevel().isEmptyBlock(blockpos1.east())) {
                    list.add(Direction.EAST);
                } else if (blockpos1.getX() > blockpos.getX() &&
                        this.getLevel().isEmptyBlock(blockpos1.west())) {
                    list.add(Direction.WEST);
                }
            }

            if (p_37349_ != Direction.Axis.Y) {
                if (blockpos1.getY() < blockpos.getY() && this.getLevel().isEmptyBlock(blockpos1.above())) {
                    list.add(Direction.UP);
                } else if (blockpos1.getY() > blockpos.getY() &&
                        this.getLevel().isEmptyBlock(blockpos1.below())) {
                    list.add(Direction.DOWN);
                }
            }

            if (p_37349_ != Direction.Axis.Z) {
                if (blockpos1.getZ() < blockpos.getZ() && this.getLevel().isEmptyBlock(blockpos1.south())) {
                    list.add(Direction.SOUTH);
                } else if (blockpos1.getZ() > blockpos.getZ() &&
                        this.getLevel().isEmptyBlock(blockpos1.north())) {
                    list.add(Direction.NORTH);
                }
            }

            direction = Direction.getRandom(this.random);

            if (list.isEmpty()) {

                for (int i = 5; !this.getLevel().isEmptyBlock(blockpos1.relative(direction)) && i > 0; --i) {
                    direction = Direction.getRandom(this.random);
                }
            } else {
                direction = list.get(this.random.nextInt(list.size()));
            }
            d1 = this.getX() + (double) direction.getStepX();
            d2 = this.getY() + (double) direction.getStepY();
            d3 = this.getZ() + (double) direction.getStepZ();
        }

        this.setMoveDirection(direction);
        double d6 = d1 - this.getX();
        double d7 = d2 - this.getY();
        double d4 = d3 - this.getZ();
        double d5 = Math.sqrt(d6 * d6 + d7 * d7 + d4 * d4);

        if (d5 == 0.0D) {
            this.targetDeltaX = 0.0D;
            this.targetDeltaY = 0.0D;
            this.targetDeltaZ = 0.0D;
        } else {
            this.targetDeltaX = d6 / d5 * 0.15D;
            this.targetDeltaY = d7 / d5 * 0.15D;
            this.targetDeltaZ = d4 / d5 * 0.15D;
        }

        this.hasImpulse = true;
        this.flightSteps = 10 + this.random.nextInt(5) * 10;
    }

    private boolean closerToCenterThan(BlockPos pos, Vector3d position, float distance) {
        return this.distToCenterSqr(pos, position.x(), position.y(), position.z()) < MathHelper.square(distance);
    }

    private double distToCenterSqr(BlockPos pos, double x, double y, double z) {
        double d0 = pos.getX() + 0.5D - x;
        double d1 = pos.getY() + 0.5D - y;
        double d2 = pos.getZ() + 0.5D - z;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void checkDespawn() {

        if (this.getLevel().getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
        }
    }

    public void tick() {
        super.tick();

        if (!this.getLevel().isClientSide) {

            if (this.finalTarget == null && this.targetId != null) {
                this.finalTarget = ((ServerWorld) this.getLevel()).getEntity(this.targetId);

                if (this.finalTarget == null) {
                    this.targetId = null;
                }
            }

            if (this.finalTarget == null || !this.finalTarget.isAlive() ||
                    this.finalTarget instanceof PlayerEntity && this.finalTarget.isSpectator()) {

                if (!this.isNoGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
                }
            } else {
                this.targetDeltaX = MathHelper.clamp(this.targetDeltaX * 1.025D, -1.0D, 1.0D);
                this.targetDeltaY = MathHelper.clamp(this.targetDeltaY * 1.025D, -1.0D, 1.0D);
                this.targetDeltaZ = MathHelper.clamp(this.targetDeltaZ * 1.025D, -1.0D, 1.0D);
                Vector3d vec3 = this.getDeltaMovement();
                this.setDeltaMovement(
                        vec3.add((this.targetDeltaX - vec3.x) * 0.2D, (this.targetDeltaY - vec3.y) * 0.2D,
                                (this.targetDeltaZ - vec3.z) * 0.2D));
            }

	        RayTraceResult hitresult = ProjectileHelper.getHitResult(this,   this::canHitEntity);

            if (hitresult.getType() != RayTraceResult.Type.MISS &&
                    !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
        }
        this.checkInsideBlocks();
        Vector3d vec31 = this.getDeltaMovement();
        this.setPos(this.getX() + vec31.x, this.getY() + vec31.y, this.getZ() + vec31.z);
	    ProjectileHelper.rotateTowardsMovement(this, 0.5F);

        if (this.getLevel().isClientSide) {
            this.getLevel().addParticle(this.getParticle(), this.getX() - vec31.x,
                    this.getY() - vec31.y + 0.15D, this.getZ() - vec31.z, 0.0D, 0.0D, 0.0D);
        } else if (this.finalTarget != null && !this.finalTarget.isAlive()) {

            if (this.flightSteps > 0) {
                --this.flightSteps;

                if (this.flightSteps == 0) {
                    this.selectNextMoveDirection(
                            this.currentMoveDirection == null ? null : this.currentMoveDirection.getAxis());
                }
            }

            if (this.currentMoveDirection != null) {
                BlockPos blockpos = this.blockPosition();
                Direction.Axis direction$axis = this.currentMoveDirection.getAxis();

                if (this.getLevel().loadedAndEntityCanStandOn(blockpos.relative(this.currentMoveDirection),
                        this)) {
                    this.selectNextMoveDirection(direction$axis);
                } else {
                    BlockPos blockpos1 = this.finalTarget.blockPosition();
                    if (direction$axis == Direction.Axis.X && blockpos.getX() == blockpos1.getX() ||
                            direction$axis == Direction.Axis.Z && blockpos.getZ() == blockpos1.getZ() ||
                            direction$axis == Direction.Axis.Y && blockpos.getY() == blockpos1.getY()) {
                        this.selectNextMoveDirection(direction$axis);
                    }
                }
            }
        }

    }

	@Override
    protected boolean canHitEntity(@Nonnull Entity pTarget) {
        return super.canHitEntity(pTarget) && !pTarget.noPhysics;
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return pDistance < 16384.0D;
    }

    public float getBrightness() {
        return 1.0F;
    }

    protected void onHitEntity(@Nonnull EntityRayTraceResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();

        if (entity != this.getOwner() && entity instanceof LivingEntity) {
	        LivingEntity target = (LivingEntity) entity;
	        this.bulletEffect(target);
        }
    }

    protected void onHitBlock(@Nonnull BlockRayTraceResult hitResult) {
        super.onHitBlock(hitResult);
        if (this.getLevel() instanceof ServerWorld) {
	        ServerWorld serverLevel = (ServerWorld) this.getLevel();
	        serverLevel.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(),
                    this.getZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0F, 1.0F);
        }
    }

    protected void onHit(@Nonnull RayTraceResult pResult) {
        super.onHit(pResult);
        this.remove();
    }

    public boolean isPickable() {
        return true;
    }

    public boolean hurt(@Nonnull DamageSource pSource, float pAmount) {

        if (this.getLevel() instanceof ServerWorld) {
	        ServerWorld serverLevel = (ServerWorld) this.getLevel();
	        this.playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0F, 1.0F);
            serverLevel.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(),
                    this.getZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
            this.remove();
        }
        return true;
    }

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	protected abstract void bulletEffect(LivingEntity target);

    protected abstract BasicParticleType getParticle();

	World getLevel() {
		return this.level;
	}
}
