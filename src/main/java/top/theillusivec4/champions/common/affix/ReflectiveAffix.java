package top.theillusivec4.champions.common.affix;


import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.champions.api.IChampion;
import top.theillusivec4.champions.api.data.AffixCategory;
import top.theillusivec4.champions.api.data.AffixSetting;
import top.theillusivec4.champions.common.affix.core.CombatAffix;
import top.theillusivec4.champions.common.config.ChampionsConfig;

public class ReflectiveAffix extends CombatAffix {
	private static final String REFLECTION_DAMAGE = "reflection";

	@SubscribeEvent
	public void onDamageEvent(LivingDamageEvent evt) {
		if (!ChampionsConfig.reflectiveLethal && evt.getSource().getMsgId().equals(REFLECTION_DAMAGE)) {
			LivingEntity living = evt.getEntityLiving();
			float currentDamage = evt.getAmount();

			if (currentDamage >= living.getHealth()) {
				evt.setAmount(living.getHealth() - 1);
			}
		}
	}

	@Override
	public float onDamage(IChampion champion, DamageSource source, float amount, float newAmount) {

		if (source.getDirectEntity() instanceof LivingEntity) {
			LivingEntity sourceEntity = (LivingEntity) source.getDirectEntity();

			if (source.getMsgId().equals(REFLECTION_DAMAGE) ||
					(source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns())) {
				return newAmount;
			}
			EntityDamageSource newSource =
					new EntityDamageSource(REFLECTION_DAMAGE, champion.getLivingEntity());
			newSource.setThorns();
			float min = (float) ChampionsConfig.reflectiveMinPercent;

			if (source.isFire()) {
				newSource.setIsFire();
			}

			if (source.isProjectile()) {
				newSource.setProjectile();
			}

			if (source.isExplosion()) {
				newSource.setExplosion();
			}

			if (source.isMagic()) {
				newSource.setMagic();
			}
			if (source.isBypassArmor()) {
				newSource.bypassArmor();
			}

			if (source.scalesWithDifficulty()) {
				newSource.setScalesWithDifficulty();
			}

			if (source.isBypassInvul()) {
				newSource.bypassInvul();
			}
			float damage = (float) Math.min(
					amount *
							(sourceEntity.getRandom().nextFloat() * (ChampionsConfig.reflectiveMaxPercent - min)
									+ min), ChampionsConfig.reflectiveMax);
			sourceEntity.hurt(newSource, damage);
		}
		return newAmount;
	}

	@Override
	public AffixSetting createDefaultSetting() {
		return AffixSetting.builder()
				.withDefault()
				.setCategory(AffixCategory.OFFENSE)
				.setHasSub()
				.build();
	}
}
