package top.theillusivec4.champions.api.affix;

import top.theillusivec4.champions.api.IChampion;

public interface IAffixLifecycle {
    default void onInitialSpawn(IChampion champion){}
    default void onSpawn(IChampion champion){}
    default void onServerUpdate(IChampion champion){}
    default void onClientUpdate(IChampion champion){}
}