package top.theillusivec4.champions.integrations.probejs.docs;

import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.champions.api.affix.LevelBasedValue;
import top.theillusivec4.champions.api.affix.ProjectileTemplate;
import top.theillusivec4.champions.api.affix.effect.AffixEntityEffect;
import top.theillusivec4.champions.integrations.probejs.util.ProbeJSUtil;
import top.theillusivec4.champions.world.entity.affix.effects.AffixAttributeEffect;
import top.theillusivec4.champions.world.entity.affix.effects.AffixEntityEffects;

public final class AffixEntityEffectDocs {
  private AffixEntityEffectDocs() {
  }

  public static void assignType(ScriptDump scriptDump) {
    ProbeJSUtil.assignType(scriptDump, AffixEntityEffect.class, context -> {
      context.direct(Types.typeOf(AffixEntityEffect.class));
      context.dispatch("all_of", builder -> builder.member("effects", Types.type(AffixEntityEffect.class).asArray()));
      context.dispatch("apply_mob_effect", builder -> builder
        .member("to_apply", Types.primitive("Special.MobEffect").asArray())
        .member("min_duration", Types.type(LevelBasedValue.class))
        .member("max_duration", Types.type(LevelBasedValue.class))
        .member("min_amplifier", Types.type(LevelBasedValue.class))
        .member("max_amplifier", Types.type(LevelBasedValue.class))
      );
      context.dispatch("ignite", builder -> builder.member("duration", Types.type(LevelBasedValue.class)));
      context.dispatch("damage_entity", builder -> builder
        .member("min_damage", Types.type(LevelBasedValue.class))
        .member("max_damage", Types.type(LevelBasedValue.class))
        .member("damage_type", Types.primitive("Special.DamageType"))
      );
      context.dispatch("spawn_particles", builder -> builder
        .member("particle", Types.type(ParticleOptions.class))
        .member("count", Types.NUMBER)
        .member("horizontal_position", Types.type(AffixEntityEffects.SpawnParticlesEffect.PositionSource.class))
        .member("vertical_position", Types.type(AffixEntityEffects.SpawnParticlesEffect.PositionSource.class))
        .member("horizontal_velocity", Types.type(AffixEntityEffects.SpawnParticlesEffect.VelocitySource.class))
        .member("vertical_velocity", Types.type(AffixEntityEffects.SpawnParticlesEffect.VelocitySource.class))
        .member("speed", Types.type(FloatProvider.class))
      );
      context.dispatch("explode", builder -> builder
        .member("attribute_to_user", true, Types.BOOLEAN)
        .member("damage_type", true, Types.primitive("Special.DamageType"))
        .member("knockback_multiplier", true, Types.type(LevelBasedValue.class))
        .member("immune_blocks", true, Types.primitive("Special.Block").asArray())
        .member("offset", true, Types.type(Vec3.class))
        .member("radius", Types.type(LevelBasedValue.class))
        .member("create_fire", true, Types.type(LevelBasedValue.class))
        .member("block_interaction", Types.type(Level.ExplosionInteraction.class))
        .member("small_particle", Types.primitive("Special.ParticleType"))
        .member("large_particle", Types.primitive("Special.ParticleType"))
        .member("sound", Types.primitive("Special.SoundEvent"))
      );
      context.dispatch("iteration_entity", builder -> builder
        .member("horizontal_scale", Types.NUMBER)
        .member("horizontal_scale", Types.NUMBER)
        .member("predicate", true, Types.type(EntityPredicate.class))
        .member("effect", Types.type(AffixEntityEffect.class))
      );
      context.dispatch("play_sound", builder -> builder
        .member("sound", Types.primitive("Special.SoundEvent"))
        .member("volume", Types.type(FloatProvider.class))
        .member("pitch", Types.type(FloatProvider.class))
      );
      context.dispatch("projection", builder -> builder
        .member("projectile", Types.type(ProjectileTemplate.class))
        .member("projectile_item", Types.type(ItemStack.class))
        .member("power", Types.type(LevelBasedValue.class))
        .member("uncertainty", Types.type(LevelBasedValue.class))
        .member("sound", Types.primitive("Special.SoundEvent"))
      );
      context.dispatch("summon_entity", builder -> builder
        .member("entity", Types.primitive("Special.EntityType").asArray())
      );
      context.dispatch("movement", builder -> builder
        .member("speed", Types.NUMBER)
      );
    });
  }
}
