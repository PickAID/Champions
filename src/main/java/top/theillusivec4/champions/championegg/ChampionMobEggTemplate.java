package top.theillusivec4.champions.championegg;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import top.theillusivec4.champions.affix.AffixContainer;
import top.theillusivec4.champions.championmob.property.ChampionProperty;
import top.theillusivec4.champions.championmob.property.provider.ChampionPropertyProvider;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class ChampionMobEggTemplate {
  private static final Codec<ItemStack> SPAWN_EGG = ItemStack.CODEC.validate(itemStack -> {
    if (itemStack.getItem() instanceof SpawnEggItem) {
      return DataResult.success(itemStack);
    } else {
      return DataResult.error(() -> "SpawnEggTemplate item must be spawn egg.");
    }
  });
  public static final Codec<ChampionMobEggTemplate> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(SPAWN_EGG.fieldOf("item").forGetter(template -> template.item), ChampionPropertyProvider.DIRECT_CODEC.optionalFieldOf("property").forGetter(template -> template.property), AffixContainer.MAP_CODEC.orElse(AffixContainer.EMPTY).forGetter(template -> template.affixes)).apply(instance, ChampionMobEggTemplate::new));
  private final ItemStack item;
  private final Optional<ChampionPropertyProvider> property;
  private final AffixContainer affixes;

  private ChampionMobEggTemplate(ItemStack item, Optional<ChampionPropertyProvider> property, AffixContainer affixes) {
    this.item = item;
    this.property = property;
    this.affixes = affixes;
  }

  public static ChampionMobEggTemplate.Builder builder(Supplier<SpawnEggItem> supplier) {
    return new Builder(supplier);
  }

  public ItemStack create() {
    ItemStack stack = this.item.copy();
    ChampionProperty property = this.property.map(ChampionPropertyProvider::get).orElse(ChampionProperty.EMPTY);
    ChampionMobEggHelper.modifyItem(stack, property, this.affixes);
    return stack;
  }

  @Override
  public int hashCode() {
    return Objects.hash(item, property, affixes);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (ChampionMobEggTemplate) obj;
    return Objects.equals(this.item, that.item) && Objects.equals(this.property, that.property) && Objects.equals(this.affixes, that.affixes);
  }

  @Override
  public String toString() {
    return "SpawnEggTemplate[" + "item=" + item + ", " + "champion=" + property + ", " + "affixes=" + affixes + ']';
  }

  public static class Builder {
    private final Supplier<SpawnEggItem> itemSupplier;
    private Function<SpawnEggItem, ItemStack> itemFactory = ItemStack::new;
    private AffixContainer.Builder affixes = null;
    private ChampionPropertyProvider property = null;

    private Builder(Supplier<SpawnEggItem> itemSupplier) {
      this.itemSupplier = itemSupplier;
    }

    public void customItem(Function<SpawnEggItem, ItemStack> itemFactory) {
      this.itemFactory = itemFactory;
    }

    public Builder affixes(AffixContainer.Builder affixes) {
      this.affixes = affixes;
      return this;
    }

    public Builder property(ChampionPropertyProvider property) {
      this.property = property;
      return this;
    }

    public ChampionMobEggTemplate build() {
      return new ChampionMobEggTemplate(
        this.itemFactory.apply(this.itemSupplier.get()),
        Optional.ofNullable(this.property),
        Optional.ofNullable(this.affixes).map(AffixContainer.Builder::build).orElse(AffixContainer.EMPTY)
      );
    }
  }
}
