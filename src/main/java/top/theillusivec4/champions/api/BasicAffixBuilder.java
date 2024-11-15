package top.theillusivec4.champions.api;

import java.util.function.Supplier;

/**
 * Basic affix builder for custom affix behavior
 *
 * @param <T> Affix type
 */
public class BasicAffixBuilder<T extends IAffix> implements IAffixBuilder<T> {

    private final Supplier<T> affixSupplier;
    private AffixCategory category;
    private String prefix;
    private boolean hasSubscriptions;

    public BasicAffixBuilder(Supplier<T> affixSupplier) {
        this.affixSupplier = affixSupplier;
    }

    @Override
    public BasicAffixBuilder<T> setCategory(AffixCategory pCategory) {
        this.category = pCategory;
        return this;
    }

    @Override
    public BasicAffixBuilder<T> setPrefix(String pPrefix) {
        this.prefix = pPrefix;
        return this;
    }

    @Override
    public IAffixBuilder<T> setHasSubscriptions() {
        this.hasSubscriptions = true;
        return this;
    }

    @Override
    public T build() {
        var affix = affixSupplier.get();
        apply(affix);
        return affix;
    }

    private void apply(T pAffix) {
        pAffix.setSubscriptions(this.hasSubscriptions);
        pAffix.setCategory(this.category);
        pAffix.setPrefix(this.prefix);
    }
}
