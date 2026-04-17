package top.theillusivec4.champions.advancements.critereon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.champions.api.championmob.ChampionMobProperty;
import top.theillusivec4.champions.api.championmob.ChampionMobPropertyHelper;

import java.util.Optional;

public record ChampionPropertyPredicate(Optional<MinMaxBounds.Ints> tier, Optional<TextColor> color, Optional<Component> prefix, Optional<Boolean> boss) implements EntitySubPredicate {
  public static final MapCodec<ChampionPropertyPredicate> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
    MinMaxBounds.Ints.CODEC.optionalFieldOf("tier").forGetter(ChampionPropertyPredicate::tier),
    TextColor.CODEC.optionalFieldOf("color").forGetter(ChampionPropertyPredicate::color),
    ComponentSerialization.CODEC.optionalFieldOf("prefix").forGetter(ChampionPropertyPredicate::prefix),
    Codec.BOOL.optionalFieldOf("boss").forGetter(ChampionPropertyPredicate::boss)
  ).apply(instance, ChampionPropertyPredicate::new));

  public boolean matches(ChampionMobProperty property) {
    return (this.tier.isEmpty() || this.tier.get().matches(property.tier()))
      && (this.color.isEmpty() || this.color.get().equals(property.color()))
      && (this.prefix.isEmpty() || this.prefix.get().equals(property.prefix()))
      && (this.boss.isEmpty() || this.boss.get() == property.boss());
  }

  @Override
  public MapCodec<? extends EntitySubPredicate> codec() {
    return MAP_CODEC;
  }

  @Override
  public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
    ChampionMobProperty property = ChampionMobPropertyHelper.get(entity);
    return this.matches(property);
  }
}
