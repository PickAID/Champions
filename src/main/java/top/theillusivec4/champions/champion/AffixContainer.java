package top.theillusivec4.champions.champion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import top.theillusivec4.champions.champion.affix.Affix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public final class AffixContainer {
	public static final AffixContainer EMPTY = new AffixContainer(List.of());
	public static final StreamCodec<RegistryFriendlyByteBuf, AffixContainer> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.collection(ArrayList::new, Affix.STREAM_CODEC), AffixContainer::getAffixList,
			AffixContainer::new
	);
	public static final Codec<AffixContainer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Affix.REFERENCE_CODEC.listOf().fieldOf("affixes").forGetter(AffixContainer::getAffixList)
	).apply(instance, AffixContainer::createOrEmpty));

	public static final MapCodec<AffixContainer> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Affix.REFERENCE_CODEC.listOf().fieldOf("affixes").forGetter(AffixContainer::getAffixList)
	).apply(instance, AffixContainer::new));
	private final List<Holder<Affix>> affixes;

	private AffixContainer(List<Holder<Affix>> affixes) {
		this.affixes = List.copyOf(affixes);
	}

	public static AffixContainer createOrEmpty(List<Holder<Affix>> affixes) {
		if (affixes.isEmpty()) {
			return EMPTY;
		} else {
			return new AffixContainer(affixes);
		}
	}

	public boolean contains(Holder<Affix> affix) {
		return affixes.contains(affix);
	}

	public AffixContainer.Mutable mutable() {
		return new Mutable(this);
	}

	@Override
	public int hashCode() {
		return affixes.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else {
			return obj instanceof AffixContainer champion && this.affixes.equals(champion.affixes);
		}
	}

	@Override
	public String toString() {
		return "EntityChampion{affixes=" + this.affixes + "}";
	}

	public boolean isEmpty() {
		return this.affixes.isEmpty();
	}

	public List<Holder<Affix>> getAffixList() {
		return affixes;
	}


	public void visit(Consumer<Holder<Affix>> visitor) {
		this.affixes.forEach(visitor);
	}

	public static class Mutable {
		private final List<Holder<Affix>> affixes;

		public Mutable(AffixContainer affixContainer) {
			this.affixes = new ArrayList<>(affixContainer.affixes);
		}

		public Mutable() {
			this.affixes = new ArrayList<>();
		}

		public void addAll(Collection<? extends Holder<Affix>> affixes) {
			for (Holder<Affix> affix : affixes) {
				this.add(affix);
			}
		}

		public void add(Holder<Affix> affix) {
			if (!this.affixes.contains(affix)) {
				this.affixes.add(affix);
			}
		}

		public void clear() {
			this.affixes.clear();
		}

		public boolean contains(Holder<Affix> affix) {
			return this.affixes.contains(affix);
		}

		public void remove(Holder<Affix> affix) {
			this.affixes.remove(affix);
		}

		public AffixContainer toImmutable() {
			return this.affixes.isEmpty() ? AffixContainer.EMPTY : new AffixContainer(this.affixes);
		}
	}
}
